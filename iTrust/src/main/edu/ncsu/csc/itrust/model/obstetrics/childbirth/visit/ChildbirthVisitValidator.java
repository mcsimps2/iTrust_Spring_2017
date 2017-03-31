package edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit;

import java.sql.SQLException;

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

public class ChildbirthVisitValidator extends POJOValidator<ChildbirthVisit>
{
	private DataSource ds;
	private OfficeVisitMySQL ovsql;
	
	/**
	 * Constructor with no parameters
	 * Automatically sets the datasource
	 */
	public ChildbirthVisitValidator() throws DBException
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
	public ChildbirthVisitValidator(DataSource ds)
	{
		this.ds = ds;
		ovsql = new OfficeVisitMySQL(this.ds);
	}
	
	@Override
	public void validate(ChildbirthVisit obj) throws FormValidationException
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
		
		//DeliveryMethod is set by default, can't be invalid
		
		//Check other fields for non-null and non-negativity
		if (obj.getPitocin() == null)
		{
			errs.addIfNotNull("Pitocin value cannot be null");
		}
		else if (obj.getPitocin() < 0)
		{
			errs.addIfNotNull("Pitocin must be a non-negative integer");
		}
		if (obj.getNitrousOxide() == null)
		{
			errs.addIfNotNull("Nitrous Oxide value cannot be null");
		}
		else if (obj.getNitrousOxide() < 0)
		{
			errs.addIfNotNull("Nitrous oxide must be a non-negative integer");
		}
		if (obj.getPethidine() == null)
		{
			errs.addIfNotNull("Pethidine value cannot be null");
		}
		else if (obj.getPethidine() < 0)
		{
			errs.addIfNotNull("Pethidine must be a non-negative integer");
		}
		if (obj.getEpiduralAnaesthesia() == null)
		{
			errs.addIfNotNull("Epidural anaesthesia value cannot be null");
		}
		else if (obj.getEpiduralAnaesthesia() < 0)
		{
			errs.addIfNotNull("Epidural anaesthesia must be a non-negative integer");
		}
		if (obj.getMagnesiumSulfide() == null)
		{
			errs.addIfNotNull("Magnesium sulfide value cannot be null");
		}
		else if (obj.getMagnesiumSulfide() < 0)
		{
			errs.addIfNotNull("Magnesium sulfide must be a non-negative integer");
		}
		
		if (errs.hasErrors())
		{
			throw new FormValidationException(errs);
		}
		
	}
	
}
