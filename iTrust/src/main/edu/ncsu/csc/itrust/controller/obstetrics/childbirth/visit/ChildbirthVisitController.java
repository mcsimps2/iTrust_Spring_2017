package edu.ncsu.csc.itrust.controller.obstetrics.childbirth.visit;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit.ChildbirthVisit;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit.ChildbirthVisitData;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit.ChildbirthVisitMySQL;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

@ManagedBean(name = "childbirth_visit_controller")
@SessionScoped
public class ChildbirthVisitController extends iTrustController {
	
	private static final String CHILDBIRTH_VISIT_SUCCESSFULLY_UPDATED = "The childbirth was successfully updated.";
	
	private ChildbirthVisitData cvData;

	/**
	 * Default constructor.
	 * @throws DBException
	 */
	public ChildbirthVisitController() throws DBException {
		this.cvData = new ChildbirthVisitMySQL();
	}

	/**
	 * For testing purposes.
	 * @param ds DataSource
	 * @param sessionUtils SessionUtils instance
	 */
	public ChildbirthVisitController(DataSource ds, SessionUtils sessionUtils) {
		this.cvData = new ChildbirthVisitMySQL(ds);
	}
	
	
	public ChildbirthVisit getByOfficeVisit(long officeVisitID) {
		try {
			return cvData.getByOfficeVisit(officeVisitID);
		} catch (DBException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void add(ChildbirthVisit cv) {
		try {
			this.cvData.add(cv);
			printFacesMessage(FacesMessage.SEVERITY_INFO, CHILDBIRTH_VISIT_SUCCESSFULLY_UPDATED, CHILDBIRTH_VISIT_SUCCESSFULLY_UPDATED);
			// TODO log 9600, 9601
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, e.getExtendedMessage(), e.getExtendedMessage());
			e.printStackTrace();
		} catch (FormValidationException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void update(ChildbirthVisit cv) {
		try {
			this.cvData.update(cv);
			printFacesMessage(FacesMessage.SEVERITY_INFO, CHILDBIRTH_VISIT_SUCCESSFULLY_UPDATED,
					CHILDBIRTH_VISIT_SUCCESSFULLY_UPDATED);
			// TODO log 9604
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, e.getExtendedMessage(), e.getExtendedMessage());
			e.printStackTrace();
		} catch (FormValidationException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Print a faces message with the given severity, summary, and details.
	 * @param severity
	 * @param summary
	 * @param detail
	 */
	public void printFacesMessage(Severity severity, String summary, String detail) {
		FacesMessage throwMsg = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, throwMsg);
	}
}