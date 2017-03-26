package edu.ncsu.itrust.model.obstetrics.visit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;

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
				PreparedStatement pstring = conn.prepareStatement("SELECT * FROM obstetricsVisit");
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
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM obstetricsVisit WHERE id="+id);
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
	public ObstetricsVisit getObstetricsVisitByOfficeVisit(long officeVisitID) throws DBException {
		try (Connection conn = ds.getConnection();
				PreparedStatement statement = conn.prepareStatement("SELECT * FROM obstetricsVisit WHERE officeVisitID="+officeVisitID);
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
