package edu.ncsu.csc.itrust.controller.obstetrics.initialization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.controller.NavigationController;
import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInit;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInitData;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInitMySQL;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.DeliveryMethod;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.PregnancyInfo;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.PregnancyInfoData;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.PregnancyInfoMySQL;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.PregnancyInfoValidator;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.beans.PersonnelBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

/**
 * Handles a lot of the logic and communication between the view and the model for Obstetrics Initialization.
 * 
 * @author amcheshi
 */
@ManagedBean(name = "obstetrics_init_controller")
@SessionScoped
public class ObstetricsInitController extends iTrustController
{
	/** Constant for the message to be displayed when a patient is made eligible for obstetric care */
	private static final String PATIENT_MADE_ELIGIBLE = " is now eligible for obstetric care";
	/** Error message when patient data cannot be found */
	private static final String ERROR_LOADING_PATIENT = "Error loading patient data";
	/** Error message when hcp data cannot be found */
	private static final String ERROR_LOADING_HCP = "Error loading HCP data";
	/** Error message when getting pregnancy data fails */
	private static final String ERROR_LOADING_PREGNANCIES = "Error loading pregnancy data.";
	/** Error message when viewing record fails */
	private static final String ERROR_VIEWING_RECORD = "Error viewing record";
	/** Error message when viewing the obstetrics overview fails */
	private static final String ERROR_VIEWING_OVERVIEW = "Error viewing obstetrics overview";
	/** Error message when adding invalid pregancy info */
	private static final String ERROR_ADDING_PREGNANCY = "Error when adding prior pregnancy";
	/** Error when non integers are input to pregnancy info */
	private static final String ERROR_ADDING_PREGNANCY_INT_REQUIRED = "Error when adding prior pregnancy: integers are required in every field";
	/** String for an OB/GYN specialist */
	private static final String OBGYN = "OB/GYN";
	
	/** Grants access to the obstetrics initializations database */
	ObstetricsInitData oiData;
	/** Grants access to the pregnancy data database */
	PregnancyInfoData pregnancyData;
	/** Used to obtain session variables and request parameters */
	SessionUtils sessionUtils;
	/** The most recently viewed ObstetricsInit record */
	private ObstetricsInit viewedOI;
	/** PregnancyInfoValidator used for validating on add, not just on save */
	private PregnancyInfoValidator pregnancyInfoValidator;
	
	/** List of pregnancy records to display, including those retrieved from
	 * the database and those added by the user */
	private List<PregnancyInfo> displayedPregnancies = new ArrayList<PregnancyInfo>();
	
	/** List of pregnancy records added by the user */
	private List<PregnancyInfo> addedPregnancies = new ArrayList<PregnancyInfo>();
	
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
	
	/**
	 * Default constructor.
	 */
	public ObstetricsInitController() {
		super();
		sessionUtils = SessionUtils.getInstance();
		try {
			oiData = new ObstetricsInitMySQL();
			pregnancyData = new PregnancyInfoMySQL();
			pregnancyInfoValidator = new PregnancyInfoValidator();
		} catch (DBException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Constructor injection, intended only for unit testing purposes.
	 * 
	 * @param ds
	 *            The injected DataSource dependency
	 */
	public ObstetricsInitController(DataSource ds) {
		super();
		sessionUtils = SessionUtils.getInstance();
		oiData = new ObstetricsInitMySQL(ds);
		pregnancyData = new PregnancyInfoMySQL(ds);
		pregnancyInfoValidator = new PregnancyInfoValidator(ds);
	}
	
	/**
	 * Returns true if the patient with the given pid is eligible for obstetric care.
	 * Precondition: the given pid is valid
	 * @param pid MID of a patient
	 * @return true if eligible, false otherwise
	 */
	public boolean isPatientEligible(String pid) {
		// Parse the String
		long pidLong;
		try {
			pidLong = Long.parseLong(pid);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_PATIENT, e.getMessage(), null);
			return false;
		}
		
		// Check patient eligibility
		PatientDAO dao = new PatientDAO(DAOFactory.getProductionInstance());
		try {
			PatientBean p = dao.getPatient(pidLong);
			if (p == null) return false;
			return p.getObstetricsCareEligibility();
		} catch (DBException e) {
			e.printStackTrace();
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_PATIENT, e.getMessage(), null);
			return false;
		}
	}
	
	/**
	 * Makes the patient with the given pid eligible for obstetric care.
	 * @param pid MID of a patient
	 * @param hcpid MID of the HCP that is performing this action
	 */
	public void makePatientEligible(String pid, String hcpid) {
		// Parse the Strings
		long pidLong;
		long hcpidLong;
		try {
			pidLong = Long.parseLong(pid);
			hcpidLong = Long.parseLong(hcpid);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_PATIENT, e.getMessage(), null);
			return;
		}
		
		// Get the patient bean from the database
		PatientDAO dao = new PatientDAO(DAOFactory.getProductionInstance());
		PatientBean patient;
		try {
			patient = dao.getPatient(pidLong);
		} catch (DBException e) {
			e.printStackTrace();
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_PATIENT, e.getMessage(), null);
			return;
		}
		
		if (patient == null) return;
		
		String message = String.format("%s %s %s", patient.getFirstName(), patient.getLastName(), PATIENT_MADE_ELIGIBLE);
		printFacesMessage(FacesMessage.SEVERITY_INFO, message, message, null);
		
		// Change eligibility and update database
		patient.setObstetricsCareEligibility(true);
		try {
			dao.editPatient(patient, hcpidLong);
		} catch (DBException e) {
			e.printStackTrace();
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_PATIENT, e.getMessage(), null);
		}
	}
	
	/**
	 * Returns a list of all of the existing Obstetrics Initialization Records for the
	 * patient with the given pid, in descending order by date.
	 * @param pid MID of a patient
	 * @return list of initialization records in descending order by date
	 */
	public List<ObstetricsInit> getRecords(String pid) {
		// Parse the String
		long pidLong;
		try {
			pidLong = Long.parseLong(pid);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_PATIENT, e.getMessage(), null);
			return null;
		}
		
		// Get records from database
		List<ObstetricsInit> list;
		try {
			list = oiData.getRecords(pidLong);
		} catch (DBException e) {
			e.printStackTrace();
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_PATIENT, e.getMessage(), null);
			return null;
		}
		
		// Sort and return list
		list.sort(null);
		return list;
	}

	private List<PregnancyInfo> getPastPregnancies() {
		try {
			return this.pregnancyData.getRecords(sessionUtils.getCurrentPatientMIDLong());
		} catch (DBException e) {
			e.printStackTrace();
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_PREGNANCIES, e.getMessage(), null);
			return null;
		}
	}
	
	public List<PregnancyInfo> getPastPregnanciesFromInit(int oid) {
		try {
			return this.pregnancyData.getRecordsFromInit(oid);
		} catch (DBException e) {
			e.printStackTrace();
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_PREGNANCIES, e.getMessage(), null);
			return null;
		}
	}
	
	/**
	 * Returns whether or not the HCP with the given hcpid has a specialization of OB/GYN.
	 * @param hcpid the MID of an HCP
	 * @return true if OB/GYN specialist, false otherwise
	 */
	public boolean isOBGYN(String hcpid) {
		// Parse the String
		long hcpidLong;
		try {
			hcpidLong = Long.parseLong(hcpid);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_HCP, e.getMessage(), null);
			return false;
		}
		
		// Get the personnel bean from the database
		PersonnelDAO dao = new PersonnelDAO(DAOFactory.getProductionInstance());
		PersonnelBean personnel;
		try {
			personnel = dao.getPersonnel(hcpidLong);
		} catch (DBException e) {
			e.printStackTrace();
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_HCP, e.getMessage(), null);
			return false;
		}
		
		// Check specialty
		if (personnel == null) return false;
		if (personnel.getSpecialty() == null) return false;
		return personnel.getSpecialty().equals(OBGYN);
	}

	public ObstetricsInit getViewedOI() {
		return viewedOI;
	}

	/**
	 * Navigates to the viewObstetricsRecord page to view the given record and logs the view.
	 * If adding a new record, oi should be null.
	 * @param oi
	 * @param hcpid the MID of the HCP viewing the record
	 */
	public void viewAddObstetricsInit(ObstetricsInit oi, String hcpid) {
		// Parse the String
		long hcpidLong;
		try {
			hcpidLong = Long.parseLong(hcpid);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_HCP, e.getMessage(), null);
			return;
		}

		// We're loading the page, so clear everything.
		clearPregnancyFields();
		clearPregnancyLists();
		clearLMP();
		
		if (oi != null) {
			// We're viewing a record, so show past pregnancy records from it.
			this.displayedPregnancies = getPastPregnanciesFromInit(oi.getID());
		} else {
			// We're adding a record, so show all past pregnancies.
			this.displayedPregnancies = getPastPregnancies();
		}
		
		// Set the record and log the view if we're viewing
		this.viewedOI = oi;
		if (oi != null) {
			TransactionLogger.getInstance().logTransaction(TransactionType.VIEW_INITIAL_OBSTETRIC_RECORD, hcpidLong, oi.getPid(), oi.getEDD());
		}
		
		// Navigate to the view/add page
		try {
			NavigationController.viewAddObstetricsRecord();
		} catch (IOException e) {
			e.printStackTrace();
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_VIEWING_RECORD, e.getMessage(), null);
		}
	}
	
	public List<DeliveryMethod> getDeliveryMethods() {
		return Arrays.asList(DeliveryMethod.values());
	}
	
	public List<PregnancyInfo> getDisplayedPregnancies() {
		return displayedPregnancies;
	}

	public void setDisplayedPregnancies(List<PregnancyInfo> displayedPregnancies) {
		this.displayedPregnancies = displayedPregnancies;
	}

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
		// Make a new pregnancy record
		PregnancyInfo newPregnancy;
		try {
			newPregnancy = new PregnancyInfo(
				Integer.parseInt(sessionUtils.getCurrentPatientMID()),
				Integer.parseInt(yearOfConception),
				Integer.parseInt(numWeeksPregnant) * 7,
				Integer.parseInt(numHoursInLabor),
				Integer.parseInt(weightGain),
				DeliveryMethod.matchString(deliveryType),
				Integer.parseInt(multiplicity)
			);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_ADDING_PREGNANCY, ERROR_ADDING_PREGNANCY_INT_REQUIRED, null);
			return;
		}
		
		//Validate the pregnancyFields
		try {
			pregnancyInfoValidator.validate(newPregnancy, false);
		} catch (FormValidationException e) {
			e.printStackTrace();
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_ADDING_PREGNANCY, e.getMessage(), null);
			return;
		}
			
		// Add the new pregnancy record to both lists
		this.addedPregnancies.add(newPregnancy);
		this.displayedPregnancies.add(newPregnancy);
		
		// Sort the displayed list
		Collections.sort(displayedPregnancies,
				(o1, o2) -> o2.getYearOfConception() - o1.getYearOfConception());
		
		// Clear fields except LMP
		clearPregnancyFields();
	}
	
	public void addObstetricsRecord() {
		// Make a new ObstetricsInit record with the LMP and save it in the database
		// Log the record
		// Go through all pregnancy records in addedPregnancies and set the OID from
		//                                                    the previous operation
		// Add all pregnancy records from addedPregnancies
		// Clear both lists and the temporary LMP
		// Redirect to the overview page with the navigation controller
	}
	
	public void cancelAddObstetricsRecord() {
		// Clear pregnancy lists (we're not going to submit them) and pregnancy fields
		clearPregnancyFields();
		clearPregnancyLists();
		clearLMP();
		
		// Go back to the overview page
		try {
			NavigationController.viewObstetricsOverview();
		} catch (IOException e) {
			e.printStackTrace();
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_VIEWING_OVERVIEW, e.getMessage(), null);
		}
	}
	
	private void clearPregnancyFields() {
		this.setYearOfConception("");
		this.setNumWeeksPregnant("");
		this.setNumHoursInLabor("");
		this.setWeightGain("");
		// We won't bother to clear the delivery type because it's a dropdown.
		this.setMultiplicity("");
	}
	
	private void clearPregnancyLists() {
		this.addedPregnancies.clear();
		this.displayedPregnancies.clear();
	}
	
	private void clearLMP() {
		this.setLmp("");
	}
}
