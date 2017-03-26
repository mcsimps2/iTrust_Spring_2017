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
	Integer crl;
	
	/** Biparietal diameter */
	Integer bpd;
	
	/** Head circumferece */
	Integer hc;
	
	/** Femur length */
	Integer fl;
	
	/** Occipitofrontal diameter */
	Integer ofd;
	
	/** Abdominal circumference */
	Integer ac;
	
	/** Humerus length */
	Integer hl;
	
	/** Estimated fetal weight */
	Integer efw;
	
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
	public Ultrasound(Long id, Long officeVisitID, Integer crl, Integer bpd, Integer hc,
			Integer fl, Integer ofd, Integer ac, Integer hl, Integer efw) {
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
	public Integer getCrl() {
		return crl;
	}

	/**
	 * @param crl the crl to set
	 */
	public void setCrl(Integer crl) {
		this.crl = crl;
	}

	/**
	 * @return the bpd
	 */
	public Integer getBpd() {
		return bpd;
	}

	/**
	 * @param bpd the bpd to set
	 */
	public void setBpd(Integer bpd) {
		this.bpd = bpd;
	}

	/**
	 * @return the hc
	 */
	public Integer getHc() {
		return hc;
	}

	/**
	 * @param hc the hc to set
	 */
	public void setHc(Integer hc) {
		this.hc = hc;
	}

	/**
	 * @return the fl
	 */
	public Integer getFl() {
		return fl;
	}

	/**
	 * @param fl the fl to set
	 */
	public void setFl(Integer fl) {
		this.fl = fl;
	}

	/**
	 * @return the ofd
	 */
	public Integer getOfd() {
		return ofd;
	}

	/**
	 * @param ofd the ofd to set
	 */
	public void setOfd(Integer ofd) {
		this.ofd = ofd;
	}

	/**
	 * @return the ac
	 */
	public Integer getAc() {
		return ac;
	}

	/**
	 * @param ac the ac to set
	 */
	public void setAc(Integer ac) {
		this.ac = ac;
	}

	/**
	 * @return the hl
	 */
	public Integer getHl() {
		return hl;
	}

	/**
	 * @param hl the hl to set
	 */
	public void setHl(Integer hl) {
		this.hl = hl;
	}

	/**
	 * @return the efw
	 */
	public Integer getEfw() {
		return efw;
	}

	/**
	 * @param efw the efw to set
	 */
	public void setEfw(Integer efw) {
		this.efw = efw;
	}
	
}
