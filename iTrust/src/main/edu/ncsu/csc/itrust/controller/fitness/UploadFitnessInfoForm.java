package edu.ncsu.csc.itrust.controller.fitness;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.Part;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.webutils.SessionUtils;

/**
 * Form used to communicate between the view and controller
 * when uploading a FitBit export file.
 * 
 * @author jcgonzal
 * @author amcheshi
 */
@ManagedBean(name = "upload_fitness_info_form")
@ViewScoped
public class UploadFitnessInfoForm 
{
	/** Controller used to run background logic */
	private FitnessInfoController controller;
	/** Used to access session variables and request parameters */
	private SessionUtils sessionUtils;
	/** Stores the file to be uploaded */
	private Part file;
	/** Stores the type of file to be uploaded */
	private String fileType;
	
	/**
	 * Default constructor.
	 */
	public UploadFitnessInfoForm() {
		this(null, SessionUtils.getInstance(), null);
	}

	/**
	 * Constructs a new FitnessInfoForm instance, setting its fields to the given
	 * FitnessInfoController and SessionUtils instances using the given DataSource.
	 * 
	 * @param fic the given FitnessInfoController
	 * @param sessionUtils the given SessionUtils
	 * @param ds the given DataSource
	 */
	public UploadFitnessInfoForm(FitnessInfoController fic, SessionUtils sessionUtils, DataSource ds) {
		// Set up session utilities
		this.sessionUtils = (sessionUtils == null) ? SessionUtils.getInstance() : sessionUtils;
		
		// Set up controller
		try {
			if (ds == null)
				controller = (fic == null) ? new FitnessInfoController() : fic;
			else
				controller = (fic == null) ? new FitnessInfoController(ds) : fic;			
		} catch (Exception e) {
			this.sessionUtils.printFacesMessage(FacesMessage.SEVERITY_ERROR, "Fitness Info Controller Error",
					"Fitness Info Controller Error", null);
		}
	}

	/**
	 * Uploads the selected file.
	 */
	public void upload()
	{
		if (file == null) {
			return;
		}
		try {
			controller.upload(fileType, file.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns file.
	 * @return file
	 */
	public Part getFile() {
		return file;
	}

	/**
	 * Sets file to the given Part.
	 * @param file the Part to set to
	 */
	public void setFile(Part file) {
		this.file = file;
	}

	/**
	 * @return the fileType
	 */
	public String getFileType() {
		return fileType;
	}

	/**
	 * @param fileType the fileType to set
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
}
