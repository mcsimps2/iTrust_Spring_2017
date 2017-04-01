package edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.POJOValidator;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisitMySQL;

public class NewbornValidator extends POJOValidator<Newborn>
{
	private DataSource ds;
	private OfficeVisitMySQL ovsql;
	
	/**
	 * Constructor with no parameters
	 * Automatically sets the datasource
	 */
	public NewbornValidator() throws DBException
	{
		try {
			Context ctx = new InitialContext();
			this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
			ovsql = new OfficeVisitMySQL(this.ds);
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
	public NewbornValidator(DataSource ds)
	{
		this.ds = ds;
		ovsql = new OfficeVisitMySQL(this.ds);
	}

	@Override
	public void validate(Newborn obj) throws FormValidationException
	{
		ErrorList errs = new ErrorList();
		
		if (obj.getOfficeVisitID() == null)
		{
			errs.addIfNotNull("Null office visit ID");
			throw new FormValidationException(errs);
		}
		
		//Make sure the office visit ID corresponds to something in the DB
		try
		{
			OfficeVisit ov = ovsql.getByID(obj.getOfficeVisitID());
			if (ov == null)
			{
				errs.addIfNotNull("Could not find the patient with the specified PID");
				throw new FormValidationException(errs);
			}
		}
		catch (DBException e)
		{
			errs.addIfNotNull("Could not find the patient with the specified PID");
			throw new FormValidationException(errs);
		}
		
		//Make sure DOB is a valid date
		if (obj.getDateOfBirth() == null)
		{
			errs.addIfNotNull("No date specified");
		}
		else
		{
			try
			{
				Calendar cal = Calendar.getInstance();
				cal.setLenient(false);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
				sdf.setLenient(false);
				cal.setTime(sdf.parse(obj.getDateOfBirth()));
				cal.getTime();
			}
			catch (Exception e)
			{
				errs.addIfNotNull("Date must be a real date in the format yyyy-M-d");
			}
		}
		
		//Make sure time is valid
		if (obj.getTimeOfBirth() == null)
		{
			errs.addIfNotNull("No time specified");
		}
		else
		{
			try
			{
				//Format: H:m:s
				SimpleDateFormat sdf = new SimpleDateFormat("H:m:s");
				sdf.setLenient(false);
				sdf.parse(obj.getTimeOfBirth());
			}
			catch (ParseException e)
			{
				errs.addIfNotNull("Time must be a real time and in the format H:m:s (hour 0-23, minute 0-59, second 0-59)");
			}
		}
		
		if (obj.getTimeEstimated() == null)
		{
			errs.addIfNotNull("Time estimated is null");
		}
		
		if(errs.hasErrors())
		{
			throw new FormValidationException(errs);
		}
		
		//Sex is set by default, has to be an enum, no check needed
	}
}
