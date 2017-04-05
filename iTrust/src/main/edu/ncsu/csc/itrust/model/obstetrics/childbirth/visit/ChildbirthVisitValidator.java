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
			errs.addIfNotNull("No office visit ID specified");
			throw new FormValidationException(errs);
		}
		
		//Make sure the office visit ID corresponds to something in the DB
		try
		{
			OfficeVisit ov = ovsql.getByID(obj.getOfficeVisitID());
			if (ov == null)
			{
				errs.addIfNotNull("Could not find the office visit with the specified ID");
				throw new FormValidationException(errs);
			}
		}
		catch (DBException e)
		{
			errs.addIfNotNull("Could not find the patient with the specified PID");
			throw new FormValidationException(errs);
		}
		
		//DeliveryMethod and VisitType is set by default, can't be invalid
		
		//Check other fields for non-null and non-negativity
		if (obj.getPitocin() == null)
		{
			errs.addIfNotNull("No pitocin value specified");
		}
		else if (obj.getPitocin() < 0)
		{
			errs.addIfNotNull("Pitocin must be a non-negative integer");
		}
		if (obj.getNitrousOxide() == null)
		{
			errs.addIfNotNull("No nitrous oxide value specified");
		}
		else if (obj.getNitrousOxide() < 0)
		{
			errs.addIfNotNull("Nitrous oxide must be a non-negative integer");
		}
		if (obj.getPethidine() == null)
		{
			errs.addIfNotNull("No pethidine value specified");
		}
		else if (obj.getPethidine() < 0)
		{
			errs.addIfNotNull("Pethidine must be a non-negative integer");
		}
		if (obj.getEpiduralAnaesthesia() == null)
		{
			errs.addIfNotNull("No epidural anethesia value specified");
		}
		else if (obj.getEpiduralAnaesthesia() < 0)
		{
			errs.addIfNotNull("Epidural anaesthesia must be a non-negative integer");
		}
		if (obj.getMagnesiumSulfate() == null)
		{
			errs.addIfNotNull("No magnesium sulfate value specified");
		}
		else if (obj.getMagnesiumSulfate() < 0)
		{
			errs.addIfNotNull("Magnesium sulfate must be a non-negative integer");
		}
		
		if (errs.hasErrors())
		{
			throw new FormValidationException(errs);
		}
	}
	
	public void validateUpdate(ChildbirthVisit obj) throws FormValidationException
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
				errs.addIfNotNull("Could not find the office visit with the specified ID");
				throw new FormValidationException(errs);
			}
		}
		catch (DBException e)
		{
			errs.addIfNotNull("Could not find the patient with the specified PID");
			throw new FormValidationException(errs);
		}
		
		//Check other fields for non-negativity
		if (obj.getPitocin() != null && obj.getPitocin() < 0)
		{
			errs.addIfNotNull("Pitocin must be a non-negative integer");
		}
		if (obj.getNitrousOxide() != null && obj.getNitrousOxide() < 0)
		{
			errs.addIfNotNull("Nitrous Oxide must be a non-negative integer");
		}
		if (obj.getPethidine() != null && obj.getPethidine() < 0)
		{
			errs.addIfNotNull("Pethidine must be a non-negative integer");
		}
		if (obj.getEpiduralAnaesthesia() != null && obj.getEpiduralAnaesthesia() < 0)
		{
			errs.addIfNotNull("Epidural anaesthesia must be a non-negative integer");
		}
		if (obj.getMagnesiumSulfate() != null && obj.getMagnesiumSulfate() < 0)
		{
			errs.addIfNotNull("Magnesium sulfate must be a non-negative integer");
		}
		
		if (errs.hasErrors())
		{
			throw new FormValidationException(errs);
		}
	}
}