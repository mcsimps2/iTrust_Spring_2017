package edu.ncsu.csc.itrust.controller.obstetrics.ultrasound;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import edu.ncsu.csc.itrust.controller.officeVisit.OfficeVisitController;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

@ManagedBean(name = "ultrasound_form")
@ViewScoped
public class UltrasoundForm {
	
	private UltrasoundController controller;
	private Long officeVisitID;
	private SessionUtils sessionUtils;
	// TODO private Ultrasound ultrasound;
	
	// TODO these are temporary fields, the UI should actually reference these fields via this form's instance of Ultrasound
	private Long crl;
	private Long bpd;
	private Long hc;
	private Long fl;
	private Long ofd;
	private Long ac;
	private Long hl;
	private Long efw;

	public UltrasoundForm() {
	    this(null, SessionUtils.getInstance());
	}

	public UltrasoundForm(UltrasoundController uc, SessionUtils sessionUtils) {
	    this.sessionUtils = (sessionUtils == null) ? SessionUtils.getInstance() : sessionUtils;
		try {
			controller = (uc == null) ? new UltrasoundController() : uc;
			OfficeVisit officeVisit = new OfficeVisitController().getSelectedVisit();
			officeVisitID = officeVisit.getVisitID();
			// TODO clearFields();
		} catch (Exception e) {
			this.sessionUtils.printFacesMessage(FacesMessage.SEVERITY_ERROR, "Ultrasound Controller Error",
					"Ultrasound Controller Error", null);
		}
	}
	
	public void add(){
		/* TODO
		controller.add(ultrasound);
		clearFields();
		*/
	}
	
	public void edit(){
		/* TODO
		controller.edit(ultrasound);
		clearFields();
		*/
	}
	
	//Temporary code
	public void remove() {
		return;
	}
	/* TODO real code
	public void remove(String ultrasoundID){
		controller.remove( Long.parseLong(ultrasoundID) );
		clearFields();
	}
	*/
	
	//Temporary code
	public List<Object> getUltrasounds(){
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < 3; i++) {
			list.add(new Object());
		}
		return list;
	}
	/* TODO real code
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
	*/
	
	//Temporary code
	public void fillInput() {
		return;
	}
	/* TODO real code
	public void fillInput(String ultrasoundID, Long crl, Long bpd, Long hc, Long fl, Long ofd, Long ac, Long hl, Long efw){
		ultrasound.setId( Long.parseLong(ultrasoundID) );
		ultrasound.setCRL(crl);
		ultrasound.setBPD(bpd);
		ultrasound.setHC(hc);
		ultrasound.setFL(fl);
		ultrasound.setOFD(ofd);
		ultrasound.setAC(ac);
		ultrasound.setHL(hl);
		ultrasound.setEFW(efw);
	}
	*/
	
	/* TODO
	public void clearFields(){
		ultrasound = new Ultrasound();
		ultrasound.setOfficeVisitId(officeVisitID);
	}
	*/

	/* TODO
	public Ultrasound getUltrasound() {
		return ultrasound;
	}
	*/

	/* TODO
	public void setUltrasound(Ultrasound ultrasound) {
		this.ultrasound = ultrasound;
	}
	*/

	public Long getCrl() {
		return crl;
	}

	public void setCrl(Long crl) {
		this.crl = crl;
	}

	public Long getBpd() {
		return bpd;
	}

	public void setBpd(Long bpd) {
		this.bpd = bpd;
	}

	public Long getHc() {
		return hc;
	}

	public void setHc(Long hc) {
		this.hc = hc;
	}

	public Long getFl() {
		return fl;
	}

	public void setFl(Long fl) {
		this.fl = fl;
	}

	public Long getOfd() {
		return ofd;
	}

	public void setOfd(Long ofd) {
		this.ofd = ofd;
	}

	public Long getAc() {
		return ac;
	}

	public void setAc(Long ac) {
		this.ac = ac;
	}

	public Long getHl() {
		return hl;
	}

	public void setHl(Long hl) {
		this.hl = hl;
	}

	public Long getEfw() {
		return efw;
	}

	public void setEfw(Long efw) {
		this.efw = efw;
	}
}
