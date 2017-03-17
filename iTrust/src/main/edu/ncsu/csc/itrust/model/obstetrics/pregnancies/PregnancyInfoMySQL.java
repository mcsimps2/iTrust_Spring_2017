package edu.ncsu.csc.itrust.model.obstetrics.pregnancies;

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
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInit;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInitMySQL;

public class PregnancyInfoMySQL implements PregnancyInfoData, Serializable
{
	/**
	 * Default UID
	 */
	private static final long serialVersionUID = 1L;
	/** Loader for DB interaction */
	PregnancyInfoSQLLoader loader;
	/** Datasource to use for connection to DB */
	DataSource ds;
	/** Validator to validate objects being inserted */
	PregnancyInfoValidator validator;
	/** for DB interaction with obstetrics */
	ObstetricsInitMySQL oisql;
	
	/**
	 * Default constructor that creates its own DataSource
	 * @throws DBException if there is a context lookup naming exception
	 */
	public PregnancyInfoMySQL() throws DBException {
		loader = new PregnancyInfoSQLLoader();
		try {
			Context ctx = new InitialContext();
			this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
		} catch (NamingException e) {
			throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
		}
		validator = new PregnancyInfoValidator(this.ds);
		oisql = new ObstetricsInitMySQL(this.ds);
	}
	
	/**
	 * Constructor for testing since we need a datasource to be passed in
	 * for testing
	 * @param ds
	 */
	public PregnancyInfoMySQL(DataSource ds)
	{
		loader = new PregnancyInfoSQLLoader();
		this.ds = ds;
		validator = new PregnancyInfoValidator(this.ds);
		oisql = new ObstetricsInitMySQL(this.ds);
	}

	@Override
	public List<PregnancyInfo> getRecords(long pid) throws DBException {
		Connection conn = null;
		PreparedStatement pstring = null;
		ResultSet results = null;
		try {
			conn = ds.getConnection();
			pstring = conn.prepareStatement("SELECT * FROM priorPregnancies WHERE pid=" + pid);
			results = pstring.executeQuery();
			final List<PregnancyInfo> list = loader.loadList(results);
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
	public List<PregnancyInfo> getNewRecordsAddedDuringInit(int obstetricsInitID) throws DBException {
		Connection conn = null;
		PreparedStatement pstring = null;
		ResultSet results = null;
		try {
			conn = ds.getConnection();
			pstring = conn.prepareStatement("SELECT * FROM priorPregnancies WHERE obstetricsInitID=" + obstetricsInitID);
			results = pstring.executeQuery();
			final List<PregnancyInfo> list = loader.loadList(results);
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
	public boolean add(PregnancyInfo pi) throws DBException {
		Connection conn = null;
		PreparedStatement pstring = null;
		try
		{
			validator.validate(pi);
		}
		catch (FormValidationException e)
		{
			throw new DBException(new SQLException(e));
		}
		try
		{
			conn = ds.getConnection();
			pstring = loader.loadParameters(conn, pstring, pi, true);
			int results = pstring.executeUpdate();
			if (results != 0) {
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
	public PregnancyInfo getRecordByID(int recordID) throws DBException {
		Connection conn = null;
		PreparedStatement pstring = null;
		ResultSet results = null;
		try {
			conn = ds.getConnection();
			pstring = conn.prepareStatement("SELECT * FROM priorPregnancies WHERE id=" + recordID);
			results = pstring.executeQuery();
			final List<PregnancyInfo> list = loader.loadList(results);
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
	public List<PregnancyInfo> getRecordsFromInit(int obstetricsInitID) throws DBException {
		//First, form a list from all pregnancy records added during the init
		List<PregnancyInfo> newRecords = getNewRecordsAddedDuringInit(obstetricsInitID);
		//Record what the pid was
		ObstetricsInit oiRecord = oisql.getRecordByID(obstetricsInitID);
		if (oiRecord == null)
		{
			throw new IllegalArgumentException("Invalid obstetricsInitID. Doesn't correspond to anything found in the Database");
		}
		long pid = oiRecord.getPid();
		//Now, get all the previous pregnancy records
		Connection conn = null;
		PreparedStatement pstring = null;
		ResultSet results = null;
		try {
			conn = ds.getConnection();
			pstring = conn.prepareStatement("SELECT * FROM priorPregnancies WHERE pid=" + pid + " AND obstetricInitID<" + obstetricsInitID);
			results = pstring.executeQuery();
			List<PregnancyInfo> list = loader.loadList(results);
			list.addAll(newRecords);
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

}
