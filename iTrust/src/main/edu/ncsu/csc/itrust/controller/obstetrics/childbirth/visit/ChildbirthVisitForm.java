package edu.ncsu.csc.itrust.controller.obstetrics.childbirth.visit;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.controller.officeVisit.OfficeVisitController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit.ChildbirthVisit;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.DeliveryMethod;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;

@ManagedBean(name = "childbirth_visit_form")
@ViewScoped
public class ChildbirthVisitForm {
	private ChildbirthVisitController controller;
	private DataSource ds;

	private Long officeVisitID;
	private OfficeVisit officeVisit;
	private ChildbirthVisit cv;
	
	private DeliveryMethod deliveryType;
	private Integer pitocin;
	private Integer nitrousOxide;
	private Integer pethidine;
	private Integer epiduralAnaesthesia;
	private Integer magnesiumSulfate;
	
	public ChildbirthVisitForm() {
		this(null, null);
	}
	
	public ChildbirthVisitForm(ChildbirthVisitController controller, DataSource ds) {
		try {
			if (ds == null) {
				Context ctx = new InitialContext();
				this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
			} else {
				this.ds = ds;
			}
			this.controller = (controller == null) ? new ChildbirthVisitController() : controller;
			
			// Find the viewed office visit
			officeVisitID = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("officeVisitId");
			officeVisit = new OfficeVisitController().getVisitByID(officeVisitID.toString());
			
			// Get the existing ChildbirthVisit for this office visit if one exists
			this.cv = this.controller.getByOfficeVisit(officeVisitID);
			if (this.cv == null) {
				this.cv = new ChildbirthVisit();
				this.cv.setOfficeVisitID(officeVisitID);
			}
			
			// Populate the ObstetricsVisit fields
			this.deliveryType = this.cv.getDeliveryType();
			this.pitocin = this.cv.getPitocin();
			this.nitrousOxide = this.cv.getNitrousOxide();
			this.pethidine = this.cv.getPethidine();
			this.epiduralAnaesthesia = this.cv.getEpiduralAnaesthesia();
			this.magnesiumSulfate = this.cv.getMagnesiumSulfate();
		} catch (NamingException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, "Controller Error", "Controller Error");
		} catch (DBException ex) {
			// 48, 52
		}
	}
	
	public void submitChildbirth() {
		boolean isNew = cv.getId() == null;
		
		// TODO Validate fields
		
		cv.setDeliveryType(this.deliveryType);
		cv.setPitocin(this.pitocin);
		cv.setNitrousOxide(this.nitrousOxide);
		cv.setPethidine(this.pethidine);
		cv.setEpiduralAnaesthesia(this.epiduralAnaesthesia);
		cv.setMagnesiumSulfate(this.magnesiumSulfate);
		
		if (isNew) {
			controller.add(cv);
		} else {
			controller.update(cv);
		}
		
		ChildbirthVisit dbCV = this.controller.getByOfficeVisit(officeVisitID);
		if (dbCV != null)
			cv = dbCV;
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
	
	public DeliveryMethod getDeliveryType() {
		return deliveryType;
	}
	public void setDeliveryType(DeliveryMethod deliveryType) {
		this.deliveryType = deliveryType;
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
}