package edu.ncsu.csc.itrust.model.obstetrics.visit;

import java.io.InputStream;

/**
 * Model of info for obstetrics visit
 * @author jcgonzal
 */
public class ObstetricsVisit {

	/** Unique id */
	private Long id;
	
	/** ID to link to OfficeVisit object */
	private Long officeVisitID;
	
	/** Number of weeks pregnant at visit */
	private Integer weeksPregnant;
	
	/** Fetal heart rate */
	private Integer fhr;
	
	/** Number of fetuses in the pregnancy */
	private Integer multiplicity;
	
	/** Whether or not a low lying placenta was observed */
	private Boolean lowLyingPlacentaObserved;
	
	/** Data for the image of the ultrasound */
	private InputStream imageOfUltrasound;
	
	/** Type of the image file (.png, .jpeg, .pdf, etc.) */
	private String imageType;
	
	/**
	 * Constructor for getting out of the db
	 * @param id
	 * @param officeVisitID
	 * @param weeksPregnant
	 * @param daysPregnant
	 * @param fhr
	 * @param multiplicity
	 * @param lowLyingPlacentaObserved
	 * @param imageOfUltraSound
	 */
	public ObstetricsVisit(Long id, Long officeVisitID, Integer weeksPregnant, Integer fhr,
			Integer multiplicity, Boolean lowLyingPlacentaObserved, InputStream imageOfUltraSound, String imageType) {
		super();
		this.id = id;
		this.officeVisitID = officeVisitID;
		this.weeksPregnant = weeksPregnant;
		this.fhr = fhr;
		this.multiplicity = multiplicity;
		this.lowLyingPlacentaObserved = lowLyingPlacentaObserved;
		this.imageOfUltrasound = imageOfUltraSound;
		this.imageType = imageType;
	}
	
	/**
	 * Constructor for passing into the db
	 * @param officeVisitID
	 * @param weeksPregnant
	 * @param fhr
	 * @param multiplicity
	 * @param lowLyingPlacentaObserved
	 * @param imageOfUltraSound
	 */
	public ObstetricsVisit(Long officeVisitID, Integer weeksPregnant, Integer fhr,
			Integer multiplicity, Boolean lowLyingPlacentaObserved, InputStream imageOfUltraSound, String imageType) {
		super();
		this.officeVisitID = officeVisitID;
		this.weeksPregnant = weeksPregnant;
		this.fhr = fhr;
		this.multiplicity = multiplicity;
		this.lowLyingPlacentaObserved = lowLyingPlacentaObserved;
		this.imageOfUltrasound = imageOfUltraSound;
		this.imageType = imageType;
	}

	/**
	 * Constructor for creating a blank obstetrics visit with just an office visit ID
	 * @param officeVisitID
	 */
	public ObstetricsVisit(Long officeVisitID) {
		super();
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
	 * @return the weeksPregnant
	 */
	public Integer getWeeksPregnant() {
		return weeksPregnant;
	}

	/**
	 * @param weeksPregnant the weeksPregnant to set
	 */
	public void setWeeksPregnant(Integer weeksPregnant) {
		this.weeksPregnant = weeksPregnant;
	}

	/**
	 * @return the fhr
	 */
	public Integer getFhr() {
		return fhr;
	}

	/**
	 * @param fhr the fhr to set
	 */
	public void setFhr(Integer fhr) {
		this.fhr = fhr;
	}

	/**
	 * @return the multiplicity
	 */
	public Integer getMultiplicity() {
		return multiplicity;
	}

	/**
	 * @param multiplicity the multiplicity to set
	 */
	public void setMultiplicity(Integer multiplicity) {
		this.multiplicity = multiplicity;
	}

	/**
	 * @return the lowLyingPlacentaObserved
	 */
	public Boolean isLowLyingPlacentaObserved() {
		return lowLyingPlacentaObserved;
	}

	/**
	 * @param lowLyingPlacentaObserved the lowLyingPlacentaObserved to set
	 */
	public void setLowLyingPlacentaObserved(Boolean lowLyingPlacentaObserved) {
		this.lowLyingPlacentaObserved = lowLyingPlacentaObserved;
	}

	/**
	 * @return the imageOfUltrasound
	 */
	public InputStream getImageOfUltrasound() {
		return imageOfUltrasound;
	}

	/**
	 * @param imageOfUltrasound the imageOfUltrasound to set
	 */
	public void setImageOfUltrasound(InputStream imageOfUltrasound) {
		this.imageOfUltrasound = imageOfUltrasound;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 * Don't compare image because doesn't implement hashcode
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fhr == null) ? 0 : fhr.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((imageType == null) ? 0 : imageType.hashCode());
		result = prime * result + ((lowLyingPlacentaObserved == null) ? 0 : lowLyingPlacentaObserved.hashCode());
		result = prime * result + ((multiplicity == null) ? 0 : multiplicity.hashCode());
		result = prime * result + ((officeVisitID == null) ? 0 : officeVisitID.hashCode());
		result = prime * result + ((weeksPregnant == null) ? 0 : weeksPregnant.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 * Don't compare image because doesn't implement equals
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ObstetricsVisit other = (ObstetricsVisit) obj;
		if (fhr == null) {
			if (other.fhr != null)
				return false;
		} else if (!fhr.equals(other.fhr))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (imageType == null) {
			if (other.imageType != null)
				return false;
		} else if (!imageType.equals(other.imageType))
			return false;
		if (lowLyingPlacentaObserved == null) {
			if (other.lowLyingPlacentaObserved != null)
				return false;
		} else if (!lowLyingPlacentaObserved.equals(other.lowLyingPlacentaObserved))
			return false;
		if (multiplicity == null) {
			if (other.multiplicity != null)
				return false;
		} else if (!multiplicity.equals(other.multiplicity))
			return false;
		if (officeVisitID == null) {
			if (other.officeVisitID != null)
				return false;
		} else if (!officeVisitID.equals(other.officeVisitID))
			return false;
		if (weeksPregnant == null) {
			if (other.weeksPregnant != null)
				return false;
		} else if (!weeksPregnant.equals(other.weeksPregnant))
			return false;
		return true;
	}
	
	
}
