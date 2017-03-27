package edu.ncsu.csc.itrust.controller.obstetrics.ultrasound;

import java.util.Collections;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.obstetrics.ultrasound.Ultrasound;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

@ManagedBean(name = "ultrasound_form")
@ViewScoped
public class UltrasoundForm {
	
	private UltrasoundController controller;
	private Long officeVisitID;
	private SessionUtils sessionUtils;
	private Ultrasound ultrasound;

	public UltrasoundForm() {
	    this(null, SessionUtils.getInstance());
	}

	public UltrasoundForm(UltrasoundController uc, SessionUtils sessionUtils) {
	    this.sessionUtils = (sessionUtils == null) ? SessionUtils.getInstance() : sessionUtils;
		try {
			controller = (uc == null) ? new UltrasoundController() : uc;
			officeVisitID = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("officeVisitId");
			clearFields();
		} catch (Exception e) {
			this.sessionUtils.printFacesMessage(FacesMessage.SEVERITY_ERROR, "Ultrasound Controller Error",
					"Ultrasound Controller Error", null);
		}
	}
	
	public void add(){
		controller.add(ultrasound);
		clearFields();
	}
	
	public void edit(){
		controller.edit(ultrasound);
		clearFields();
	}
	
	public void delete(Long ultrasoundID){
		controller.delete(ultrasoundID);
		clearFields();
	}
	
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
