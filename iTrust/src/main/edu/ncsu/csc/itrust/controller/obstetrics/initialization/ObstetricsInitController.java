package edu.ncsu.csc.itrust.controller.obstetrics.initialization;

import java.io.IOException;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.controller.NavigationController;
import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInit;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInitData;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInitMySQL;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
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
	
	/** Grants access to the database */
	ObstetricsInitData oiData;
	/** Used to obtain session variables and request parameters */
	SessionUtils sessionUtils;
	/** The most recently viewed ObstetricsInit record */
	private ObstetricsInit viewedOI;
	
	
	/**
	 * Default constructor.
	 */
	public ObstetricsInitController() {
		super();
		sessionUtils = SessionUtils.getInstance();
		try {
			oiData = new ObstetricsInitMySQL();
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
			printFacesMessage(FacesMessage.SEVERITY_ERROR, "NumberFormatException", e.getMessage(), null);
			return false;
		}
		
		// Check patient eligibility
		PatientDAO dao = new PatientDAO(DAOFactory.getProductionInstance());
		try {
			return dao.getPatient(pidLong).getObstetricsCareEligibility();
		} catch (DBException e) {
			e.printStackTrace();
			printFacesMessage(FacesMessage.SEVERITY_ERROR, "DBException", e.getMessage(), null);
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
			printFacesMessage(FacesMessage.SEVERITY_ERROR, "NumberFormatException", e.getMessage(), null);
			return;
		}
		
		// Get the patient bean from the database
		PatientDAO dao = new PatientDAO(DAOFactory.getProductionInstance());
		PatientBean patient;
		try {
			patient = dao.getPatient(pidLong);
		} catch (DBException e) {
			e.printStackTrace();
			printFacesMessage(FacesMessage.SEVERITY_ERROR, "DBException", e.getMessage(), null);
			return;
		}
		
		String message = String.format("%s %s %s", patient.getFirstName(), patient.getLastName(), PATIENT_MADE_ELIGIBLE);
		printFacesMessage(FacesMessage.SEVERITY_INFO, message, message, null);
		
		// Change eligibility and update database
		patient.setObstetricsCareEligibility(true);
		try {
			dao.editPatient(patient, hcpidLong);
		} catch (DBException e) {
			e.printStackTrace();
			printFacesMessage(FacesMessage.SEVERITY_ERROR, "DBException", e.getMessage(), null);
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
			printFacesMessage(FacesMessage.SEVERITY_ERROR, "NumberFormatException", e.getMessage(), null);
			return null;
		}
		
		// Get records from database
		List<ObstetricsInit> list;
		try {
			list = oiData.getRecords(pidLong);
		} catch (DBException e) {
			e.printStackTrace();
			printFacesMessage(FacesMessage.SEVERITY_ERROR, "DBException", e.getMessage(), null);
			return null;
		}
		
		// Sort and return list
		list.sort(null);
		return list;
	}

	public ObstetricsInit getViewedOI() {
		return viewedOI;
	}

	/**
	 * Navigates to the viewObstetricsRecord page to view the given record.
	 * @param viewedOI
	 */
	public void setViewedOI(ObstetricsInit viewedOI) {
		this.viewedOI = viewedOI;
		try {
			NavigationController.viewObstetricsRecord();
		} catch (IOException e) {
			e.printStackTrace();
			printFacesMessage(FacesMessage.SEVERITY_ERROR, "IOException", e.getMessage(), null);
		}
	}
}
