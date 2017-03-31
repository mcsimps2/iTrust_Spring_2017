package edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns;

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

public class NewbornMySQL implements NewbornData
{

	/** The sql loader used to prepare the statements */
	NewbornSQLLoader loader;
	
	/** The validator */
	NewbornValidator validator;
	
	/** Datasource to access the db */
	private DataSource ds;
	
	/**
	 * Constructor of ObstetricsVisitMySQL instance.
	 * 
	 * @throws DBException when data source cannot be created from the given context names
	 */
	public NewbornMySQL() throws DBException {
		loader = new NewbornSQLLoader();
		try {
			Context ctx = new InitialContext();
			this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
		} catch (NamingException e) {
			throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
		}
		validator = new NewbornValidator();
	}
	
	/**
	 * Constructor for testing.
	 * 
	 * @param ds testing data source
	 */
	public NewbornMySQL(DataSource ds) {
		loader = new NewbornSQLLoader();
		this.ds = ds;
		validator = new NewbornValidator(this.ds);
	}
	
	@Override
	public List<Newborn> getAll() throws DBException
	{
		try (Connection conn = ds.getConnection();
				PreparedStatement pstring = conn.prepareStatement("SELECT * FROM childbirthNewborns;");
				ResultSet results = pstring.executeQuery()) {
			List<Newborn> list = loader.loadList(results);
			return list;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	@Override
	public Newborn getByID(long id) throws DBException
	{
		try (Connection conn = ds.getConnection();
				PreparedStatement statement = conn.prepareStatement("SELECT * FROM childbirthNewborns WHERE id="+id+";");
				ResultSet resultSet = statement.executeQuery()) {
				List<Newborn> list = loader.loadList(resultSet);
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
	public boolean add(Newborn addObj) throws FormValidationException, DBException
	{
		try {
			validator.validate(addObj);
		} catch (FormValidationException e1) {
			throw new DBException(new SQLException(e1.getMessage()));
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
	public boolean update(Newborn updateObj) throws DBException, FormValidationException
	{
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
	public List<Newborn> getByOfficeVisit(long officeVisitID) throws DBException
	{
		try (Connection conn = ds.getConnection();
				PreparedStatement statement = conn.prepareStatement("SELECT * FROM childbirthNewborns WHERE officeVisitID="+officeVisitID+";");
				ResultSet resultSet = statement.executeQuery()) {
				List<Newborn> list = loader.loadList(resultSet);
				return list;
			} catch (SQLException e) {
				throw new DBException(e);
			}
	}

}
