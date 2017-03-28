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
}
