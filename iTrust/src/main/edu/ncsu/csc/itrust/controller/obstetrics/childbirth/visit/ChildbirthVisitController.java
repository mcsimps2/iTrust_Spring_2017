package edu.ncsu.csc.itrust.controller.obstetrics.childbirth.visit;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit.ChildbirthVisit;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit.ChildbirthVisitData;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit.ChildbirthVisitMySQL;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
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
			printFacesMessage(FacesMessage.SEVERITY_INFO, CHILDBIRTH_VISIT_SUCCESSFULLY_UPDATED, CHILDBIRTH_VISIT_SUCCESSFULLY_UPDATED, null);

			logTransaction(TransactionType.CREATE_CHILDBIRTH_VISIT, "Office Visit ID: " + cv.getOfficeVisitID());
			logTransaction(TransactionType.ADD_CHILDBIRTH_DRUGS, "Office Visit ID: " + cv.getOfficeVisitID());
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, e.getExtendedMessage(), e.getExtendedMessage(), null);
			e.printStackTrace();
		} catch (FormValidationException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage(), null);
			e.printStackTrace();
		}
	}
	
	public void update(ChildbirthVisit cv) {
		try {
			this.cvData.update(cv);
			printFacesMessage(FacesMessage.SEVERITY_INFO, CHILDBIRTH_VISIT_SUCCESSFULLY_UPDATED,
					CHILDBIRTH_VISIT_SUCCESSFULLY_UPDATED, null);
			
			logTransaction(TransactionType.EDIT_CHILDBIRTH_VISIT, "Office Visit ID: " + cv.getOfficeVisitID());
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, e.getExtendedMessage(), e.getExtendedMessage(), null);
			e.printStackTrace();
		} catch (FormValidationException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage(), null);
			e.printStackTrace();
		}
	}
}