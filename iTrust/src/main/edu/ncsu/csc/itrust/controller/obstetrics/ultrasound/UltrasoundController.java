package edu.ncsu.csc.itrust.controller.obstetrics.ultrasound;


import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.obstetrics.ultrasound.Ultrasound;
import edu.ncsu.csc.itrust.model.obstetrics.ultrasound.UltrasoundMySQL;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

@ManagedBean(name = "ultrasound_controller")
@SessionScoped
public class UltrasoundController extends iTrustController {

	/** Constant for the message to be displayed if the ultrasound is invalid */
	private static final String INVALID_ULTRASOUND = "Invalid Ultrasound";
	/** Constant for the message to be displayed if the ultrasound was successfully created */
	private static final String ULTRASOUND_SUCCESSFULLY_CREATED = "Ultrasound is Successfully Created";
	/** Constant for the message to be displayed if the ultrasound was successfully updated */
	private static final String ULTRASOUND_SUCCESSFULLY_UPDATED = "Ultrasound is Successfully Updated";
	/** Constant for the message to be displayed if the ultrasound was successfully deleted */
	private static final String ULTRASOUND_SUCCESSFULLY_DELETED = "Ultrasound is Successfully Deleted";
	/** Constant for the message to be displayed if ultrasounds are unable to be retrieved */
	private static final String UNABLE_TO_RETRIEVE_ULTRASOUNDS = "Unable to Retrieve Ultrasounds";
	private UltrasoundMySQL sql;

	/**
	 * Default constructor.
	 * @throws DBException
	 */
	public UltrasoundController() throws DBException {
		super();
		sql = new UltrasoundMySQL();
	}

	/**
	 * Constructor injection, intended only for unit testing purposes.
	 * 
	 * @param ds
	 *            The injected DataSource dependency
	 */
	public UltrasoundController(DataSource ds) throws DBException {
		super();
		this.sql = new UltrasoundMySQL(ds);
	}

	/**
	 * Adds the given ultrasound to the database.
	 * @param ultrasound
	 */
	public void add(Ultrasound ultrasound) {
		try {
			if (sql.add(ultrasound)) {
				printFacesMessage(FacesMessage.SEVERITY_INFO, ULTRASOUND_SUCCESSFULLY_CREATED,
						ULTRASOUND_SUCCESSFULLY_CREATED, null);
				logTransaction(TransactionType.ULTRASOUND, getSessionUtils().getCurrentOfficeVisitId().toString());
			} else {
				throw new Exception();
			}
		} catch (SQLException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_ULTRASOUND, e.getMessage(), null);
		} catch (Exception e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_ULTRASOUND, INVALID_ULTRASOUND, null);
		}
	}

	/**
	 * Edits the given ultrasound in the database.
	 * @param ultrasound
	 */
	public void edit(Ultrasound ultrasound) {
		try {
			if (sql.update(ultrasound)) {
				printFacesMessage(FacesMessage.SEVERITY_INFO, ULTRASOUND_SUCCESSFULLY_UPDATED,
						ULTRASOUND_SUCCESSFULLY_UPDATED, null);
				logTransaction(TransactionType.ULTRASOUND, getSessionUtils().getCurrentOfficeVisitId().toString());
			} else {
				throw new Exception();
			}
		} catch (SQLException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_ULTRASOUND, e.getMessage(), null);
		} catch (Exception e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_ULTRASOUND, INVALID_ULTRASOUND, null);
		}
	}

	/**
	 * Deletes the ultrasound in the database with the given ID.
	 * @param ultrasoundID
	 */
	public void delete(long ultrasoundID) {
        try {
        	if (sql.delete(ultrasoundID)) {
				printFacesMessage(FacesMessage.SEVERITY_INFO, ULTRASOUND_SUCCESSFULLY_DELETED,
						ULTRASOUND_SUCCESSFULLY_DELETED, null);
				logTransaction(TransactionType.ULTRASOUND, getSessionUtils().getCurrentOfficeVisitId().toString());
        	} else {
        		throw new Exception();
        	}
        } catch (SQLException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_ULTRASOUND, e.getMessage(), null);
		} catch (Exception e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_ULTRASOUND, INVALID_ULTRASOUND, null);
		}
	}
	
	/**
	 * Returns a List of all of the Ultrasounds with the given officeVisitID.
	 * @param officeVisitID
	 * @return all of the ultrasounds for the office visit with the given ID
	 * @throws DBException
	 */
	public List<Ultrasound> getUltrasoundsByOfficeVisit(Long officeVisitID) throws DBException {
		List<Ultrasound> ultrasounds = Collections.emptyList();
		try {
			ultrasounds = sql.getByOfficeVisit(officeVisitID);
		} catch (Exception e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, UNABLE_TO_RETRIEVE_ULTRASOUNDS, UNABLE_TO_RETRIEVE_ULTRASOUNDS, null);
		}
		return ultrasounds;
	}
}
