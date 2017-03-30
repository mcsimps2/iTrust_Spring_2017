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
	 * Constructor for getting an ultrasound out of the db
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
	 * Constructor for passing into the db
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
	public Ultrasound(Long officeVisitID, Float crl, Float bpd, Float hc,
			Float fl, Float ofd, Float ac, Float hl, Float efw) {
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
	 * Constructor for creating a blank Ultrasound with just an officeVisitID
	 * @param officeVisitID2
	 */
	public Ultrasound(Long officeVisitID) {
		this.officeVisitID = officeVisitID;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ac == null) ? 0 : ac.hashCode());
		result = prime * result + ((bpd == null) ? 0 : bpd.hashCode());
		result = prime * result + ((crl == null) ? 0 : crl.hashCode());
		result = prime * result + ((efw == null) ? 0 : efw.hashCode());
		result = prime * result + ((fl == null) ? 0 : fl.hashCode());
		result = prime * result + ((hc == null) ? 0 : hc.hashCode());
		result = prime * result + ((hl == null) ? 0 : hl.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((ofd == null) ? 0 : ofd.hashCode());
		result = prime * result + ((officeVisitID == null) ? 0 : officeVisitID.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ultrasound other = (Ultrasound) obj;
		if (ac == null) {
			if (other.ac != null)
				return false;
		} else if (!ac.equals(other.ac))
			return false;
		if (bpd == null) {
			if (other.bpd != null)
				return false;
		} else if (!bpd.equals(other.bpd))
			return false;
		if (crl == null) {
			if (other.crl != null)
				return false;
		} else if (!crl.equals(other.crl))
			return false;
		if (efw == null) {
			if (other.efw != null)
				return false;
		} else if (!efw.equals(other.efw))
			return false;
		if (fl == null) {
			if (other.fl != null)
				return false;
		} else if (!fl.equals(other.fl))
			return false;
		if (hc == null) {
			if (other.hc != null)
				return false;
		} else if (!hc.equals(other.hc))
			return false;
		if (hl == null) {
			if (other.hl != null)
				return false;
		} else if (!hl.equals(other.hl))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (ofd == null) {
			if (other.ofd != null)
				return false;
		} else if (!ofd.equals(other.ofd))
			return false;
		if (officeVisitID == null) {
			if (other.officeVisitID != null)
				return false;
		} else if (!officeVisitID.equals(other.officeVisitID))
			return false;
		return true;
	}
	
	
	
}
