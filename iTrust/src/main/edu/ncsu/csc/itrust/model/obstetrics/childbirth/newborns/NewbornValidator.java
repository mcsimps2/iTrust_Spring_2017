package edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
			errs.addIfNotNull("No office visit ID specified");
			throw new FormValidationException(errs);
		}
		
		//Make sure the office visit ID corresponds to something in the DB
		try
		{
			OfficeVisit ov = ovsql.getByID(obj.getOfficeVisitID());
			if (ov == null)
			{
				errs.addIfNotNull("Could not find the office visit with the specified office visit ID");
			}
		}
		catch (DBException e)
		{
			errs.addIfNotNull("Could not find the office visit with the specified office visit ID");
		}
		
		//Make sure DOB is a valid date
		if (obj.getDateOfBirth() == null || obj.getDateOfBirth().equals(""))
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
		if (obj.getTimeOfBirth() == null || obj.getTimeOfBirth().equals(""))
		{
			errs.addIfNotNull("No time specified");
		}
		else
		{
			try
			{
				//Format: H:m
				SimpleDateFormat sdf = new SimpleDateFormat("H:m");
				sdf.setLenient(false);
				obj.setTimeOfBirth(sdf.format(sdf.parse(obj.getTimeOfBirth())));
				/* The reason for this last statement
				 * A time could be passed in as xx:yy:zz:tt
				 * This would pass sdf.parse(xx:yy:zz:tt) as it provides an hour and minute (along with extra stuff)
				 * But we want to set the time of birth explicitly to H:m
				 */
				
			}
			catch (ParseException e)
			{
				errs.addIfNotNull("Time must be a real time and in the format H:m (hour 0-23, minute 0-59)");
			}
		}
		
		if (obj.getTimeEstimated() == null)
		{
			errs.addIfNotNull("No time estimated flag specified");
		}
		
		if (obj.getSex() == null)
		{
			errs.addIfNotNull("No sex specified");
		}
		
		if(errs.hasErrors())
		{
			throw new FormValidationException(errs);
		}
	}
	
	public void validateUpdate(Newborn obj) throws FormValidationException
	{
		ErrorList errs = new ErrorList();
		
		if (obj.getOfficeVisitID() == null)
		{
			errs.addIfNotNull("No office visit ID specified");
			throw new FormValidationException(errs);
		}
		
		//Make sure the office visit ID corresponds to something in the DB
		try
		{
			OfficeVisit ov = ovsql.getByID(obj.getOfficeVisitID());
			if (ov == null)
			{
				errs.addIfNotNull("Could not find the office visit with the specified office visit ID");
			}
		}
		catch (DBException e)
		{
			errs.addIfNotNull("Could not find the office visit with the specified office visit ID");
		}
		
		//Make sure DOB is a valid date
		if (obj.getDateOfBirth() != null && !obj.getDateOfBirth().equals(""))
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
		if (obj.getTimeOfBirth() != null && !obj.getTimeOfBirth().equals(""))
		{
			try
			{
				//Format: H:m
				SimpleDateFormat sdf = new SimpleDateFormat("H:m");
				sdf.setLenient(false);
				sdf.parse(obj.getTimeOfBirth());
				obj.setTimeOfBirth(sdf.format(sdf.parse(obj.getTimeOfBirth())));
				/* The reason for this last statement
				 * A time could be passed in as xx:yy:zz:tt
				 * This would pass sdf.parse(xx:yy:zz:tt) as it provides an hour and minute (along with extra stuff)
				 * But we want to set the time of birth explicitly to H:m
				 */
			}
			catch (ParseException e)
			{
				errs.addIfNotNull("Time must be a real time and in the format H:m (hour 0-23, minute 0-59)");
			}
		}
		
		if(errs.hasErrors())
		{
			throw new FormValidationException(errs);
		}
	}
}
