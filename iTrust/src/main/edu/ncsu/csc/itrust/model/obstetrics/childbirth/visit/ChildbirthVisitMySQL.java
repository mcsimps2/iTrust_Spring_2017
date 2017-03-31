package edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ChildbirthVisitMySQL implements ChildbirthVisitData
{
	private ChildbirthVisitSQLLoader loader;
	private ChildbirthVisitValidator validator;
	private DataSource ds;
	
	public ChildbirthVisitMySQL() throws DBException
	{
		loader = new ChildbirthVisitSQLLoader();
		try {
			Context ctx = new InitialContext();
			this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
		} catch (NamingException e) {
			throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
		}
		validator = new ChildbirthVisitValidator();
	}
	
	public ChildbirthVisitMySQL(DataSource ds)
	{
		loader = new ChildbirthVisitSQLLoader();
		this.ds = ds;
		validator = new ChildbirthVisitValidator(this.ds);
	}
	
	@Override
	public List<ChildbirthVisit> getAll() throws DBException
	{
		Connection conn = null;
		PreparedStatement pstring = null;
		ResultSet results = null;
		try {
			conn = ds.getConnection();
			pstring = conn.prepareStatement("SELECT * FROM childbirthVisits;");
			results = pstring.executeQuery();
			final List<ChildbirthVisit> list = loader.loadList(results);
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
	public ChildbirthVisit getByID(long id) throws DBException
	{
		Connection conn = null;
		PreparedStatement pstring = null;
		ResultSet results = null;
		try {
			conn = ds.getConnection();
			pstring = conn.prepareStatement("SELECT * FROM childbirthVisits WHERE id=" + id + ";");
			results = pstring.executeQuery();
			final List<ChildbirthVisit> list = loader.loadList(results);
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
	public boolean add(ChildbirthVisit addObj) throws FormValidationException, DBException
	{
		Connection conn = null;
		PreparedStatement pstring = null;
		try
		{
			validator.validate(addObj);
		}
		catch (FormValidationException e)
		{
			throw new DBException(new SQLException(e));
		}
		try
		{
			conn = ds.getConnection();
			pstring = loader.loadParameters(conn, pstring, addObj, true);
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
	public boolean update(ChildbirthVisit updateObj) throws DBException, FormValidationException
	{
		Connection conn = null;
		PreparedStatement pstring = null;
		try
		{
			validator.validate(updateObj);
		}
		catch (FormValidationException e)
		{
			throw new DBException(new SQLException(e));
		}
		try
		{
			conn = ds.getConnection();
			pstring = loader.loadParameters(conn, pstring, updateObj, false);
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
	public ChildbirthVisit getByOfficeVisit(long officeVisitID) throws DBException
	{
		Connection conn = null;
		PreparedStatement pstring = null;
		ResultSet results = null;
		try {
			conn = ds.getConnection();
			pstring = conn.prepareStatement("SELECT * FROM childbirthVisits WHERE officeVisitID=" + officeVisitID + ";");
			results = pstring.executeQuery();
			final List<ChildbirthVisit> list = loader.loadList(results);
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

}
