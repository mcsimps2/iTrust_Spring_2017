package edu.ncsu.csc.itrust.model.obstetrics.visit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInit;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInitMySQL;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisitMySQL;

/**
 * Object used to access the ObstetricsVisit database
 * @author jcgonzal
 */
public class ObstetricsVisitMySQL implements ObstetricsVisitData {
	
	/** The sql loader used to prepare the statements */
	ObstetricsVisitSQLLoader loader;
	
	/** The validator */
	ObstetricsVisitValidator validator;
	
	/** Datasource to access the db */
	private DataSource ds;
	
	/**
	 * Constructor of ObstetricsVisitMySQL instance.
	 * 
	 * @throws DBException when data source cannot be created from the given context names
	 */
	public ObstetricsVisitMySQL() throws DBException {
		loader = new ObstetricsVisitSQLLoader();
		try {
			Context ctx = new InitialContext();
			this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
		} catch (NamingException e) {
			throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
		}
		validator = new ObstetricsVisitValidator();
	}
	
	/**
	 * Constructor for testing.
	 * 
	 * @param ds testing data source
	 */
	public ObstetricsVisitMySQL(DataSource ds) {
		loader = new ObstetricsVisitSQLLoader();
		this.ds = ds;
		validator = new ObstetricsVisitValidator();
	}

	@Override
	public List<ObstetricsVisit> getAll() throws DBException {
		try (Connection conn = ds.getConnection();
				PreparedStatement pstring = conn.prepareStatement("SELECT * FROM obstetricsVisit;");
				ResultSet results = pstring.executeQuery()) {
			List<ObstetricsVisit> list = loader.loadList(results);
			return list;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	@Override
	public ObstetricsVisit getByID(long id) throws DBException {
		try (Connection conn = ds.getConnection();
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM obstetricsVisit WHERE id="+id+";");
			ResultSet resultSet = statement.executeQuery()) {
			List<ObstetricsVisit> list = loader.loadList(resultSet);
			if (list.size() > 0) {
				return list.get(0);
			} else {
				return null;
			}
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	@Override
	public boolean add(ObstetricsVisit addObj) throws FormValidationException, DBException {
		
		//Calculate the weeks and days pregnant at time of birth
		OfficeVisitMySQL officeVisitMySQL = new OfficeVisitMySQL();
		OfficeVisit visit = officeVisitMySQL.getByID(addObj.getOfficeVisitID());
		ObstetricsInitMySQL obstetricsInitMySQL = new ObstetricsInitMySQL();
		List<ObstetricsInit> list = obstetricsInitMySQL.getRecords(visit.getPatientMID());
		if (list.isEmpty())
			throw new FormValidationException("There is not an obstetrics initialization within the last 49 weeks.");
		ObstetricsInit init = list.get(0);
		Date lmp = ObstetricsInit.stringToJavaDate(init.getLMP());
		
		int weeks = 0;
		int days = 0;
		
		Calendar lmpCal = Calendar.getInstance();
		lmpCal.setTime(lmp);
		Calendar cal = Calendar.getInstance();
		
		while (cal.after(lmpCal)) {
			cal.add(Calendar.WEEK_OF_YEAR, -1);
			weeks++;
		}
		if (!cal.equals(lmpCal)) {
			cal.add(Calendar.WEEK_OF_YEAR, 1);
			weeks--;
			while (cal.after(lmpCal)) {
				cal.add(Calendar.DAY_OF_WEEK, -1);
				days++;
			}
		}
		
		if (weeks > 49 || (weeks == 49 && days > 0)) {
			throw new FormValidationException("There is not an obstetrics initialization within the last 49 weeks.");
		}
		
		addObj.setWeeksPregnant(weeks);
		addObj.setDaysPregnant(days);
		
		try {
			validator.validate(addObj);
		} catch (FormValidationException e1) {
			throw new DBException(new SQLException(e1));
		}
		try (Connection conn = ds.getConnection();
			PreparedStatement statement = loader.loadParameters(conn, null, addObj, true);) {
			int results = statement.executeUpdate();
			return results == 1;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	@Override
	public boolean update(ObstetricsVisit updateObj) throws DBException, FormValidationException {
		try {
			validator.validate(updateObj);
		} catch (FormValidationException e1) {
			throw new DBException(new SQLException(e1.getMessage()));
		}

		try (Connection conn = ds.getConnection();
			PreparedStatement statement = loader.loadParameters(conn, null, updateObj, false);) {
			int results = statement.executeUpdate();
			return results == 1;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	@Override
	public ObstetricsVisit getByOfficeVisit(long officeVisitID) throws DBException {
		try (Connection conn = ds.getConnection();
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM obstetricsVisit WHERE officeVisitID="+officeVisitID+";");
			ResultSet resultSet = statement.executeQuery()) {
			List<ObstetricsVisit> list = loader.loadList(resultSet);
			if (list.size() > 0) {
				return list.get(0);
			} else {
				return null;
			}
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

}
