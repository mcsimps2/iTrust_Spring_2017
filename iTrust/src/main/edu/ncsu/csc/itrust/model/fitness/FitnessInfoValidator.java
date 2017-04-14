package edu.ncsu.csc.itrust.model.fitness;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.POJOValidator;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.PatientLoader;

/**
 * Validates FitnessInfo objects to make sure their fields make sense
 * Namely, there are no negative values, the PID corresponds to a real patient,
 * the date is between their date of birth and date of death, and that the calories burned
 * is greater than "active calories"
 * @author mcsimps2
 *
 */
public class FitnessInfoValidator extends POJOValidator<FitnessInfo>
{
	private DataSource ds;
	
	/**
	 * Constructor with no parameters
	 * Automatically sets the datasource
	 */
	public FitnessInfoValidator() throws DBException
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
	public FitnessInfoValidator(DataSource ds) {
		this.ds = ds;
	}
	
	/**
	 * Used to validate a FitnessInfo object.  If the validation does not succeed, a {@link FormValidationException}
	 * is thrown.
	 * The validation is done as follows:
	 * (1) Verifies the PID exists in the patient database
	 * (2) Verifies the PID is for a patient
	 * (3) Verifies the date of the FitnessInfo is between the patients date of birth and death
	 * (4) Verifies all fields are non-negative
	 * (5) Verifies the burned calories are >= to active calories
	 * @param obj the FitnessInfo object to be validated
	 * @throws FormValidationException
	 */
	@Override
	public void validate(FitnessInfo obj) throws FormValidationException {
		
		ErrorList errs = new ErrorList();
		
		//WE ARE UNABLE TO USE AUTHDAO AND PATIENTDAO HERE because we only have
		//A datasource.  Calling them would assume the datasource is given by the context
		// initialization, but it could also be for testing. Thus, we need to go thru
		//the datasource to get our info
		
		//First, check the user exists
		
		try (Connection conn = ds.getConnection();
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE MID=?");)
		{
			stmt.setLong(1, obj.getPid());
			try (ResultSet results = stmt.executeQuery();)
			{
				final boolean check = results.next();
				if (!check)
				{
					errs.addIfNotNull("The specified PID does not exist in the database");
					throw new FormValidationException(errs);
				}
			}
		} catch (SQLException e) {
			errs.addIfNotNull("Could not access database to check PID");
			throw new FormValidationException(errs);
		}
		
		//Now get the Date of Birth (DOB) and date of death (DOD)
		//Make a patientbean of the patient
		PatientBean patient = null;
		try (Connection conn = ds.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM patients WHERE MID = ?")) {
			ps.setLong(1, obj.getPid());
			ResultSet rs = ps.executeQuery();
			PatientLoader patientLoader = new PatientLoader();
			patient = rs.next() ? patientLoader.loadSingle(rs) : null;
			rs.close();
		} catch (SQLException e) {
			//System.out.println("Bad pid4");
			errs.addIfNotNull("Could not find the patient with the specified PID");
			throw new FormValidationException(errs);
		}
		//Patient better not be null now
		if (patient == null)
		{
			//System.out.println("Bad pid5");
			errs.addIfNotNull("Could not find the patient with the specified PID");
			throw new FormValidationException(errs);
		}
		
		//Now, we have a valid PID, and the PatientBean corresponding to it

		//Verify the date
		if (obj.getDate() == null)
		{
			errs.addIfNotNull("No date in fitness object");
			throw new FormValidationException(errs);
		}
		Date DOB = patient.getDateOfBirth();
		Date DOD = patient.getDateOfDeath(); //Date of death
		Date date = obj.getJavaDate();
		if (DOB != null)
		{
			//Make sure the date is after DOB
			
			if (DOB.after(date))
			{
				errs.addIfNotNull("The date of birth is after the given date of data!");
			}
		}
		if (DOD != null)
		{
			//Make sure the date is before DOD
			if (DOD.before(date))
			{
				errs.addIfNotNull("The date of death is before the given date of data!");
			}
		}
		
		//Now check other fields
		if (obj.getSteps() < 0)
		{
			errs.addIfNotNull("Negative step count");
		}
		if (obj.getCaloriesBurned() < 0)
		{
			errs.addIfNotNull("Negative calories burned");
		}
		if (obj.getMiles() < 0)
		{
			errs.addIfNotNull("Negative distance");
		}
		if (obj.getFloors() < 0)
		{
			errs.addIfNotNull("Negative floors");
		}
		if (obj.getActiveCalories() < 0)
		{
			errs.addIfNotNull("Negative active calories");
		}
		if (obj.getMinutesSedentary() < 0)
		{
			errs.addIfNotNull("Negative minutes sedentary");
		}
		if (obj.getMinutesFairlyActive() < 0)
		{
			errs.addIfNotNull("Negative minutes fairly active");
		}
		if (obj.getMinutesLightlyActive() < 0)
		{
			errs.addIfNotNull("Negative minutes lightly active");
		}
		if (obj.getMinutesVeryActive() < 0)
		{
			errs.addIfNotNull("Negative minutes very active");
		}
		if (obj.getHeartRateLow() < 0)
		{
			errs.addIfNotNull("Negative heart rate low");
		}
		if (obj.getHeartRateHigh() < 0)
		{
			errs.addIfNotNull("Negative heart rate high");
		}
		if (obj.getHeartRateAvg() < 0)
		{
			errs.addIfNotNull("Negative heart rate avg");
		}
		if (obj.getActiveHours() < 0)
		{
			errs.addIfNotNull("Negative active hours");
		}
		if (obj.getMinutesUVExposure() < 0)
		{
			errs.addIfNotNull("Negative minutes UV exposure");
		}
		if (obj.getActiveCalories() > obj.getCaloriesBurned())
		{
			errs.addIfNotNull("Active calories is greater than the calories burned");
		}
		if (errs.hasErrors())
		{
			throw new FormValidationException(errs);
		}
		
	}

}