package edu.ncsu.csc.itrust.controller.obstetrics.childbirth.visit;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit.ChildbirthVisit;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit.VisitType;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.DeliveryMethod;

@ManagedBean(name = "childbirth_visit_form")
@ViewScoped
public class ChildbirthVisitForm {
	private ChildbirthVisitController controller;

	private Long officeVisitID;
	private ChildbirthVisit cv;
	
	private DeliveryMethod deliveryType;
	private VisitType visitType;
	private Integer pitocin;
	private Integer nitrousOxide;
	private Integer pethidine;
	private Integer epiduralAnaesthesia;
	private Integer magnesiumSulfate;
	private Integer rh;
	
	public ChildbirthVisitForm() {
		this(null, null, (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("officeVisitId"));
	}
	
	public ChildbirthVisitForm(ChildbirthVisitController controller, DataSource ds, Long officeVisitID) {
		try {
			this.controller = (controller == null) ? new ChildbirthVisitController() : controller;
			
			// Find the viewed office visit
			this.officeVisitID = officeVisitID;
			
			// Get the existing ChildbirthVisit for this office visit if one exists
			this.cv = this.controller.getByOfficeVisit(officeVisitID);
			if (this.cv == null) {
				this.cv = new ChildbirthVisit();
				this.cv.setOfficeVisitID(officeVisitID);
			}
			
			// Populate the ObstetricsVisit fields
			this.deliveryType = this.cv.getDeliveryType();
			this.visitType = this.cv.getVisitType();
			this.pitocin = this.cv.getPitocin();
			this.nitrousOxide = this.cv.getNitrousOxide();
			this.pethidine = this.cv.getPethidine();
			this.epiduralAnaesthesia = this.cv.getEpiduralAnaesthesia();
			this.magnesiumSulfate = this.cv.getMagnesiumSulfate();
			this.rh = this.cv.getRH();
		} catch (DBException ex) {
			this.controller.printFacesMessage(FacesMessage.SEVERITY_ERROR, "Controller Error", "Controller Error", null);
			ex.printStackTrace();
		}
	}
	
	public void submitChildbirth() {
		boolean isNew = cv.getId() == null;
		
		cv.setDeliveryType(this.deliveryType);
		cv.setVisitType(this.visitType);
		cv.setPitocin(this.pitocin);
		cv.setNitrousOxide(this.nitrousOxide);
		cv.setPethidine(this.pethidine);
		cv.setEpiduralAnaesthesia(this.epiduralAnaesthesia);
		cv.setMagnesiumSulfate(this.magnesiumSulfate);
		cv.setRH(this.rh);
		
		if (isNew) {
			controller.add(cv);
		} else {
			controller.update(cv);
		}
		
		ChildbirthVisit dbCV = this.controller.getByOfficeVisit(officeVisitID);
		if (dbCV != null) cv = dbCV;
	}

	public DeliveryMethod getDeliveryType() {
		return deliveryType;
	}
	public void setDeliveryType(DeliveryMethod deliveryType) {
		this.deliveryType = deliveryType;
	}
	public VisitType getVisitType() {
		return visitType;
	}
	public void setVisitType(VisitType visitType) {
		this.visitType = visitType;
	}
	public Integer getPitocin() {
		return pitocin;
	}
	public void setPitocin(Integer pitocin) {
		this.pitocin = pitocin;
	}
	public Integer getNitrousOxide() {
		return nitrousOxide;
	}
	public void setNitrousOxide(Integer nitrousOxide) {
		this.nitrousOxide = nitrousOxide;
	}
	public Integer getPethidine() {
		return pethidine;
	}
	public void setPethidine(Integer pethidine) {
		this.pethidine = pethidine;
	}
	public Integer getEpiduralAnaesthesia() {
		return epiduralAnaesthesia;
	}
	public void setEpiduralAnaesthesia(Integer epiduralAnaesthesia) {
		this.epiduralAnaesthesia = epiduralAnaesthesia;
	}
	public Integer getMagnesiumSulfate() {
		return magnesiumSulfate;
	}
	public void setMagnesiumSulfate(Integer magnesiumSulfate) {
		this.magnesiumSulfate = magnesiumSulfate;
	}

	public Integer getRh() {
		return rh;
	}

	public void setRh(Integer rh) {
		this.rh = rh;
	}
}