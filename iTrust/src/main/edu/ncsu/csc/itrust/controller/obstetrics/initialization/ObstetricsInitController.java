package edu.ncsu.csc.itrust.controller.obstetrics.initialization;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.CSVParser;
import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.CSVFormatException;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.fitness.FitbitImportFactory;
import edu.ncsu.csc.itrust.model.fitness.FitnessImportFactory;
import edu.ncsu.csc.itrust.model.fitness.FitnessInfo;
import edu.ncsu.csc.itrust.model.fitness.FitnessInfoData;
import edu.ncsu.csc.itrust.model.fitness.FitnessInfoFileFormatException;
import edu.ncsu.csc.itrust.model.fitness.FitnessInfoMySQL;
import edu.ncsu.csc.itrust.model.fitness.MicrosoftBandImportFactory;
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
	//ObstetricsInitData oiData; TODO
	/** Used to obtain session variables and request parameters */
	SessionUtils sessionUtils;
	/** Queue of messages to be printed to the screen TODO*/
	
	
	/**
	 * Default constructor.
	 */
	public ObstetricsInitController() {
		super();
		sessionUtils = SessionUtils.getInstance();
		/* TODO
		try {
			oiData = new ObstetricsInitMySQL();
		} catch (DBException e) {
			e.printStackTrace();
		}
		*/ 
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
		//oiData = new ObstetricsInitMySQL(ds); TODO
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
			return false;
		}
		
		// Check patient eligibility
		PatientDAO dao = new PatientDAO(DAOFactory.getProductionInstance());
		//return dao.getPatient(pidLong).getObstetricsCareEligibility(); TODO
		return true; //TODO
	}
	
	public void makePatientEligible(String pid, String hcpid) {
		// Parse the Strings
		long pidLong;
		long hcpidLong;
		try {
			pidLong = Long.parseLong(pid);
			hcpidLong = Long.parseLong(hcpid);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return;
		}
		
		// Get the patient bean from the database
		PatientDAO dao = new PatientDAO(DAOFactory.getProductionInstance());
		PatientBean patient;
		try {
			patient = dao.getPatient(pidLong);
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		String message = String.format("%s %s %s", patient.getFirstName(), patient.getLastName(), PATIENT_MADE_ELIGIBLE);
		printFacesMessage(FacesMessage.SEVERITY_INFO, message, message, null);
		
		// Change eligibility and update database
		/*
		patient.setObstetricsCareEligibility(true); TODO
		dao.editPatient(patient, hcpidLong);
		*/
	}
	
	public void getRecords(String pid) {
		
	}
}
