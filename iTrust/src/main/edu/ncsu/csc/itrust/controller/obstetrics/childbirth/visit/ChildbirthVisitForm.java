package edu.ncsu.csc.itrust.controller.obstetrics.childbirth.visit;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.controller.obstetrics.visit.ObstetricsVisitController;
import edu.ncsu.csc.itrust.controller.officeVisit.OfficeVisitController;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit.ChildbirthVisit;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInit;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.DeliveryMethod;
import edu.ncsu.csc.itrust.model.obstetrics.visit.ObstetricsVisit;
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
	private int pitocin;
	private int nitrousOxide;
	private int pethidine;
	private int epiduralAnaesthesia;
	private int magnesiumSulfate;
	
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
			this.cv = controller.getByOfficeVisit(officeVisitID);
			if (this.cv == null) {
				this.cv = new ChildbirthVisit();
				this.cv.setOfficeVisitID(officeVisitID);
			}
			
			// Populate the ObstetricsVisit fields
			this.deliveryType = cv.getDeliveryType();
			this.pitocin = cv.getPitocin();
			this.nitrousOxide = cv.getNitrousOxide();
			this.pethidine = cv.getPethidine();
			this.epiduralAnaesthesia = cv.getEpiduralAnaesthesia();
			this.magnesiumSulfate = cv.getMagnesiumSulfate();
		} catch (Exception e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, "Controller Error", "Controller Error");
		}
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
	public int getPitocin() {
		return pitocin;
	}
	public void setPitocin(int pitocin) {
		this.pitocin = pitocin;
	}
	public int getNitrousOxide() {
		return nitrousOxide;
	}
	public void setNitrousOxide(int nitrousOxide) {
		this.nitrousOxide = nitrousOxide;
	}
	public int getPethidine() {
		return pethidine;
	}
	public void setPethidine(int pethidine) {
		this.pethidine = pethidine;
	}
	public int getEpiduralAnaesthesia() {
		return epiduralAnaesthesia;
	}
	public void setEpiduralAnaesthesia(int epiduralAnaesthesia) {
		this.epiduralAnaesthesia = epiduralAnaesthesia;
	}
	public int getMagnesiumSulfate() {
		return magnesiumSulfate;
	}
	public void setMagnesiumSulfate(int magnesiumSulfate) {
		this.magnesiumSulfate = magnesiumSulfate;
	}
}