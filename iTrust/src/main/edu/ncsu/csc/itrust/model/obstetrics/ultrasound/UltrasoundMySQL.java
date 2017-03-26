package edu.ncsu.csc.itrust.model.obstetrics.ultrasound;

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
 * Object used to access, modify, and delete ultrasounds
 * @author jcgonzal
 */
public class UltrasoundMySQL implements UltrasoundData {

	/** The sql loader used to prepare the statements */
	UltrasoundSQLLoader loader;
	
	/** The validator */
	UltrasoundValidator validator;
	
	/** Datasource used to access the db */
	DataSource ds;
	
	/**
	 * Default constructor
	 * @throws DBException
	 */
	public UltrasoundMySQL() throws DBException {
		loader = new UltrasoundSQLLoader();
		try {
			Context ctx = new InitialContext();
			this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
		} catch (NamingException e) {
			throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
		}
		validator = new UltrasoundValidator();
	}
	
	/**
	 * Constructor for testing
	 * @param ds testing data source
	 */
	public UltrasoundMySQL(DataSource ds) {
		loader = new UltrasoundSQLLoader();
		this.ds = ds;
		validator = new UltrasoundValidator();
	}
	
	@Override
	public List<Ultrasound> getAll() throws DBException {
		try (Connection conn = ds.getConnection();
			PreparedStatement pstring = conn.prepareStatement("SELECT * FROM ultrasound;");
			ResultSet results = pstring.executeQuery()) {
			List<Ultrasound> list = loader.loadList(results);
			return list;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	@Override
	public Ultrasound getByID(long id) throws DBException {
		try (Connection conn = ds.getConnection();
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM ultrasound WHERE id="+id+";");
			ResultSet resultSet = statement.executeQuery()) {
			List<Ultrasound> list = loader.loadList(resultSet);
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
	public boolean add(Ultrasound addObj) throws FormValidationException, DBException {
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
	public boolean update(Ultrasound updateObj) throws DBException, FormValidationException {
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
	public Ultrasound getByOfficeVisit(long officeVisitID) throws DBException {
		try (Connection conn = ds.getConnection();
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM ultrasound WHERE officeVisitID="+officeVisitID);) {
			ResultSet resultSet = statement.executeQuery();
			List<Ultrasound> list = loader.loadList(resultSet);
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
	public boolean delete(long id) throws DBException {
		try (Connection conn = ds.getConnection();
            PreparedStatement pstring = loader.loadDelete(conn, id);) {
            return pstring.executeUpdate() > 0;
        } catch (SQLException e) {
        	throw new DBException(e);
        }
	}

}
