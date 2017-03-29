package edu.ncsu.csc.itrust.controller.obstetrics.ultrasound;

import java.util.Collections;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import edu.ncsu.csc.itrust.controller.obstetrics.officeVisit.ObstetricsVisitController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.obstetrics.ultrasound.Ultrasound;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

@ManagedBean(name = "ultrasound_form")
@ViewScoped
public class UltrasoundForm {
	
	private UltrasoundController controller;
	private ObstetricsVisitController ovc;
	private Long officeVisitID;
	private SessionUtils sessionUtils;
	private Ultrasound ultrasound;

	/**
	 * Constructor used in run time.
	 */
	public UltrasoundForm() {
	    this(null, null, SessionUtils.getInstance());
	}

	/**
	 * Constructor used for testing. Can pass mocked parameters to unit test.
	 * @param uc
	 * @param ovc
	 * @param sessionUtils
	 */
	public UltrasoundForm(UltrasoundController uc, ObstetricsVisitController ovc, SessionUtils sessionUtils) {
		try {
			this.sessionUtils = (sessionUtils == null) ? SessionUtils.getInstance() : sessionUtils;
		    this.ovc = (ovc == null) ? new ObstetricsVisitController() : ovc;
			controller = (uc == null) ? new UltrasoundController() : uc;
			officeVisitID = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("officeVisitId");
			clearFields();
		} catch (Exception e) {
			this.sessionUtils.printFacesMessage(FacesMessage.SEVERITY_ERROR, "Controller Error",
					"Controller Error", null);
		}
	}
	
	/**
	 * Adds the ultrasound to the database.
	 * Only works if an ObstetricsVisit has been submitted for this office visit.
	 */
	public void add(){
		if (ovc.getByOfficeVisit(officeVisitID) == null) {
			sessionUtils.printFacesMessage(FacesMessage.SEVERITY_ERROR, "The Obstetrics tab must be saved before you can add an ultrasound",
					"The Obstetrics tab must be saved before you can add an ultrasound", null);
			return;
		}
		controller.add(ultrasound);
		clearFields();
	}
	
	/**
	 * Edits the current ultrasound in the database.
	 */
	public void edit(){
		controller.edit(ultrasound);
		clearFields();
	}
	
	/**
	 * Deletes the ultrasound in the database with the given ID.
	 * @param ultrasoundID
	 */
	public void delete(Long ultrasoundID){
		controller.delete(ultrasoundID);
		clearFields();
	}
	
	/**
	 * Returns a List of all of the Ultrasounds for the current office visit.
	 * @return all of the ultrasounds for the current office visit
	 */
	public List<Ultrasound> getUltrasounds(){
		List<Ultrasound> ultrasounds = Collections.emptyList();
		try {
			ultrasounds = controller.getUltrasoundsByOfficeVisit(officeVisitID);
		} catch (DBException e) {
			sessionUtils.printFacesMessage(FacesMessage.SEVERITY_ERROR, "Ultrasound Controller Error", "Ultrasound Controller Error",
					null);
		}
		return ultrasounds;
	}
	
	/**
	 * Fills the fields in this form with the given parameters.
	 * Used when editing an existing ultrasound on the page.
	 * @param ultrasoundID
	 * @param crl
	 * @param bpd
	 * @param hc
	 * @param fl
	 * @param ofd
	 * @param ac
	 * @param hl
	 * @param efw
	 */
	public void fillInput(Long ultrasoundID, Float crl, Float bpd, Float hc, Float fl, Float ofd, Float ac, Float hl, Float efw){
		ultrasound.setId(ultrasoundID);
		ultrasound.setCrl(crl);
		ultrasound.setBpd(bpd);
		ultrasound.setHc(hc);
		ultrasound.setFl(fl);
		ultrasound.setOfd(ofd);
		ultrasound.setAc(ac);
		ultrasound.setHl(hl);
		ultrasound.setEfw(efw);
	}
	
	/**
	 * Clears all of the fields in this form.
	 */
	public void clearFields(){
		ultrasound = new Ultrasound(officeVisitID);
	}

	public Ultrasound getUltrasound() {
		return ultrasound;
	}

	public void setUltrasound(Ultrasound ultrasound) {
		this.ultrasound = ultrasound;
	}
}
