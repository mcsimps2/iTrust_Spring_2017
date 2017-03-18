package edu.ncsu.csc.itrust.model.obstetrics.pregnancies;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.POJOValidator;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInitMySQL;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.PatientLoader;

public class PregnancyInfoValidator extends POJOValidator<PregnancyInfo>
{
	private DataSource ds;
	private ObstetricsInitMySQL oisql;
	
	/**
	 * Constructor with no parameters
	 * Automatically sets the datasource
	 */
	public PregnancyInfoValidator() throws DBException
	{
		try {
			Context ctx = new InitialContext();
			this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
			oisql = new ObstetricsInitMySQL(this.ds);
		} catch (NamingException e) {
			throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
		}
	}
	
	/**
	 * Constructor that sets the datasource
	 * Useful for testing purposes since it can specify a datasource
	 * @param ds the datasource used by the class
	 */
	public PregnancyInfoValidator (DataSource ds) {
		this.ds = ds;
		oisql = new ObstetricsInitMySQL(this.ds);
	}

	@Override
	public void validate(PregnancyInfo obj) throws FormValidationException {
		ErrorList errs = new ErrorList();
		
		//Verify the PID exists in the database
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
		
		//Make sure that the obstetricsInitID actually refers to a real record in the obstetricsInit DB
		try
		{
			if (oisql.getByID(obj.getObstetricsInitID()) == null)
			{
				errs.addIfNotNull("The given ObstetricsInitID does not correspond to an actual entry in the database");
				throw new FormValidationException(errs);
			}
		} catch (DBException e)
		{
			errs.addIfNotNull("Unable to access the database");
			throw new FormValidationException(errs);
		}
		
		//Verify the year
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		if (obj.getYearOfConception() > year)
		{
			errs.addIfNotNull("Year is in the future");
		}
		
		if (Math.abs(year - obj.getYearOfConception()) > 175)
		{
			errs.addIfNotNull("Difference between current year and year of conception is too great");
		}
		
		//Verify non-negativity
		if (obj.getNumDaysPregnant() < 0)
		{
			errs.addIfNotNull("Negative number of days/weeks pregnant");
		}
		
		if (obj.getNumHoursInLabor() < 0)
		{
			errs.addIfNotNull("Negative hours in labor");
		}
		
		if (obj.getWeightGain() < 0)
		{
			errs.addIfNotNull("Negative weight gain");
		}
		
		if (obj.getDeliveryType() == null)
		{
			errs.addIfNotNull("Empty delivery type");
		}
		
		if (obj.getMultiplicity() < 1)
		{
			errs.addIfNotNull("Multiplicity must be at least 1");
		}
		
		if (errs.hasErrors())
		{
			throw new FormValidationException(errs);
		}
	}
}
