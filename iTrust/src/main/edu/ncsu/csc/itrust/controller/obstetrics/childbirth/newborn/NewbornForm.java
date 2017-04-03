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
import edu.ncsu.csc.itrust.webutils.SessionUtils;

@ManagedBean(name = "newborn_form")
@ViewScoped
public class NewbornForm {
	
	private NewbornController controller;
	private ChildbirthVisitController cvc;
	private Long officeVisitID;
	private SessionUtils sessionUtils;
	private Newborn newborn;
	
	private static final String SAVE_CHILDBIRTH_FIRST_ERROR = "The Childbirth tab must be saved before you can add a newborn";

	/**
	 * Constructor used in run time.
	 */
	public NewbornForm() {
	    this(null, null, SessionUtils.getInstance());
	}

	/**
	 * Constructor used for testing. Can pass mocked parameters to unit test.
	 * @param uc
	 * @param ovc
	 * @param sessionUtils
	 */
	public NewbornForm(NewbornController nc, ChildbirthVisitController cvc, SessionUtils sessionUtils) {
		try {
			this.sessionUtils = (sessionUtils == null) ? SessionUtils.getInstance() : sessionUtils;
		    this.cvc = (cvc == null) ? new ChildbirthVisitController() : cvc;
			this.controller = (nc == null) ? new NewbornController() : nc;
			officeVisitID = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("officeVisitId");
			clearFields();
		} catch (Exception e) {
			this.sessionUtils.printFacesMessage(FacesMessage.SEVERITY_ERROR, "Controller Error",
					"Controller Error", null);
		}
	}
	
	/**
	 * Adds the newborn to the database.
	 * Only works if an Childbirth has been submitted for this office visit.
	 */
	public void add(){
		if (cvc.getByOfficeVisit(officeVisitID) == null) {
			sessionUtils.printFacesMessage(FacesMessage.SEVERITY_ERROR, SAVE_CHILDBIRTH_FIRST_ERROR,
					SAVE_CHILDBIRTH_FIRST_ERROR, null);
			return;
		}
		controller.add(newborn);
		clearFields();
	}
	
	/**
	 * Edits the current newborn in the database.
	 */
	public void edit(){
		controller.edit(newborn);
		clearFields();
	}
	
	/**
	 * Deletes the newborn in the database with the given ID.
	 * @param newbornID
	 */
	public void delete(Long newbornID){
		controller.delete(newbornID);
		clearFields();
	}
	
	/**
	 * Returns a List of all of the newborns for the current office visit.
	 * @return all of the newborns for the current office visit
	 */
	public List<Newborn> getNewborns(){
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
	 */
	public void fillInput(Long newbornID, String dateOfBirth, String timeOfBirth, SexType sex, Boolean estimatedTime){
		newborn.setId(newbornID);
		newborn.setDateOfBirth(dateOfBirth);
		newborn.setTimeOfBirth(timeOfBirth);
		newborn.setSex(sex);
		newborn.setTimeEstimated(estimatedTime);
	}
	
	public List<SexType> getSexTypes() {
		return Arrays.asList(SexType.values());
	}
	
	/**
	 * Clears all of the fields in this form.
	 */
	public void clearFields(){
		newborn = new Newborn(officeVisitID);
	}

	public Newborn getNewborn() {
		return newborn;
	}

	public void setNewborn(Newborn newborn) {
		this.newborn = newborn;
	}
}
