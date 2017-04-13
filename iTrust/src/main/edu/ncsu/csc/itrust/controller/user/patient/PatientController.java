package edu.ncsu.csc.itrust.controller.user.patient;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;

import edu.ncsu.csc.itrust.controller.user.UserController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.DataBean;
import edu.ncsu.csc.itrust.model.user.patient.Patient;

@ManagedBean(name="patient_controller")
public class PatientController extends UserController implements Serializable{
	private DataBean<Patient> patientData;
	public PatientController() throws DBException {
		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -164769440967020045L;
	

	
	public boolean doesPatientExistWithID(String mid) throws DBException{
		return doesRecordExistWithID(mid, patientData);
	}


}
