package edu.ncsu.csc.itrust.controller.obstetrics.ultrasound;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;

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
	//TODO private UltrasoundMySQL sql;

	public UltrasoundController() throws DBException {
		super();
		//TODO sql = new UltrasoundMySQL();
	}

	/**
	 * Constructor injection, intended only for unit testing purposes.
	 * 
	 * @param ds
	 *            The injected DataSource dependency
	 */
	public UltrasoundController(DataSource ds) throws DBException {
		super();
		//TODO this.sql = new UltrasoundMySQL(ds);
	}
	
	/* TODO
	public void setSql(UltrasoundMySQL sql){
	    this.sql = sql;
	}
	*/

	/* TODO
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
	*/

	/* TODO
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
	*/

	/* TODO
	public void remove(long ultrasoundID) {
        try {
        	if (sql.remove(ultrasoundID)) {
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
	*/
	
	/* TODO
	public List<Ultrasound> getUltrasoundsByOfficeVisit(Long officeVisitID) throws DBException {
		List<Ultrasound> ultrasounds = Collections.emptyList();
		try {
			ultrasounds = sql.getUltrasoundsForOfficeVisit(officeVisitID);
		} catch (Exception e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, UNABLE_TO_RETRIEVE_ULTRASOUNDS, UNABLE_TO_RETRIEVE_ULTRASOUNDS, null);
		}
		return ultrasounds;
	}
	*/
}
