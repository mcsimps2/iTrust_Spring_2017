package edu.ncsu.csc.itrust.model.obstetrics.visit;

import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.POJOValidator;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInit;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInitMySQL;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisitMySQL;

/**
 * Used to validate obstetrics visit object
 * @author jcgonzal
 */
public class ObstetricsVisitValidator extends POJOValidator<ObstetricsVisit> {
	
	private DataSource ds;
	private OfficeVisitMySQL ovsql;
	private ObstetricsInitMySQL oisql;
	
	/**
	 * Constructor with no parameters
	 * Automatically sets the datasource
	 */
	public ObstetricsVisitValidator() throws DBException
	{
		try {
			Context ctx = new InitialContext();
			this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
			ovsql = new OfficeVisitMySQL(this.ds);
			oisql = new ObstetricsInitMySQL(this.ds);
		} catch (NamingException e) {
			throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
		}
	}
	
	/**
	 * Constructor that sets the datasource
	 * Useful for testing purposes since it can specify a datasource
	 * @param ds the datasource used by the class
	 * @throws DBException 
	 */
	public ObstetricsVisitValidator(DataSource ds)
	{
		this.ds = ds;
		ovsql = new OfficeVisitMySQL(this.ds);
		oisql = new ObstetricsInitMySQL(this.ds);
	}

	@Override
	public void validate(ObstetricsVisit obj) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		
		// Check that officeVisitID is specified
		if (obj.getOfficeVisitID() == null)
		{
			errorList.addIfNotNull("No office visit ID specified");
			throw new FormValidationException(errorList);
		}
		
		//Make sure the office visit ID corresponds to something in the DB
		try
		{
			OfficeVisit ov = ovsql.getByID(obj.getOfficeVisitID());
			if (ov == null)
			{
				errorList.addIfNotNull("Could not find the office visit with the specified ID");
				throw new FormValidationException(errorList);
			}
		}
		catch (DBException e)
		{
			errorList.addIfNotNull("Could not find the patient with the specified PID");
			throw new FormValidationException(errorList);
		}
		
		// Check that the obstetricsInitID is specified
		if (obj.getObstetricsInitID() == null)
		{
			errorList.addIfNotNull("No obstetrics init ID specified");
			throw new FormValidationException(errorList);
		}
		
		//Make sure the obstetrics init ID corresponds to something in the DB
		try
		{
			ObstetricsInit oi = oisql.getByID(obj.getObstetricsInitID());
			if (oi == null)
			{
				errorList.addIfNotNull("Could not find the obstetrics init with the specified ID");
				throw new FormValidationException(errorList);
			}
		}
		catch (DBException e)
		{
			errorList.addIfNotNull("Could not find the patient with the specified PID");
			throw new FormValidationException(errorList);
		}
		
		if (obj.getWeeksPregnant() == null)
			errorList.addIfNotNull("Weeks pregnant is required");
		
		else if (obj.getWeeksPregnant() < 0 || obj.getWeeksPregnant() > 48)
			errorList.addIfNotNull("Weeks pregnant must be between 0 and 48");
		
		if (obj.getMultiplicity() == null)
			errorList.addIfNotNull("Multiplicity is required");
		
		else if (obj.getMultiplicity() <= 0)
			errorList.addIfNotNull("Multiplicity must be greater than zero");
		
		if (obj.getFhr() == null)
			errorList.addIfNotNull("Fetal heart rate is required");
		
		else if (obj.getFhr() <= 0)
			errorList.addIfNotNull("Fetal heart rate must be greater than zero");
		
		if (obj.isLowLyingPlacentaObserved() == null)
			errorList.addIfNotNull("Low lying placenta observation is required");
		
		if ( errorList.hasErrors() )
			throw new FormValidationException(errorList);
	}
	
	/**
	 * Validation for updating the obstetrics visit object.
	 * The multiplicity and fhr fields are allowed to be null.
	 * @param obj
	 * @throws FormValidationException
	 */
	public void validateUpdate(ObstetricsVisit obj) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		
		// Check that officeVisitID is specified
		if (obj.getOfficeVisitID() == null)
		{
			errorList.addIfNotNull("No office visit ID specified");
			throw new FormValidationException(errorList);
		}
		
		//Make sure the office visit ID corresponds to something in the DB
		try
		{
			OfficeVisit ov = ovsql.getByID(obj.getOfficeVisitID());
			if (ov == null)
			{
				errorList.addIfNotNull("Could not find the office visit with the specified ID");
				throw new FormValidationException(errorList);
			}
		}
		catch (DBException e)
		{
			errorList.addIfNotNull("Could not find the patient with the specified PID");
			throw new FormValidationException(errorList);
		}
		
		// Check that the obstetricsInitID is specified
		if (obj.getObstetricsInitID() == null)
		{
			errorList.addIfNotNull("No obstetrics init ID specified");
			throw new FormValidationException(errorList);
		}
		
		//Make sure the obstetrics init ID corresponds to something in the DB
		try
		{
			ObstetricsInit oi = oisql.getByID(obj.getObstetricsInitID());
			if (oi == null)
			{
				errorList.addIfNotNull("Could not find the obstetrics init with the specified ID");
				throw new FormValidationException(errorList);
			}
		}
		catch (DBException e)
		{
			errorList.addIfNotNull("Could not find the patient with the specified PID");
			throw new FormValidationException(errorList);
		}
		
		if (obj.getWeeksPregnant() == null)
			errorList.addIfNotNull("Weeks pregnant is required");
		
		else if (obj.getWeeksPregnant() < 0 || obj.getWeeksPregnant() > 48)
			errorList.addIfNotNull("Weeks pregnant must be between 0 and 48");
		
		if (obj.getMultiplicity() != null && obj.getMultiplicity() <= 0)
			errorList.addIfNotNull("Multiplicity must be greater than zero");
		
		if (obj.getFhr() != null && obj.getFhr() <= 0)
			errorList.addIfNotNull("Fetal heart rate must be greater than zero");
		
		if ( errorList.hasErrors() )
			throw new FormValidationException(errorList);
	}
}
