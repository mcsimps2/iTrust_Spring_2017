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
	private ObstetricsVisitController obsVController;
	private OfficeVisitController offVController;
	private Long officeVisitID;
	private ObstetricsVisit obstetricsVisit;
	private OfficeVisit officeVisit;
	private Integer weeksPregnant;
	private Float weight;
	private String bloodPressure;
	private Integer fhr;
	private Integer multiplicity;
	private Boolean placentaObserved;

	/**
	 * Default constructor for OfficeVisitForm.
	 */
	public ObstetricsVisitForm() {
		this(null, null);
	}

	/**
	 * Constructor for OfficeVisitForm for testing purposes.
	 */
	public ObstetricsVisitForm(ObstetricsVisitController obsVC, OfficeVisitController offVC) {
		try {
			obsVController = (obsVC == null) ? new ObstetricsVisitController() : obsVC;
			offVController = (offVC == null) ? new OfficeVisitController() : offVC;
			officeVisitID = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("officeVisitId");
			officeVisit = offVController.getVisitByID(officeVisitID.toString());
			weight = officeVisit.getWeight();
			bloodPressure = officeVisit.getBloodPressure();
			obstetricsVisit = obsVController.getByOfficeVisit(officeVisitID);
			if (obstetricsVisit == null) {
				obstetricsVisit = new ObstetricsVisit(officeVisitID);
			}
			weeksPregnant = obstetricsVisit.getWeeksPregnant();
			if (weeksPregnant == null) {
				weeksPregnant = obsVController.calculateWeeksPregnant(officeVisit);
			}
			fhr = obstetricsVisit.getFhr();
			multiplicity = obstetricsVisit.getMultiplicity();
			placentaObserved = obstetricsVisit.isLowLyingPlacentaObserved();
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
		boolean isNew = obstetricsVisit.getWeeksPregnant() == null;

		if (isNew && (weight == null || bloodPressure == null || bloodPressure.isEmpty())) {
			FacesMessage throwMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "All fields are required",
					"All fields are required");
			FacesContext.getCurrentInstance().addMessage(null, throwMsg);
			return;
		}
		
		officeVisit = offVController.getVisitByID(officeVisitID.toString());
		officeVisit.setWeight(weight);
		officeVisit.setBloodPressure(bloodPressure);
		offVController.edit(officeVisit);
		
		obstetricsVisit.setWeeksPregnant(weeksPregnant);
		obstetricsVisit.setFhr(fhr);
		obstetricsVisit.setMultiplicity(multiplicity);
		obstetricsVisit.setLowLyingPlacentaObserved(placentaObserved);
		if (isNew){
			obsVController.add(obstetricsVisit);
			obstetricsVisit = obsVController.getByOfficeVisit(officeVisitID);
			// TODO probably add some stuff here regarding the scheduling of the next appointment
		} else {
			obsVController.update(obstetricsVisit);
		}
	}

	public Integer getWeeksPregnant() {
		return weeksPregnant;
	}

	public void setWeeksPregnant(Integer weeksPregnant) {
		this.weeksPregnant = weeksPregnant;
	}

	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public String getBloodPressure() {
		return bloodPressure;
	}

	public void setBloodPressure(String bloodPressure) {
		this.bloodPressure = bloodPressure;
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
