package edu.ncsu.csc.itrust.model.obstetrics.initialization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.POJOValidator;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.PatientLoader;

public class ObstetricsInitValidator extends POJOValidator<ObstetricsInit>
{
	private DataSource ds;
	
	/**
	 * Constructor with no parameters
	 * Automatically sets the datasource
	 */
	public ObstetricsInitValidator() throws DBException
	{
		try {
			Context ctx = new InitialContext();
			this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
		} catch (NamingException e) {
			throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
		}
	}
	
	/**
	 * Constructor that sets the datasource
	 * Useful for testing purposes since it can specify a datasource
	 * @param ds the datasource used by the class
	 */
	public ObstetricsInitValidator(DataSource ds) {
		this.ds = ds;
	}

	@Override
	public void validate(ObstetricsInit obj) throws FormValidationException {
		ErrorList errs = new ErrorList();
		//No longer checking Date Of Birth and Date of Death since this is bugged in iTrust
		
		//Verify non-null values
		if (obj.getDate() == null || obj.getLMP() == null)
		{
			errs.addIfNotNull("Null or empty date or LMP");
			throw new FormValidationException(errs);
		}
		
		//Verify LMP <= date
		Date dateOfInit = obj.getJavaDate();
		Date dateOfLMP = obj.getJavaLMP();
	
		if (dateOfInit.before(dateOfLMP))
		{
			errs.addIfNotNull("The initialization date is before the LMP");
			throw new FormValidationException(errs);
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateOfInit);
		cal.add(Calendar.YEAR, -1);
		Date yearBeforeInit = cal.getTime();
		
		if (dateOfLMP.before(yearBeforeInit)) {
			errs.addIfNotNull("The LMP date cannot be more than a year before the initialization date");
			throw new FormValidationException(errs);
		}
		
		//Verify the PID exists in the database
		PatientBean patient = null;
		
		try (Connection conn = ds.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM patients WHERE MID = ?;")) {
			ps.setLong(1, obj.getPid());
			ResultSet rs = ps.executeQuery();
			PatientLoader patientLoader = new PatientLoader();
			patient = rs.next() ? patientLoader.loadSingle(rs) : null;
			rs.close();
		} catch (SQLException e) {
			errs.addIfNotNull("Could not find the patient with the specified PID");
			throw new FormValidationException(errs);
		}
		
		if (patient == null)
		{
			errs.addIfNotNull("Could not find the patient with the specified PID");
			throw new FormValidationException(errs);
		}
		
		//Verify the patient is obstetrics eligible
		if (!patient.getObstetricsCareEligibility())
		{
			errs.addIfNotNull("Patient is not eligible for obstetrics care");
			throw new FormValidationException(errs);
		}
	}
	
	
}
