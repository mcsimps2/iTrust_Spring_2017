package edu.ncsu.csc.itrust.controller.fitness;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.model.fitness.FitnessInfo;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

/**
 * Form used to communicate between the view and controller
 * when viewing and adding/editing fitness info.
 * 
 * @author amcheshi
 */
@ManagedBean(name = "fitness_info_form")
@ViewScoped
public class FitnessInfoForm 
{
	/** Controller used to run background logic */
	private FitnessInfoController controller;
	/** Used to store data */
	private FitnessInfo fitnessInfo;
	/** Used to access session variables and request parameters */
	private SessionUtils sessionUtils;
	
	/**
	 * Default constructor.
	 */
	public FitnessInfoForm() {
		this(null, SessionUtils.getInstance(), null);
	}

	/**
	 * Constructs a new FitnessInfoForm instance, setting its fields to the given
	 * FitnessInfoController and SessionUtils instances using the given DataSource.
	 * 
	 * Obtains any pre-existing data for this day. If no data exists, then creates
	 * a new FitnessInfo object and fills it with the appropriate pid and date.
	 * 
	 * @param fic the given FitnessInfoController
	 * @param sessionUtils the given SessionUtils
	 * @param ds the given DataSource
	 */
	public FitnessInfoForm(FitnessInfoController fic, SessionUtils sessionUtils, DataSource ds) {
		// Set up session utilities
		this.sessionUtils = (sessionUtils == null) ? SessionUtils.getInstance() : sessionUtils;
		
		// Set up controller and fitnessInfo
		try {
			if (ds == null)
				controller = (fic == null) ? new FitnessInfoController() : fic;
			else
				controller = (fic == null) ? new FitnessInfoController(ds) : fic;
			
			// Check for pre-existing data
			fitnessInfo = controller.getSelectedFitnessInfo();
			if (fitnessInfo == null) { //no data, so make a new FitnessInfo
				fitnessInfo = new FitnessInfo();
				fitnessInfo.setPid(Long.parseLong(sessionUtils.getSessionPID()));
				fitnessInfo.setDate(sessionUtils.getRequestParameter("date"));
			}
		} catch (Exception e) {
			this.sessionUtils.printFacesMessage(FacesMessage.SEVERITY_ERROR, "Fitness Info Controller Error",
					"Fitness Info Controller Error", null);
		}
	}

	/**
	 * Submits the form, adding/updating the fitness info for the viewed patient on the selected day.
	 */
	public void submit()
	{
		controller.addFitnessInfo(fitnessInfo);
	}

	/**
	 * Returns fitnessInfo.
	 * @return fitnessInfo
	 */
	public FitnessInfo getFitnessInfo() {
		return fitnessInfo;
	}

	/**
	 * Sets fitnessInfo to the given FitnessInfo object.
	 * @param fitnessInfo the given FitnessInfo object
	 */
	public void setFitnessInfo(FitnessInfo fitnessInfo) {
		this.fitnessInfo = fitnessInfo;
	}
}
