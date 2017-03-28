package edu.ncsu.csc.itrust.controller.obstetrics.officeVisit;

import java.io.IOException;
import java.io.OutputStream;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;

import edu.ncsu.csc.itrust.controller.officeVisit.OfficeVisitController;
import edu.ncsu.csc.itrust.controller.officeVisit.OfficeVisitForm;
import edu.ncsu.csc.itrust.model.obstetrics.visit.ObstetricsVisit;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;

@ManagedBean(name = "obstetrics_visit_form")
@ViewScoped
public class ObstetricsVisitForm {
	private ObstetricsVisitController controller;
	private Long officeVisitID;
	private ObstetricsVisit ov;
	private Integer weeksPregnant;
	private Integer fhr;
	private Integer multiplicity;
	private Boolean placentaObserved;
	private String calendarID;
	private Part file;

	/**
	 * Default constructor for OfficeVisitForm.
	 */
	public ObstetricsVisitForm() {
		this(null);
	}

	/**
	 * Constructor for OfficeVisitForm for testing purposes.
	 */
	public ObstetricsVisitForm(ObstetricsVisitController ovc) {
		try {
			controller = (ovc == null) ? new ObstetricsVisitController() : ovc;
			officeVisitID = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("officeVisitId");
			OfficeVisit officeVisit = new OfficeVisitController().getVisitByID(officeVisitID.toString());
			ov = controller.getByOfficeVisit(officeVisitID);
			if (ov == null) {
				ov = new ObstetricsVisit(officeVisitID);
			}
			weeksPregnant = ov.getWeeksPregnant();
			if (weeksPregnant == null) {
				weeksPregnant = controller.calculateWeeksPregnant(officeVisit);
			}
			fhr = ov.getFhr();
			multiplicity = ov.getMultiplicity();
			placentaObserved = ov.isLowLyingPlacentaObserved();
		} catch (Exception e) {
			FacesMessage throwMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Controller Error",
					"Controller Error");
			FacesContext.getCurrentInstance().addMessage(null, throwMsg);
		}
	}
	
	/**
	 * Called when user updates obstetrics on officeVisitInfo.xhtml.
	 */
	public void submitObstetrics() {
		boolean isNew = ov.getId() == null;
		
		// Update the OfficeVisit fields first
		OfficeVisitForm offVForm = (OfficeVisitForm) FacesContext.getCurrentInstance().getViewRoot().getViewMap().get("office_visit_form");
		if (isNew && (offVForm.getWeight() == null || offVForm.getBloodPressure() == null || offVForm.getBloodPressure().isEmpty())) {
			FacesMessage throwMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "All fields are required",
					"All fields are required");
			FacesContext.getCurrentInstance().addMessage(null, throwMsg);
			return;
		}
		offVForm.submitHealthMetrics();
				
		// Now update the ObstetricsVisit fields
		ov.setWeeksPregnant(weeksPregnant);
		ov.setFhr(fhr);
		ov.setMultiplicity(multiplicity);
		ov.setLowLyingPlacentaObserved(placentaObserved);
		if (isNew){
			controller.add(ov);
			ov = controller.getByOfficeVisit(officeVisitID);
			
			// TODO probably add some stuff here regarding the scheduling of the next appointment
			
		} else {
			controller.update(ov);
		}
	}
	
	/**
	 * Called when the user clicks the button to upload a file on the Ultrasound tab of an Office Visit.
	 */
	public void upload() {
		if (file == null) {
			return;
		}
		
		if (ov.getId() == null) {
			FacesMessage throwMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "The Obstetrics tab must be saved before you can upload a file",
					"The Obstetrics tab must be saved before you can upload a file");
			FacesContext.getCurrentInstance().addMessage(null, throwMsg);
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
