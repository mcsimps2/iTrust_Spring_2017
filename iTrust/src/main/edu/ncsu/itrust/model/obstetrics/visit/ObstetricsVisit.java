package edu.ncsu.itrust.model.obstetrics.visit;

import java.sql.Blob;

/**
 * Model of info for obstetrics visit
 * @author jcgonzal
 *
 */
public class ObstetricsVisit {

	private long id;
	
	private long patientMID;
	
	private long obstetricsInitID;
	
	private long officeVisitID;
	
	private int fhr;
	
	private int multiplicity;
	
	private boolean lowLyingPlacentaObserved;
	
	private Blob imageOfUltrasound;
	
	public ObstetricsVisit(long id, long patientMID, long officeVisitID, long obstetricsInitID,
			int fhr, int multiplicity, boolean lowLyingPlacentaObserved, Blob imageOfUltraSound) {
		super();
		this.id = id;
		this.patientMID = patientMID;
		this.officeVisitID = officeVisitID;
		this.obstetricsInitID = obstetricsInitID;
		this.fhr = fhr;
		this.multiplicity = multiplicity;
		this.lowLyingPlacentaObserved = lowLyingPlacentaObserved;
		this.imageOfUltrasound = imageOfUltraSound;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the patientMID
	 */
	public long getPatientMID() {
		return patientMID;
	}

	/**
	 * @param patientMID the patientMID to set
	 */
	public void setPatientMID(long patientMID) {
		this.patientMID = patientMID;
	}

	/**
	 * @return the obstetricsInitID
	 */
	public long getObstetricsInitID() {
		return obstetricsInitID;
	}

	/**
	 * @param obstetricsInitID the obstetricsInitID to set
	 */
	public void setObstetricsInitID(long obstetricsInitID) {
		this.obstetricsInitID = obstetricsInitID;
	}

	/**
	 * @return the officeVisitID
	 */
	public long getOfficeVisitID() {
		return officeVisitID;
	}

	/**
	 * @param officeVisitID the officeVisitID to set
	 */
	public void setOfficeVisitID(long officeVisitID) {
		this.officeVisitID = officeVisitID;
	}

	/**
	 * @return the fhr
	 */
	public int getFhr() {
		return fhr;
	}

	/**
	 * @param fhr the fhr to set
	 */
	public void setFhr(int fhr) {
		this.fhr = fhr;
	}

	/**
	 * @return the multiplicity
	 */
	public int getMultiplicity() {
		return multiplicity;
	}

	/**
	 * @param multiplicity the multiplicity to set
	 */
	public void setMultiplicity(int multiplicity) {
		this.multiplicity = multiplicity;
	}

	/**
	 * @return the lowLyingPlacentaObserved
	 */
	public boolean isLowLyingPlacentaObserved() {
		return lowLyingPlacentaObserved;
	}

	/**
	 * @param lowLyingPlacentaObserved the lowLyingPlacentaObserved to set
	 */
	public void setLowLyingPlacentaObserved(boolean lowLyingPlacentaObserved) {
		this.lowLyingPlacentaObserved = lowLyingPlacentaObserved;
	}

	/**
	 * @return the imageOfUltrasound
	 */
	public Blob getImageOfUltrasound() {
		return imageOfUltrasound;
	}

	/**
	 * @param imageOfUltrasound the imageOfUltrasound to set
	 */
	public void setImageOfUltrasound(Blob imageOfUltrasound) {
		this.imageOfUltrasound = imageOfUltrasound;
	}
}
