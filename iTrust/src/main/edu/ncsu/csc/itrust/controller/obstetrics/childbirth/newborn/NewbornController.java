package edu.ncsu.csc.itrust.controller.obstetrics.childbirth.newborn;


import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns.Newborn;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns.NewbornMySQL;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

@ManagedBean(name = "newborn_controller")
@SessionScoped
public class NewbornController extends iTrustController {
	
	/** Newborn mysql class to access the db */
	NewbornMySQL sql;
	/** Patient DAO used to create and delete patients */
	PatientDAO patientDAO;
	
	/** Constant for the message to be displayed if the newborn is invalid */
	private static final String INVALID_NEWBORN = "Invalid Newborn";
	/** Constant for the message to be displayed if the newborn was successfully created */
	private static final String NEWBORN_SUCCESSFULLY_CREATED = "Newborn was successfully created";
	/** Constant for the message to be displayed if newborns are unable to be retrieved */
	private static final String UNABLE_TO_RETRIEVE_NEWBORNS = "Unable to retrieve newborns";
	/** Constant for the message to be displayed if newborn was successfully deleted */
	private static final String NEWBORN_SUCCESSFULLY_DELETED = "Newborn was successfully deleted";

	/**
	 * Default constructor.
	 * @throws DBException
	 */
	public NewbornController() throws DBException {
		super();
		sql = new NewbornMySQL();
		patientDAO = new PatientDAO(DAOFactory.getProductionInstance());
	}

	/**
	 * Constructor injection, intended only for unit testing purposes.
	 * 
	 * @param ds
	 *            The injected DataSource dependency
	 */
	public NewbornController(DataSource ds, PatientDAO patientDAO) throws DBException {
		super();
		this.sql = new NewbornMySQL(ds);
		this.patientDAO = patientDAO;
	}

	/**
	 * Adds the given newborn to the database and creates a patient
	 * @param newborn
	 */
	public void add(Newborn newborn) {
		try {
			if (sql.add(newborn)) {
				long pid = patientDAO.addEmptyPatient();
				newborn.setPID(pid);
				if (sql.update(newborn)) {
					printFacesMessage(FacesMessage.SEVERITY_INFO, NEWBORN_SUCCESSFULLY_CREATED,
							NEWBORN_SUCCESSFULLY_CREATED, null);
					logTransaction(TransactionType.ULTRASOUND, getSessionUtils().getCurrentOfficeVisitId().toString());
				} else {
					throw new Exception();
				}
			} else {
				throw new Exception();
			}
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_NEWBORN, e.getExtendedMessage(), null);
		} catch (Exception e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_NEWBORN, INVALID_NEWBORN, null);
		}
	}

	/**
	 * Edits the given newborn in the database.
	 * @param newborn
	 */
	public void edit(Newborn newborn) {
		try {
			if (sql.update(newborn)) {
				printFacesMessage(FacesMessage.SEVERITY_INFO, NEWBORN_SUCCESSFULLY_CREATED,
						NEWBORN_SUCCESSFULLY_CREATED, null);
				//logTransaction(TransactionType.NEWBORN, getSessionUtils().getCurrentOfficeVisitId().toString());
			} else {
				throw new Exception();
			}
		} catch (SQLException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_NEWBORN, e.getMessage(), null);
		} catch (Exception e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_NEWBORN, INVALID_NEWBORN, null);
		}
	}

	/**
	 * Deletes the newborn in the database with the given ID and deletes the associated patient object.
	 * @param newbornID
	 */
	public void delete(long newbornID) {
        try {
        	if (sql.delete(newbornID)) {
				printFacesMessage(FacesMessage.SEVERITY_INFO, NEWBORN_SUCCESSFULLY_DELETED,
						NEWBORN_SUCCESSFULLY_DELETED, null);
				//logTransaction(TransactionType.NEWBORN, getSessionUtils().getCurrentOfficeVisitId().toString());
        	} else {
        		throw new Exception();
        	}
        } catch (SQLException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_NEWBORN, e.getMessage(), null);
		} catch (Exception e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_NEWBORN, INVALID_NEWBORN, null);
		}
	}
	
	/**
	 * Returns a List of all of the Newborns with the given officeVisitID.
	 * @param officeVisitID
	 * @return all of the newborns for the office visit with the given ID
	 * @throws DBException
	 */
	public List<Newborn> getNewbornsByOfficeVisit(Long officeVisitID) throws DBException {
		List<Newborn> newborns = Collections.emptyList();
		try {
			newborns = sql.getByOfficeVisit(officeVisitID);
		} catch (Exception e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, UNABLE_TO_RETRIEVE_NEWBORNS + officeVisitID, UNABLE_TO_RETRIEVE_NEWBORNS + officeVisitID, null);
		}
		return newborns;
	}
}
