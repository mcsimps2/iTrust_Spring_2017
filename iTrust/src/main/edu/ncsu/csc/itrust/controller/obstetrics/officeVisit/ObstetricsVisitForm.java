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
			if (ds == null) {
				Context ctx = new InitialContext();
				this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
			} else {
				this.ds = ds;
			}
			controller = (ovc == null) ? new ObstetricsVisitController() : ovc;
			officeVisitID = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("officeVisitId");
			officeVisit = new OfficeVisitController().getVisitByID(officeVisitID.toString());
			ov = controller.getByOfficeVisit(officeVisitID);
			if (ov == null) {
				ov = new ObstetricsVisit(officeVisitID);
			}
			
			ObstetricsInit oi = controller.getMostRecentOI(officeVisit);
			if (oi != null) {
				rhFlag = oi.getRH();
			}
			
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
		if (isNew) {
			boolean isValid = validateOfficeVisitFields(offVForm);
			if (!isValid) {
				return;
			}
		}
		
		// Now update the ObstetricsVisit fields
		ov.setWeeksPregnant(weeksPregnant);
		ov.setFhr(fhr);
		ov.setMultiplicity(multiplicity);
		ov.setLowLyingPlacentaObserved(placentaObserved);
		if (isNew){
			try {
				controller.add(ov);
			} catch (DBException e) {
				System.out.println("DBException");//TODO
				printFacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Obstetrics Visit", e.getExtendedMessage());
				return;
			} catch (FormValidationException e) {
				System.out.println("FormValidationException");//TODO
				printFacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Obstetrics Visit", e.getLocalizedMessage()); //TODO try getLocalizedMessage
				return;
			}
			ov = controller.getByOfficeVisit(officeVisitID);
			ApptBean nextAppointment = scheduleNextAppointment();
			if (nextAppointment != null) {
				DateFormat dateFormat = new SimpleDateFormat();
				printFacesMessage(FacesMessage.SEVERITY_INFO, "Next appointment scheduled",
						"Obstetrics Visit Successfully Updated. "
						+ "The patient's next appointment has been scheduled for " + dateFormat.format(nextAppointment.getDate()));
			}
		} else {
			controller.update(ov);
		}
		offVForm.submitHealthMetrics();
	}
	
	private boolean validateOfficeVisitFields(OfficeVisitForm offVForm) {
		if (offVForm.getWeight() == null) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, "Weight is required", "Weight is required");
			return false;
		}
		if (offVForm.getBloodPressure() == null || offVForm.getBloodPressure().isEmpty()) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, "Blood Pressure is required", "Blood Pressure is required");
			return false;
		}
		
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
		if (file == null) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, "No file selected",
					"No file selected");
			return;
		}
		
		if (ov.getId() == null) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, "The Obstetrics tab must be saved before you can upload a file",
					"The Obstetrics tab must be saved before you can upload a file");
			return;
		}
		
		try {
			ov.setImageOfUltrasound(file.getInputStream());
			ov.setImageType(file.getSubmittedFileName());
			controller.upload(ov);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Called when the user clicks the button to download the image file on the Ultrasound tab of an Office Visit.
	 */
	public void download() {
		ov = controller.getByOfficeVisit(officeVisitID);

		FacesContext fc = FacesContext.getCurrentInstance();
	    ExternalContext ec = fc.getExternalContext();

	    String filename = ov.getImageType();
	    ec.responseReset();
	    ec.setResponseContentType(ec.getMimeType(filename)); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ExternalContext#getMimeType() for auto-detection based on filename.
	    ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + filename + "\""); // The Save As popup magic is done here. You can give it any file name you want, this only won't work in MSIE, it will use current request URL as file name instead.

	    try {
	    	OutputStream output = ec.getResponseOutputStream();
		    IOUtils.copy(ov.getImageOfUltrasound(), output);
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	    
	    fc.responseComplete();
	}
	
	public void printFacesMessage(Severity severity, String summary, String detail) {
		FacesMessage throwMsg = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, throwMsg);
	}
	
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
