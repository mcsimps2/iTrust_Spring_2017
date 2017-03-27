package edu.ncsu.csc.itrust.controller.obstetrics.officeVisit;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import edu.ncsu.csc.itrust.controller.officeVisit.OfficeVisitController;
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
		boolean isNew = ov.getWeeksPregnant() == null;
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
}
