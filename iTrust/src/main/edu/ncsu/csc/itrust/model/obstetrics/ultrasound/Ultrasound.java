package edu.ncsu.csc.itrust.model.obstetrics.ultrasound;

/**
 * Pojo for ultrasound object
 * @author jcgonzal
 */
public class Ultrasound {

	/** Unique id */
	Long id;
	
	/** id to link to officevisit object */
	Long officeVisitID;
	
	/** Crown rump length */
	Float crl;
	
	/** Biparietal diameter */
	Float bpd;
	
	/** Head circumferece */
	Float hc;
	
	/** Femur length */
	Float fl;
	
	/** Occipitofrontal diameter */
	Float ofd;
	
	/** Abdominal circumference */
	Float ac;
	
	/** Humerus length */
	Float hl;
	
	/** Estimated fetal weight */
	Float efw;
	
	/**
	 * Constructor for the ultrasound pojo
	 * @param id
	 * @param officeVisitID
	 * @param crl
	 * @param bpd
	 * @param hc
	 * @param fl
	 * @param ofd
	 * @param ac
	 * @param hl
	 * @param efw
	 */
	public Ultrasound(Long id, Long officeVisitID, Float crl, Float bpd, Float hc,
			Float fl, Float ofd, Float ac, Float hl, Float efw) {
		this.id = id;
		this.officeVisitID = officeVisitID;
		this.crl = crl;
		this.bpd = bpd;
		this.hc = hc;
		this.fl = fl;
		this.ofd = ofd;
		this.ac = ac;
		this.hl = hl;
		this.efw = efw;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the officeVisitID
	 */
	public Long getOfficeVisitID() {
		return officeVisitID;
	}

	/**
	 * @param officeVisitID the officeVisitID to set
	 */
	public void setOfficeVisitID(Long officeVisitID) {
		this.officeVisitID = officeVisitID;
	}

	/**
	 * @return the crl
	 */
	public Float getCrl() {
		return crl;
	}

	/**
	 * @param crl the crl to set
	 */
	public void setCrl(Float crl) {
		this.crl = crl;
	}

	/**
	 * @return the bpd
	 */
	public Float getBpd() {
		return bpd;
	}

	/**
	 * @param bpd the bpd to set
	 */
	public void setBpd(Float bpd) {
		this.bpd = bpd;
	}

	/**
	 * @return the hc
	 */
	public Float getHc() {
		return hc;
	}

	/**
	 * @param hc the hc to set
	 */
	public void setHc(Float hc) {
		this.hc = hc;
	}

	/**
	 * @return the fl
	 */
	public Float getFl() {
		return fl;
	}

	/**
	 * @param fl the fl to set
	 */
	public void setFl(Float fl) {
		this.fl = fl;
	}

	/**
	 * @return the ofd
	 */
	public Float getOfd() {
		return ofd;
	}

	/**
	 * @param ofd the ofd to set
	 */
	public void setOfd(Float ofd) {
		this.ofd = ofd;
	}

	/**
	 * @return the ac
	 */
	public Float getAc() {
		return ac;
	}

	/**
	 * @param ac the ac to set
	 */
	public void setAc(Float ac) {
		this.ac = ac;
	}

	/**
	 * @return the hl
	 */
	public Float getHl() {
		return hl;
	}

	/**
	 * @param hl the hl to set
	 */
	public void setHl(Float hl) {
		this.hl = hl;
	}

	/**
	 * @return the efw
	 */
	public Float getEfw() {
		return efw;
	}

	/**
	 * @param efw the efw to set
	 */
	public void setEfw(Float efw) {
		this.efw = efw;
	}
	
}
