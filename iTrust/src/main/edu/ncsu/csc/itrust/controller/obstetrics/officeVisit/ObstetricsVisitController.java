package edu.ncsu.csc.itrust.controller.obstetrics.officeVisit;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalQueries;
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
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInit;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInitData;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInitMySQL;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

@ManagedBean(name = "obstetrics_visit_controller")
@SessionScoped
public class ObstetricsVisitController extends iTrustController {
	/** Max number of weeks since LMP for a patient to be considered an obstetrics patient */
	private static final int MAX_WEEKS_SINCE_LMP = 49;
	//private ObstetricsVisitData ovData; TODO
	private ObstetricsInitData oiData;
	private SessionUtils sessionUtils;

	public ObstetricsVisitController() throws DBException {
		//ovData = new ObstetricsVisitMySQL(); TODO
		oiData = new ObstetricsInitMySQL();
		sessionUtils = SessionUtils.getInstance();
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
		//ovData = new ObstetricsVisitMySQL(ds); TODO
		oiData = new ObstetricsInitMySQL(ds);
		this.sessionUtils = sessionUtils;
	}

	/**
	 * For testing purposes
	 * 
	 * @param ds
	 *            DataSource
	 */
	public ObstetricsVisitController(DataSource ds) {
		//ovData = new ObstetricsVisitMySQL(ds); TODO
		oiData = new ObstetricsInitMySQL(ds);
		sessionUtils = SessionUtils.getInstance();
	}
	
	/**
	 * Returns the ObstetricsVisit that has the given officeVisitID.
	 * If such an ObstetricsVisit does not exist, returns null.
	 * @param ovID ID of the OfficeVisit associated with the desired ObstetricsVisit
	 * @return the ObstetricsVisit with the given officeVisitID, or null if not found
	 */
	/* TODO
	public ObtetricsVisit getVisitWithID(Long ovID) {
		return null;
	}
	*/
	
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
	public Integer calculateWeeksPregnant(OfficeVisit ov) throws Exception { //TODO GET RID OF THROWS DECLARATION
		// Convert OfficeVisit LocalDateTime to java.util.Date
		Date ovDate = Date.from(ov.getDate().toInstant(ZoneOffset.UTC));

		// Get the list of initialization records for the patient
		List<ObstetricsInit> oiList = oiData.getRecords(ov.getPatientMID());
		if (oiList.isEmpty()) {
			return null;
		}
		
		// Iterate through the list to find the most recent initialization as of the office visit date
		for (ObstetricsInit oi : oiList) {
			// Calculate the time difference
			Date lmpDate = oi.getJavaLMP();
			long diff = ovDate.getTime() - lmpDate.getTime();
			if (diff >= 0) {
				int weeks = (int) (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)/7);
				return weeks < MAX_WEEKS_SINCE_LMP ? weeks : null;
			}
		}
		return null;
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
