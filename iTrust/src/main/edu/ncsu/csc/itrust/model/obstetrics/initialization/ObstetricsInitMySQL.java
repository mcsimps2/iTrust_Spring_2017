package edu.ncsu.csc.itrust.model.obstetrics.initialization;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;


public class ObstetricsInitMySQL implements ObstetricsInitData, Serializable
{
	/**
	 * The default serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/** Loader for DB interaction */
	ObstetricsInitSQLLoader loader;
	/** The datasource to use for connection to the DB */
	DataSource ds;
	/** The validator to use for validating the objects */
	ObstetricsInitValidator validator;
	
	/**
	 * Default constructor that creates its own DataSource
	 * @throws DBException if there is a context lookup naming exception
	 */
	public ObstetricsInitMySQL() throws DBException {
		loader = new ObstetricsInitSQLLoader();
		try {
			Context ctx = new InitialContext();
			this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
		} catch (NamingException e) {
			throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
		}
		validator = new ObstetricsInitValidator(this.ds);
	}
	/**
	 * Constructor for testing since we need a datasource to be passed in
	 * for testing
	 * @param ds
	 */
	public ObstetricsInitMySQL(DataSource ds)
	{
		loader = new ObstetricsInitSQLLoader();
		this.ds = ds;
		validator = new ObstetricsInitValidator(this.ds);
	}

	@Override
	public List<ObstetricsInit> getRecords(long pid) throws DBException {
		Connection conn = null;
		PreparedStatement pstring = null;
		ResultSet results = null;
		try {
			conn = ds.getConnection();
			pstring = conn.prepareStatement("SELECT * FROM obstetricsInit WHERE pid=" + pid + ";");
			results = pstring.executeQuery();
			final List<ObstetricsInit> list = loader.loadList(results);
			return list;
		} catch (SQLException e) {
			throw new DBException(e);
		} finally {
			try {
				if (results != null) {
					results.close();
				}
			} catch (SQLException e) {
				throw new DBException(e);
			} finally {
				DBUtil.closeConnection(conn, pstring);
			}
		}
	}

	@Override
	public boolean add(ObstetricsInit oi) throws DBException {
		Connection conn = null;
		PreparedStatement pstring = null;
		try
		{
			validator.validate(oi);
		}
		catch (FormValidationException e)
		{
			throw new DBException(new SQLException(e));
		}
		try
		{
			conn = ds.getConnection();
			pstring = loader.loadParameters(conn, pstring, oi, true);
			int rowsAffected = pstring.executeUpdate();
			if (rowsAffected != 0) {
				return true;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, pstring);
		}
		return false;
	}
	
	@Override
	public List<ObstetricsInit> getAll() throws DBException {
		Connection conn = null;
		PreparedStatement pstring = null;
		ResultSet results = null;
		try {
			conn = ds.getConnection();
			pstring = conn.prepareStatement("SELECT * FROM obstetricsInit;");
			results = pstring.executeQuery();
			final List<ObstetricsInit> list = loader.loadList(results);
			return list;
		} catch (SQLException e) {
			throw new DBException(e);
		} finally {
			try {
				if (results != null) {
					results.close();
				}
			} catch (SQLException e) {
				throw new DBException(e);
			} finally {
				DBUtil.closeConnection(conn, pstring);
			}
		}
	}
	
	@Override
	public ObstetricsInit getByID(long id) throws DBException {
		Connection conn = null;
		PreparedStatement pstring = null;
		ResultSet results = null;
		try {
			conn = ds.getConnection();
			pstring = conn.prepareStatement("SELECT * FROM obstetricsInit WHERE id=" + id + ";");
			results = pstring.executeQuery();
			final List<ObstetricsInit> list = loader.loadList(results);
			if (list.size() != 0)
			{
				return list.get(0);
			}
			return null;
		} catch (SQLException e) {
			throw new DBException(e);
		} finally {
			try {
				if (results != null) {
					results.close();
				}
			} catch (SQLException e) {
				throw new DBException(e);
			} finally {
				DBUtil.closeConnection(conn, pstring);
			}
		}
	}
	
	@Override
	public boolean update(ObstetricsInit oi) throws DBException {
		throw new IllegalStateException("Unimplemented");
	}
	
	@Override
	public int addAndReturnID(ObstetricsInit oi) throws DBException, FormValidationException
	{
		Connection conn = null;
		PreparedStatement pstring = null;
		
		validator.validate(oi);
		
		try
		{
			conn = ds.getConnection();
			pstring = loader.loadParameters(conn, pstring, oi, true);
			int rowsAffected = pstring.executeUpdate();
			if (rowsAffected == 0)
			{
				return -1;
			}
			pstring = conn.prepareStatement("SELECT LAST_INSERT_ID() FROM obstetricsInit;");
			ResultSet rs = pstring.executeQuery();
			if (rs.next())
			{
				return rs.getInt(1);
			}
			else
			{
				return -1;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, pstring);
		}
	}

}
