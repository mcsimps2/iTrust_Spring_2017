package edu.ncsu.csc.itrust.controller.obstetrics.officeVisit;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.Part;
import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;

import edu.ncsu.csc.itrust.controller.officeVisit.OfficeVisitController;
import edu.ncsu.csc.itrust.controller.officeVisit.OfficeVisitForm;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInit;
import edu.ncsu.csc.itrust.model.obstetrics.visit.ObstetricsVisit;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisitValidator;
import edu.ncsu.csc.itrust.model.old.beans.ApptBean;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

@ManagedBean(name = "obstetrics_visit_form")
@ViewScoped
public class ObstetricsVisitForm {
	private ObstetricsVisitController controller;
	private DataSource ds;
	private Long officeVisitID;
	private OfficeVisit officeVisit;
	private ObstetricsVisit ov;
	private Integer weeksPregnant;
	private Integer fhr;
	private Integer multiplicity;
	private Boolean placentaObserved;
	private String calendarID;
	private Part file;
	private boolean rhFlag;

	/**
	 * Default constructor for OfficeVisitForm.
	 */
	public ObstetricsVisitForm() {
		this(null, null);
	}

	/**
	 * Constructor for OfficeVisitForm for testing purposes.
	 */
	public ObstetricsVisitForm(ObstetricsVisitController ovc, DataSource ds) {
		try {
			// Use parameters if not null
			if (ds == null) {
				Context ctx = new InitialContext();
				this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
			} else {
				this.ds = ds;
			}
			controller = (ovc == null) ? new ObstetricsVisitController() : ovc;
			
			// Find the viewed office visit
			officeVisitID = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("officeVisitId");
			officeVisit = new OfficeVisitController().getVisitByID(officeVisitID.toString());
			
			// Get the existing ObstetricsVisit for this office visit if one exists
			ov = controller.getByOfficeVisit(officeVisitID);
			if (ov == null) {
				ov = new ObstetricsVisit(officeVisitID);
			}
			
			// Find the most recent ObstetricsInit record (as of the office visit date)
			ObstetricsInit oi = controller.getMostRecentOI(officeVisit);
			if (oi != null) {
				rhFlag = oi.getRH();
			}
			
			// Populate the ObstetricsVisit fields
			weeksPregnant = ov.getWeeksPregnant();
			if (weeksPregnant == null) {
				weeksPregnant = controller.calculateWeeksPregnant(officeVisit, oi);
			}
			fhr = ov.getFhr();
			multiplicity = ov.getMultiplicity();
			placentaObserved = ov.isLowLyingPlacentaObserved();
		} catch (Exception e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, "Controller Error",
					"Controller Error");
			
		}
	}
	
	/**
	 * Called when user updates obstetrics on officeVisitInfo.xhtml.
	 */
	public void submitObstetrics() {
		boolean isNew = ov.getId() == null;
		
		// Validate the OfficeVisit fields first
		OfficeVisitForm offVForm = (OfficeVisitForm) FacesContext.getCurrentInstance().getViewRoot().getViewMap().get("office_visit_form");
		boolean isValid = validateOfficeVisitFields(offVForm, isNew);
		if (!isValid) {
			return;
		}
		
		// Now update the ObstetricsVisit fields
		ov.setWeeksPregnant(weeksPregnant);
		ov.setFhr(fhr);
		ov.setMultiplicity(multiplicity);
		ov.setLowLyingPlacentaObserved(placentaObserved);
		if (isNew){
			// Try adding the ObstetricsVisit
			try {
				controller.add(ov);
			} catch (DBException e) {
				printFacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Obstetrics Visit", e.getExtendedMessage());
				return;
			} catch (FormValidationException e) {
				printFacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Obstetrics Visit", e.getMessage());
				return;
			}
			
			// Schedule the next appointment, if possible
			ApptBean nextAppointment = scheduleNextAppointment();
			if (nextAppointment != null) {
				DateFormat dateFormat = new SimpleDateFormat();
				printFacesMessage(FacesMessage.SEVERITY_INFO, "Next appointment scheduled",
						"Obstetrics Visit Successfully Updated. "
						+ "The patient's next appointment has been scheduled for " + dateFormat.format(nextAppointment.getDate()));
			}
		} else {
			controller.update(ov); //just update like normal
		}
		
		// Update the health metrics (weight and blood pressure) and reload the ObstetricsVisit
		offVForm.submitHealthMetrics();
		ov = controller.getByOfficeVisit(officeVisitID);
	}
	
	/**
	 * Returns true if the weight and blood pressure fields in the given OfficeVisitForm are populated and valid.
	 * @param offVForm
	 * @return true if populated and valid, false otherwise
	 */
	private boolean validateOfficeVisitFields(OfficeVisitForm offVForm, boolean isNew) {
		// Check that the fields are populated if new
		if (isNew) {
			if (offVForm.getWeight() == null) {
				printFacesMessage(FacesMessage.SEVERITY_ERROR, "Weight is required", "Weight is required");
				return false;
			}
			if (offVForm.getBloodPressure() == null || offVForm.getBloodPressure().isEmpty()) {
				printFacesMessage(FacesMessage.SEVERITY_ERROR, "Blood Pressure is required", "Blood Pressure is required");
				return false;
			}
		}
		
		// Now validate their format
		OfficeVisitValidator validator = new OfficeVisitValidator(ds);
		OfficeVisit bufferedOV = offVForm.getOv();
		bufferedOV.setWeight(offVForm.getWeight());
		bufferedOV.setBloodPressure(offVForm.getBloodPressure());
		try {
			validator.validate(bufferedOV);
			return true;
		} catch (FormValidationException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			return false;
		}
	}

	/**
	 * Schedule the next appointment and return the corresponding ApptBean.
	 * If the appointment cannot be automatically scheduled, returns null.
	 * @return the schedules appointment bean or null if not able to schedule
	 */
	private ApptBean scheduleNextAppointment() {
		String pid = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pid");
		Long hcpid = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("loggedInMID");
		long pidLong;
		try {
			pidLong = Long.parseLong(pid);
			int numDays = getNumDaysToNextAppointment();
			if (numDays == -1) {
				printFacesMessage(FacesMessage.SEVERITY_INFO, "The patient needs to make an appointment for a childbirth visit",
						"The patient needs to make an appointment for a childbirth visit");
				return null;
			}
			controller.logTransaction(TransactionType.SCHEDULE_NEXT_OFFICE_VISIT, officeVisit.getVisitID().toString());
			return GoogleScheduler.scheduleObstetricsAppointment(hcpid, pidLong, calendarID, numDays, officeVisit);
		} catch (NumberFormatException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, "Error loading patient information", "Error loading patient information");
			return null;
		} catch (GoogleSchedulerException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to automatically schedule next appointment",
					"Unable to automatically schedule next appointment");
			return null;
		}
	}

	/**
	 * Get the number of days until the next appointment,
	 * based on the current number of weeks pregnant.
	 * Returns -1 if the next appointment should be a childbirth visit.
	 * @return the number of days until the next appointment, or -1 if childbirth visit is next
	 */
	public int getNumDaysToNextAppointment() {
		if (weeksPregnant < 14) {
			return 28;
		}
		if (weeksPregnant < 29) {
			return 14;
		}
		if (weeksPregnant < 40) {
			return 7;
		}
		if (weeksPregnant < 42) {
			// Calculations for "Every other week day"
			Calendar cal = Calendar.getInstance();
			int weekDays = 0;
			int ret = 0;
			while (weekDays != 2) {
				cal.add(Calendar.DATE, 1);
				ret++;
				if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
					weekDays++;
				}
			}
			return ret;
		}
		return -1;
	}

	/**
	 * Called when the user clicks the button to upload a file on the Ultrasound tab of an Office Visit.
	 */
	public void upload() {
		// Check that a file has been selected
		if (file == null) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, "No file selected",
					"No file selected");
			return;
		}
		
		// Check that ObstetricsVisit has been submitted
		if (ov.getId() == null) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, "The Obstetrics tab must be saved before you can upload a file",
					"The Obstetrics tab must be saved before you can upload a file");
			return;
		}
		
		// Upload the file
		try {
			ov.setImageOfUltrasound(file.getInputStream());
			ov.setImageType(file.getSubmittedFileName());
			controller.upload(ov);
			ov = controller.getByOfficeVisit(officeVisitID);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Called when the user clicks the button to download the image file on the Ultrasound tab of an Office Visit.
	 */
	public void download() {
		// Prepare the OutputStream for download
		FacesContext fc = FacesContext.getCurrentInstance();
	    ExternalContext ec = fc.getExternalContext();

	    String filename = ov.getImageType();
	    ec.responseReset();
	    ec.setResponseContentType(ec.getMimeType(filename)); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ExternalContext#getMimeType() for auto-detection based on filename.
	    ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + filename + "\""); // The Save As popup magic is done here. You can give it any file name you want, this only won't work in MSIE, it will use current request URL as file name instead.

	    // Copy the contents of the InputStream from database to the OutputStream for download
	    try {
	    	OutputStream output = ec.getResponseOutputStream();
		    IOUtils.copy(ov.getImageOfUltrasound(), output);
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	    
	    fc.responseComplete();
	    
	    // Update ObstetricsVisit to repopulate InputStream
		ov = controller.getByOfficeVisit(officeVisitID);
	}
	
	/**
	 * Print a faces message with the given severity, summary, and details.
	 * @param severity
	 * @param summary
	 * @param detail
	 */
	public void printFacesMessage(Severity severity, String summary, String detail) {
		FacesMessage throwMsg = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, throwMsg);
	}
	
	/**
	 * Returns true if the patient needs an RH shot (assuming they haven't had one already)
	 * @return
	 */
	public boolean needsShot() {
		return rhFlag && weeksPregnant >= 28;
	}

	public Integer getWeeksPregnant() {
		return weeksPregnant;
	}

	public void setWeeksPregnant(Integer weeksPregnant) {
		this.weeksPregnant = weeksPregnant;
	}

	public Integer getFhr() {
		return fhr;
	}

	public void setFhr(Integer fhr) {
		this.fhr = fhr;
	}

	public Integer getMultiplicity() {
		return multiplicity;
	}

	public void setMultiplicity(Integer multiplicity) {
		this.multiplicity = multiplicity;
	}

	public Boolean getPlacentaObserved() {
		return placentaObserved;
	}

	public void setPlacentaObserved(Boolean placentaObserved) {
		this.placentaObserved = placentaObserved;
	}

	public String getCalendarID() {
		return calendarID;
	}

	public void setCalendarID(String calendarID) {
		this.calendarID = calendarID;
	}

	public Part getFile() {
		return file;
	}

	public void setFile(Part file) {
		this.file = file;
	}

	public ObstetricsVisit getOv() {
		return ov;
	}
}
