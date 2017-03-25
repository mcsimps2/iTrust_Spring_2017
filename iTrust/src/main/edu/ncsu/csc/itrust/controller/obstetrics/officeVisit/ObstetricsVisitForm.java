package edu.ncsu.csc.itrust.controller.obstetrics.officeVisit;

import java.time.LocalDateTime;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.ncsu.csc.itrust.controller.officeVisit.OfficeVisitController;
import edu.ncsu.csc.itrust.model.ValidationFormat;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

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
		
		/* Real code
		try {
			controller = (ovc == null) ? new ObstetricsVisitController() : ovc;
			OfficeVisit officeVisit = new OfficeVisitController().getSelectedVisit();
			officeVisitID = officeVisit.getVisitID();
			ov = controller.getVisitWithID(officeVisitID);
			if (ov == null) {
				ov = new ObstetricsVisit();
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
	
	// TODO old cold here

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
