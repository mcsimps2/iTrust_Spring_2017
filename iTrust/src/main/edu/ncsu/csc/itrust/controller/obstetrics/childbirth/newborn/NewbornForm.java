package edu.ncsu.csc.itrust.controller.obstetrics.childbirth.newborn;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import edu.ncsu.csc.itrust.controller.obstetrics.childbirth.visit.ChildbirthVisitController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns.Newborn;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns.SexType;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

@ManagedBean(name = "newborn_form")
@ViewScoped
public class NewbornForm {
	
	private NewbornController controller;
	private ChildbirthVisitController cvc;
	private SessionUtils sessionUtils;
	private Newborn newborn;
	private PatientBean patient;
	private PatientDAO patientDAO;
	
	private static final String SAVE_CHILDBIRTH_FIRST_ERROR = "The Childbirth tab must be saved before you can add a newborn";

	/**
	 * Constructor used in run time.
	 */
	public NewbornForm() {
	    this(null, null, SessionUtils.getInstance(), (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("officeVisitId"),
	    		new PatientDAO(DAOFactory.getProductionInstance()));
	}

	/**
	 * Constructor used for testing. Can pass mocked parameters to unit test.
	 * @param uc
	 * @param ovc
	 * @param sessionUtils
	 */
	public NewbornForm(NewbornController nc, ChildbirthVisitController cvc, SessionUtils sessionUtils, Long officeVisitID, PatientDAO patientDAO) {
		try {
			this.sessionUtils = (sessionUtils == null) ? SessionUtils.getInstance() : sessionUtils;
		    this.cvc = (cvc == null) ? new ChildbirthVisitController() : cvc;
			this.controller = (nc == null) ? new NewbornController() : nc;
			this.patientDAO = patientDAO;
			PatientBean mother = patientDAO.getPatient(sessionUtils.getCurrentPatientMIDLong());
			clearFields(officeVisitID, mother.getLastName(), mother.getEmail());
		} catch (Exception e) {
			this.sessionUtils.printFacesMessage(FacesMessage.SEVERITY_ERROR, "Controller Error",
					"Controller Error", null);
		}
	}
	
	/**
	 * Adds the newborn to the database.
	 * Only works if a child birth has been submitted for this office visit.
	 * @param officeVisitID
	 * @param lastName
	 * @param email
	 */
	public void add(Long officeVisitID, String lastName, String email){
		if (cvc.getByOfficeVisit(officeVisitID) == null) {
			sessionUtils.printFacesMessage(FacesMessage.SEVERITY_ERROR, SAVE_CHILDBIRTH_FIRST_ERROR,
					SAVE_CHILDBIRTH_FIRST_ERROR, null);
			return;
		}
		if (controller.add(newborn, patient))
			clearFields(officeVisitID, lastName, email);
	}
	
	/**
	 * Edits the current newborn in the database.
	 * @param officeVisitID
	 * @param lastName
	 * @param email
	 */
	public void edit(Long officeVisitID, String lastName, String email){
		if (controller.edit(newborn, patient))
			clearFields(officeVisitID, lastName, email);
	}
	
	/**
	 * Deletes the newborn in the database with the given ID.
	 * @param newbornID
	 */
	public void delete(Long newbornID){
		controller.delete(newbornID);
	}
	
	/**
	 * Returns a List of all of the newborns for the current office visit.
	 * @param officeVisitID
	 * @return all of the newborns for the current office visit
	 */
	public List<Newborn> getNewborns(Long officeVisitID){
		List<Newborn> newborns = Collections.emptyList();
		try {
			newborns = controller.getNewbornsByOfficeVisit(officeVisitID);
		} catch (DBException e) {
			sessionUtils.printFacesMessage(FacesMessage.SEVERITY_ERROR, "Newborn Controller Error", "Newborn Controller Error",
					null);
		}
		return newborns;
	}
	
	/**
	 * Fills the fields in this form with the given parameters.
	 * Used when editing an existing ultrasound on the page.
	 * @param newbornID
	 * @param dateOfBirth
	 * @param time
	 * @param sex
	 * @param estimatedTime
	 * @param pid
	 * @throws DBException  
	 */
	public void fillInput(Long newbornID, String dateOfBirth, String timeOfBirth, SexType sex, Boolean estimatedTime, Long pid) throws DBException {
		newborn.setId(newbornID);
		newborn.setDateOfBirth(dateOfBirth);
		newborn.setTimeOfBirth(timeOfBirth);
		newborn.setSex(sex);
		newborn.setTimeEstimated(estimatedTime);
		newborn.setPid(pid);
		patient = patientDAO.getPatient(pid);
	}
	
	public List<SexType> getSexTypes() {
		return Arrays.asList(SexType.values());
	}
	
	/**
	 * Clears all of the fields in this form.
	 * @param officeVisitID
	 * @param lastName
	 */
	public void clearFields(Long officeVisitID, String lastName, String email){
		newborn = new Newborn(officeVisitID);
		patient = new PatientBean();
		patient.setLastName(lastName);
		patient.setEmail(email);
	}

	public Newborn getNewborn() {
		return newborn;
	}

	public void setNewborn(Newborn newborn) {
		this.newborn = newborn;
	}
	
	public PatientBean getPatient() {
		return patient;
	}
	
	public void setPatient(PatientBean patient) {
		this.patient = patient;
	}
	
	public String getFirstName(Long pid) {
		try {
			PatientBean p = patientDAO.getPatient(pid);
			if (p == null) return "";
			return p.getFirstName();
		} catch (DBException e) {
			return "";
		}
	}
	
	public String getLastName(Long pid) {
		try {
			PatientBean p = patientDAO.getPatient(pid);
			if (p == null) return "";
			return p.getLastName();
		} catch (DBException e) {
			return "";
		}
	}
}
