package edu.ncsu.csc.itrust.controller.obstetrics.visit;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInit;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInitData;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInitMySQL;
import edu.ncsu.csc.itrust.model.obstetrics.visit.ObstetricsVisit;
import edu.ncsu.csc.itrust.model.obstetrics.visit.ObstetricsVisitData;
import edu.ncsu.csc.itrust.model.obstetrics.visit.ObstetricsVisitMySQL;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

@ManagedBean(name = "obstetrics_visit_controller")
@SessionScoped
public class ObstetricsVisitController extends iTrustController {
	/** Max number of weeks since LMP for a patient to be considered an obstetrics patient */
	private static final int MAX_WEEKS_SINCE_LMP = 49;
	/** Constant for the error message to be displayed if the Obstetrics Visit is invalid */
	private static final String OBSTETRICS_VISIT_CANNOT_BE_UPDATED = "Invalid Obstetrics Visit";
	/** Constant for the message to be displayed if the obstetrics visit was successfully updated */
	private static final String OBSTETRICS_VISIT_SUCCESSFULLY_UPDATED = "Obstetrics Visit Successfully Updated";
	/** Constant for the error message to be displayed if an image fails to upload */
	private static final String FILE_UPLOAD_FAILED = "File Upload Failed";
	/** Constant for the message to be displayed if an image is successfully uploaded */
	private static final String FILE_UPLOAD_SUCCESS = "File Successfully Uploaded";
	
	private ObstetricsVisitData ovData;
	private ObstetricsInitData oiData;

	/**
	 * Default constructor.
	 * @throws DBException
	 */
	public ObstetricsVisitController() throws DBException {
		ovData = new ObstetricsVisitMySQL();
		oiData = new ObstetricsInitMySQL();
	}

	/**
	 * For testing purposes
	 * 
	 * @param ds
	 *            DataSource
	 * @param sessionUtils
	 *            SessionUtils instance
	 */
	public ObstetricsVisitController(DataSource ds, SessionUtils sessionUtils) {
		ovData = new ObstetricsVisitMySQL(ds);
		oiData = new ObstetricsInitMySQL(ds);
	}

	/**
	 * For testing purposes
	 * 
	 * @param ds
	 *            DataSource
	 */
	public ObstetricsVisitController(DataSource ds) {
		ovData = new ObstetricsVisitMySQL(ds);
		oiData = new ObstetricsInitMySQL(ds);
	}
	
	/**
	 * Returns the ObstetricsVisit that has the given officeVisitID.
	 * If such an ObstetricsVisit does not exist, returns null.
	 * @param ovID ID of the OfficeVisit associated with the desired ObstetricsVisit
	 * @return the ObstetricsVisit with the given officeVisitID, or null if not found
	 */
	public ObstetricsVisit getByOfficeVisit(Long ovID) {
		try {
			return ovData.getByOfficeVisit(ovID.longValue());
		} catch (DBException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Return the most recent ObstetricsInit record using the date and pid from the given OfficeVisit.
	 * If no record is found, returns null.
	 * @param ov
	 * @return
	 */
	public ObstetricsInit getMostRecentOI(OfficeVisit ov) {
		// Convert OfficeVisit LocalDateTime to java.util.Date
		Date ovDate = Date.from(ov.getDate().toInstant(ZoneOffset.UTC));

		//Log the transaction
		logTransaction(TransactionType.VIEW_OBSTETRIC_OFFICE_VISIT, "Office Visit ID: " + ov.getVisitID());
		// Get the list of initialization records for the patient
		List<ObstetricsInit> oiList;
		try {
			oiList = oiData.getRecords(ov.getPatientMID());
		} catch (DBException e) {
			e.printStackTrace();
			return null;
		}
		if (oiList.isEmpty()) {
			return null;
		}
		
		// Iterate through the list to find the most recent initialization as of the office visit date
		for (ObstetricsInit oi : oiList) {
			// Calculate the time difference
			Date lmpDate = oi.getJavaLMP();
			long diff = ovDate.getTime() - lmpDate.getTime();
			if (diff >= 0) {
				return oi;
			}
		}
		return null;
	}
	
	/**
	 * Calculates and returns the number of weeks pregnant at the time of the given OfficeVisit.
	 * Uses the date of the given office visit and the LMP from the most recent obstetrics
	 * initialization record for the patient associated with the office visit.
	 * 
	 * If the number of weeks pregnant is 49 or greater, or if there are no initialization records
	 * for the patient, then returns null as the patient would no longer be considered an
	 * obstetrics patient.
	 * 
	 * @param ov the given OfficeVisit
	 * @return the number of weeks pregnant, or null if not an obstetrics patient
	 * @throws DBException 
	 */
	public Integer calculateWeeksPregnant(OfficeVisit ov, ObstetricsInit oi) {
		if (oi == null) {
			return null;
		}
		
		// Convert OfficeVisit LocalDateTime to java.util.Date
		Date ovDate = Date.from(ov.getDate().toInstant(ZoneOffset.UTC));
		
		// Calculate the time difference
		Date lmpDate = oi.getJavaLMP();
		long diff = ovDate.getTime() - lmpDate.getTime();
		int weeks = (int) (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)/7);
		return weeks < MAX_WEEKS_SINCE_LMP ? weeks : null;
	}
	
	/**
	 * Attempts to add the given ObstetricsVisit to the database.
	 * @param ov
	 * @throws FormValidationException 
	 * @throws DBException 
	 */
	public void add(ObstetricsVisit ov) throws DBException, FormValidationException {
		ovData.add(ov);
		logTransaction(TransactionType.CREATE_OBSTETRIC_OFFICE_VISIT, "Office Visit ID: " + ov.getOfficeVisitID().toString());
	}
	
	/**
	 * Attempts to update the given ObstetricsVisit in the database.
	 * @param ov
	 */
	public void update(ObstetricsVisit ov) {
		try {
			ovData.update(ov);
			printFacesMessage(FacesMessage.SEVERITY_INFO, OBSTETRICS_VISIT_SUCCESSFULLY_UPDATED,
					OBSTETRICS_VISIT_SUCCESSFULLY_UPDATED, null);
			logTransaction(TransactionType.EDIT_OBSTETRIC_OFFICE_VISIT, "Office Visit ID: " + ov.getOfficeVisitID().toString());
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, OBSTETRICS_VISIT_CANNOT_BE_UPDATED, e.getExtendedMessage(),
					null);
		} catch (Exception e) {
			e.printStackTrace();
			printFacesMessage(FacesMessage.SEVERITY_ERROR, OBSTETRICS_VISIT_CANNOT_BE_UPDATED,
					OBSTETRICS_VISIT_CANNOT_BE_UPDATED, null);
		}
	}
	
	/**
	 * Updates the given ObstetricsVisit in the database (but now it should contain a new file).
	 * @param ov
	 */
	public void upload(ObstetricsVisit ov) {
		try {
			ovData.update(ov);
			printFacesMessage(FacesMessage.SEVERITY_INFO, FILE_UPLOAD_SUCCESS,
					FILE_UPLOAD_SUCCESS, null);
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, FILE_UPLOAD_FAILED, e.getExtendedMessage(),
					null);
		} catch (Exception e) {
			e.printStackTrace();
			printFacesMessage(FacesMessage.SEVERITY_ERROR, FILE_UPLOAD_FAILED,
					FILE_UPLOAD_FAILED, null);
		}
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
}
