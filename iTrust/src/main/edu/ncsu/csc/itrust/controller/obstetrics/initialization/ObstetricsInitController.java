package edu.ncsu.csc.itrust.controller.obstetrics.initialization;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;

import edu.ncsu.csc.itrust.controller.NavigationController;
import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit.VisitType;
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
	private static final String PATIENT_MADE_ELIGIBLE = " is now eligible for obstetric care.";
	private static final String ERROR_LOADING_PATIENT = "Error loading patient data.";
	private static final String ERROR_LOADING_HCP = "Error loading HCP data.";
	private static final String ERROR_LOADING_PREGNANCIES = "Error loading pregnancy data.";
	private static final String ERROR_VIEWING_RECORD = "Error viewing record.";
	private static final String ERROR_VIEWING_OVERVIEW = "Error viewing obstetrics overview.";
	private static final String ERROR_ADDING_PREGNANCY = "Error when adding prior pregnancy.";
	private static final String ERROR_ADDING_PREGNANCY_INT_REQUIRED = "Error when adding prior pregnancy: integers are required in every field (weight gain can take a decimal value)";
	private static final String ERROR_ADDING_RECORD = "Error adding the obstetrics initialization record.";
	private static final String ERROR_LMP_FORMAT = "Error: please format the LMP as YYYY-MM-DD.";
	private static final String ERROR_REQUIRED_LMP = "Error: the LMP field is required.";
	private static final String ERROR_ADD_PREGNANCY_FIRST = "Error: there was unsubmitted information in the pregnancy form. Please finish adding it or remove it.";
	private static final String OBGYN = "OB/GYN";
	private static final String SUCCESS_ADD_OBSTETRICS = "The obstetrics record was added successfully.";
	
	/** Grants access to the obstetrics initializations database */
	ObstetricsInitData oiData;
	/** Grants access to the pregnancy data database */
	PregnancyInfoData pregnancyData;
	/** Used to obtain session variables and request parameters */
	SessionUtils sessionUtils;
	/** Patient DAO used to access patient eligibility */
	PatientDAO patientDAO;
	/** Personnel DAO used to access hcp type */
	private PersonnelDAO personnelDAO;
	/** The most recently viewed ObstetricsInit record */
	private ObstetricsInit viewedOI;
	/** PregnancyInfoValidator used for validating on add, not just on save */
	private PregnancyInfoValidator pregnancyInfoValidator;
	
	/** List of pregnancy records to display, including those retrieved from
	 * the database and those added by the user */
	private List<PregnancyInfo> displayedPregnancies = new ArrayList<PregnancyInfo>();
	
	/** List of pregnancy records added by the user */
	private List<PregnancyInfo> addedPregnancies = new ArrayList<PregnancyInfo>();
	
	/** Temporary storage of RH flag */
	private boolean RH;
	
	/** Temporary storage of geneticPotentialForMiscarriage flag */
	private boolean geneticPotentialForMiscarriage;
	
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
		patientDAO = new PatientDAO(DAOFactory.getProductionInstance());
		personnelDAO = new PersonnelDAO(DAOFactory.getProductionInstance());
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
		patientDAO = new PatientDAO(DAOFactory.getProductionInstance());
		personnelDAO = new PersonnelDAO(DAOFactory.getProductionInstance());
		oiData = new ObstetricsInitMySQL(ds);
		pregnancyData = new PregnancyInfoMySQL(ds);
		pregnancyInfoValidator = new PregnancyInfoValidator(ds);
	}
	
	/**
	 * For testing purposes
	 * 
	 * @param ds
	 *            DataSource
	 * @param sessionUtils
	 *            SessionUtils instance
	 */
	public ObstetricsInitController(DataSource ds, SessionUtils sessionUtils, PatientDAO patientDAO, PersonnelDAO personnelDAO) {
		super();
		this.sessionUtils = sessionUtils;
		this.patientDAO = patientDAO;
		this.personnelDAO = personnelDAO;
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
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_PATIENT, e.getMessage(), null);
			return false;
		}
		
		// Check patient eligibility
		try {
			PatientBean p = patientDAO.getPatient(pidLong);
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
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_PATIENT, e.getMessage(), null);
			return;
		}
		
		// Get the patient bean from the database
		PatientBean patient;
		try {
			patient = patientDAO.getPatient(pidLong);
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
			patientDAO.editPatient(patient, hcpidLong);
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

	/**
	 * Get the past pregnancies for the patient in session utils
	 * @return list of past pregnancies
	 */
	public List<PregnancyInfo> getPastPregnancies() {
		try {
			return this.pregnancyData.getRecords(sessionUtils.getCurrentPatientMIDLong());
		} catch (DBException e) {
			e.printStackTrace();
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_PREGNANCIES, e.getMessage(), null);
			return null;
		}
	}
	
	/**
	 * Get the past pregnancies for the patient in session utils
	 * @param oid obstetrics init id
	 * @return list of past pregnancies
	 */
	public List<PregnancyInfo> getPastPregnanciesFromInit(long oid) {
		try {
			List<PregnancyInfo> pregnancies = this.pregnancyData.getRecordsFromInit(oid);
			Collections.sort(pregnancies,
					(o1, o2) -> o2.getYearOfConception() - o1.getYearOfConception());
			return pregnancies;
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
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_HCP, e.getMessage(), null);
			return false;
		}
		
		// Get the personnel bean from the database
		PersonnelBean personnel;
		try {
			personnel = personnelDAO.getPersonnel(hcpidLong);
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

	/**
	 * Get viewed OI. Used for whether it is view or add.
	 * @return the OI
	 */
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
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_HCP, e.getMessage(), null);
			return;
		}

		// We're loading the page, so clear everything.
		clearPregnancyFields();
		clearPregnancyLists();
		clearLMP();
		clearRH();
		clearGeneticPotentialForMiscarriage();
		
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
			navigateToViewAdd();
		} catch (IOException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_VIEWING_RECORD, e.getMessage(), null);
		}
	}
	
	/**
	 * Navigates the the add/view obstetrics init record page.
	 * @throws IOException
	 */
	public void navigateToViewAdd() throws IOException {
		NavigationController.viewAddObstetricsRecord();
	}

	/**
	 * Gets the delivery method options for the dropdown
	 * @return the delivery method options
	 */
	public List<DeliveryMethod> getDeliveryMethods() {
		return Arrays.asList(DeliveryMethod.values());
	}

	/**
	 * Gets the visit type options for the dropdown
	 * @return the visit type options
	 */
	public List<VisitType> getVisitTypes() {
		return Arrays.asList(VisitType.values());
	}
	
	/**
	 * Add pregnancy record based on the fields
	 * @return success
	 */
	public void addPregnancyRecord() {
		
		if (StringUtils.isEmpty(yearOfConception) &&
				StringUtils.isEmpty(numWeeksPregnant) &&
				StringUtils.isEmpty(numHoursInLabor) &&
				StringUtils.isEmpty(weightGain) &&
				StringUtils.isEmpty(multiplicity))
			return;
		
		// Make a new pregnancy record
		PregnancyInfo newPregnancy;
		try {
			newPregnancy = new PregnancyInfo(
				Integer.parseInt(sessionUtils.getCurrentPatientMID()),
				Integer.parseInt(yearOfConception),
				Integer.parseInt(numWeeksPregnant) * 7,
				Integer.parseInt(numHoursInLabor),
				Double.parseDouble(weightGain),
				DeliveryMethod.matchString(deliveryType),
				Integer.parseInt(multiplicity)
			);
		} catch (NumberFormatException e) {
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
		
		// Clear fields except LMP
		clearPregnancyFields();
		return;
	}
	
	/**
	 * Add obstetrics record based on the fields
	 */
	public void addObstetricsRecord() {
		// if (they're all empty) or (they're all filled)
		if (!StringUtils.isEmpty(yearOfConception)
		 || !StringUtils.isEmpty(numWeeksPregnant)
		 || !StringUtils.isEmpty(numHoursInLabor)
		 || !StringUtils.isEmpty(weightGain)
		 || !StringUtils.isEmpty(multiplicity))
		{
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_ADD_PREGNANCY_FIRST, ERROR_ADD_PREGNANCY_FIRST, null);
			return;
		}
			
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String today = dateFormat.format(date);
		
		long pid = sessionUtils.getCurrentPatientMIDLong();
		
		// Validate the date
		if (this.getLmp() == null || this.getLmp().isEmpty()) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_REQUIRED_LMP, ERROR_REQUIRED_LMP, null);
			return;
		} else if (!ObstetricsInit.verifyDate(this.getLmp())) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LMP_FORMAT, ERROR_LMP_FORMAT, null);
			return;
		}
		
		// Make a new ObstetricsInit record with the LMP and save it in the database
		ObstetricsInit oi = new ObstetricsInit(pid, today, this.getLmp());
		oi.setRH(this.getRH());
		oi.setGeneticPotentialForMiscarriage(this.getGeneticPotentialForMiscarriage());
		try {
			long oid = oiData.addAndReturnID(oi);
			
			TransactionLogger.getInstance().logTransaction(
				TransactionType.CREATE_INITIAL_OBSTETRIC_RECORD,
				sessionUtils.getSessionLoggedInMIDLong(),
				Long.parseLong(sessionUtils.getSessionPID()),
				oi.getEDD()
			);
			
			// Go through all pregnancy records in addedPregnancies
			for (PregnancyInfo pregnancy : this.addedPregnancies) {
				// set the OID
				pregnancy.setObstetricsInitID(oid);
				// and add it to the database
				pregnancyData.add(pregnancy);
			}
			
			// Clear both lists and the temporary LMP
			this.clearPregnancyFields();
			this.clearPregnancyLists();
			this.clearLMP();
			this.clearRH();
			this.clearGeneticPotentialForMiscarriage();
			
			// Add success messages
			printFacesMessage(FacesMessage.SEVERITY_INFO, SUCCESS_ADD_OBSTETRICS, SUCCESS_ADD_OBSTETRICS, null);
			
			// Redirect to the overview page with the navigation controller
			redirectToObstetricsRecordsOverview();
		} catch (DBException e) {
			e.printStackTrace();
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_ADDING_RECORD, e.getMessage(), null);
		} catch (IOException e) {
			e.printStackTrace();
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_VIEWING_OVERVIEW, e.getMessage(), null);
		} catch (FormValidationException e) {
			e.printStackTrace();
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_ADDING_RECORD, e.getMessage(), null);
		}
	}
	
	/**
	 * Navigates to the obstetrics init records overview page.
	 * @throws IOException
	 */
	public void redirectToObstetricsRecordsOverview() throws IOException {
		NavigationController.viewObstetricsOverview();
	}

	public void cancelAddObstetricsRecord() {
		// Clear pregnancy lists (we're not going to submit them) and pregnancy fields
		clearPregnancyFields();
		clearPregnancyLists();
		clearLMP();
		clearRH();
		clearGeneticPotentialForMiscarriage();
		
		// Go back to the overview page
		try {
			redirectToObstetricsRecordsOverview();
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
	
	private void clearRH() {
		this.setRH(false);
	}
	
	private void clearGeneticPotentialForMiscarriage() {
		this.setGeneticPotentialForMiscarriage(false);
	}
	
	/**
	 * Sends a FacesMessage for FacesContext to display.
	 * 
	 * @param severity
	 *            severity of the message
	 * @param summary
	 *            localized summary message text
	 * @param detail
	 *            localized detail message text
	 * @param clientId
	 *            The client identifier with which this message is associated
	 *            (if any)
	 */
	@Override
	public void printFacesMessage(Severity severity, String summary, String detail, String clientId) {
		FacesContext ctx = FacesContext.getCurrentInstance();
		if (ctx == null) {
			return;
		}
		ctx.getExternalContext().getFlash().setKeepMessages(true);
		ctx.addMessage(clientId, new FacesMessage(severity, summary, detail));
	}
	
	/**
	 * Get the displayed pregnancies
	 * @return
	 */
	public List<PregnancyInfo> getDisplayedPregnancies() {
		Collections.sort(displayedPregnancies,
				(o1, o2) -> o2.getYearOfConception() - o1.getYearOfConception());
		
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
	
	 public boolean getRH() {
		 return RH;
	 }
	 	
	 public void setRH(boolean val) {
	 	this.RH = val;
	 }
	 
	 public boolean getGeneticPotentialForMiscarriage()
	 {
		 return geneticPotentialForMiscarriage;
	 }
	 
	 public void setGeneticPotentialForMiscarriage(boolean val)
	 {
		 this.geneticPotentialForMiscarriage = val;
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
}