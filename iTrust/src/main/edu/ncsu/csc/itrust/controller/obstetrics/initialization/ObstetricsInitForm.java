package edu.ncsu.csc.itrust.controller.obstetrics.initialization;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.PregnancyInfo;

@ManagedBean(name = "obstetrics_init_form")
@ViewScoped
public class ObstetricsInitForm {
	/** List of pregnancy records to display, including those retrieved from
	 * the database and those added by the user */
	private List<PregnancyInfo> displayedPregnancies;
	
	/** List of pregnancy records added by the user */
	private List<PregnancyInfo> addedPregnancies;
	
	/** Temporary storage of the LMP for when the user is adding prior pregnancies */
	private String lmp;
	
	/** Year of conception for pregnancy record */
	private String yearOfConception;
	
	/** Number of weeks pregnant for pregnancy record */
	private String numWeeksPregnant;
	
	/** Number of hours in labor for pregnancy record */
	private String numHoursInLabor;
	
	/** Weight gain for pregnancy record */
	private String weightGain;
	
	/** Delivery type for pregnancy record */
	private String deliveryType;
	
	/** Multiplicity for pregnancy record */
	private String multiplicity;
	
	public String getLmp() {
		return lmp;
	}

	public void setLmp(String lmp) {
		this.lmp = lmp;
	}

	public String getYearOfConception() {
		return yearOfConception;
	}

	public void setYearOfConception(String yearOfConception) {
		this.yearOfConception = yearOfConception;
	}

	public String getNumWeeksPregnant() {
		return numWeeksPregnant;
	}

	public void setNumWeeksPregnant(String numWeeksPregnant) {
		this.numWeeksPregnant = numWeeksPregnant;
	}

	public String getNumHoursInLabor() {
		return numHoursInLabor;
	}

	public void setNumHoursInLabor(String numHoursInLabor) {
		this.numHoursInLabor = numHoursInLabor;
	}

	public String getWeightGain() {
		return weightGain;
	}

	public void setWeightGain(String weightGain) {
		this.weightGain = weightGain;
	}

	public String getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	public String getMultiplicity() {
		return multiplicity;
	}

	public void setMultiplicity(String multiplicity) {
		this.multiplicity = multiplicity;
	}

	public void addPregnancyRecord() {
		// Validate all fields except lists and lmp
		// Add the new pregnancy to both lists
	}
	
	public void addObstetricsRecord() {
		// Make a new ObstetricsInit record with the LMP and save it in the database
		// Add all pregnancy records from addedPregnancies to the database using
		//                                   the OID from the previous operation
		// Clear both lists and the temporary LMP
		// Redirect to the overview page with the navigation controller
	}
}