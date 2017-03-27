package edu.ncsu.csc.itrust.controller.obstetrics.officeVisit;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "obstetrics_visit_form")
@ViewScoped
public class ObstetricsVisitForm {
	private ObstetricsVisitController controller;
	private Long officeVisitID;
	//private ObstetricsVisit ov; TODO
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
		// Temporary code
		weeksPregnant = 1;
		fhr = 2;
		multiplicity = 3;
		placentaObserved = true;
		
		/* Real code TODO
		try {
			controller = (ovc == null) ? new ObstetricsVisitController() : ovc;
			OfficeVisit officeVisit = new OfficeVisitController().getSelectedVisit();
			officeVisitID = officeVisit.getVisitID();
			ov = controller.getVisitWithID(officeVisitID);
			if (ov == null) {
				ov = new ObstetricsVisit();
				ov.setOfficeVisitID(officeVisitID);
			}
			weeksPregnant = ov.getWeeksPregnant();
			if (weeksPregnant == null) {
				weeksPregnant = controller.calculateWeeksPregnant(officeVisit);
			}
			fhr = ov.getFHR();
			multiplicity = ov.getMultiplicity();
			placentaObserved = ov.getPlacentaObserved();
		} catch (Exception e) {
			FacesMessage throwMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Controller Error",
					"Controller Error");
			FacesContext.getCurrentInstance().addMessage(null, throwMsg);
		}
		*/
	}
	
	/**
	 * Called when user updates obstetrics on officeVisitInfo.xhtml.
	 */
	public void submitObstetrics() {
		// Temporary code
		return;
		
		/* Real code TODO
		boolean isNew = ov.getWeeksPregnant() == null;
		ov.setWeeksPregnant(weeksPregnant);
		ov.setFHR(fhr);
		ov.setMultiplicity(multiplicity);
		ov.setPlacentaObserved(placentaObserved);
		controller.edit(ov, isNew);
		if (isNew){
		    controller.logTransaction(TransactionType.CREATE_OBSTETRIC_OFFICE_VISIT, "Office Visit ID: " + ov.getOfficeVisitID().toString());
		} else {
		    controller.logTransaction(TransactionType.EDIT_OBSTETRIC_OFFICE_VISIT, "Office Visit ID: " + ov.getOfficeVisitID().toString());
		}
		*/
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
