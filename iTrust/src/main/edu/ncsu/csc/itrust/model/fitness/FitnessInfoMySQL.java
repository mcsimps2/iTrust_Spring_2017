package edu.ncsu.csc.itrust.model.fitness;

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

/**
 * Class responsible for adding, updating, and retrieving fitness data from the database
 * @author mcsimps2
 *
 */
public class FitnessInfoMySQL implements FitnessInfoData, Serializable
{
	/**
	 * The default serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/* Loader for database interaction */
	FitnessInfoSQLLoader loader;
	/* The datasource from which to get the connection to the database */
	DataSource ds; 
	/* The validator to use for validating FitnessInfo objects */
	FitnessInfoValidator validator;
	
	/**
	 * Default constructor that creates its own DataSource
	 * @throws DBException if there is a context lookup naming exception
	 */
	public FitnessInfoMySQL() throws DBException {
		loader = new FitnessInfoSQLLoader();
		try {
			Context ctx = new InitialContext();
			this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
		} catch (NamingException e) {
			throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
		}
		validator = new FitnessInfoValidator(this.ds);
	}
	/**
	 * Constructor for testing since we need a datasource to be passed in
	 * for testing
	 * @param ds
	 */
	public FitnessInfoMySQL(DataSource ds)
	{
		loader = new FitnessInfoSQLLoader();
		this.ds = ds;
		validator = new FitnessInfoValidator(this.ds);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<FitnessInfo> getAll() throws DBException {
		Connection conn = null;
		PreparedStatement pstring = null;
		ResultSet results = null;
		try {
			conn = ds.getConnection();
			pstring = conn.prepareStatement("SELECT * FROM fitness");
			results = pstring.executeQuery();
			final List<FitnessInfo> list = loader.loadList(results);
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
	
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FitnessInfo getByID(long id) throws DBException {
		FitnessInfo ret = null;
		Connection conn = null;
		PreparedStatement pstring = null;
		ResultSet results = null;
		List<FitnessInfo> list = null;
		try {
			conn = ds.getConnection();
			pstring = conn.prepareStatement("SELECT * FROM fitness WHERE pid=?");

			pstring.setLong(1, id);

			results = pstring.executeQuery();

			/* May update with loader instead */
			list = loader.loadList(results);
			if (list.size() > 0) {
				//return the first date one
				ret = list.get(0);
				for (int i = 1; i < list.size(); i++)
				{
					if (ret.getJavaDate().after(list.get(i).getJavaDate()))
					{
						ret = list.get(i);
					}
				}
			}
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
		return ret;
	}

	/**
	 * Add should be used in place of update().  Add makes sure no duplicate entries for the same
	 * PID and date exist in the database.  It, by default, overwrites existing data.
	 * Add either inserts the given FitnessInfo object into the database if such an entry does not
	 * exist for the given PID and date in the FitnessInfo object, or it updates the entry found with
	 * the same PID and date in the passed in object if such an entry is found.
	 * 
	 * Add verifies that the FitnessInfo object has valid fields using FitnessInfoValidator
	 * @param fi the FitnessInfo to put into the database
	 * @return true upon success, false otherwise
	 * @throws FormValidationException
	 * @throws DBException
	 */
	@Override
	public boolean add(FitnessInfo fi) throws FormValidationException, DBException {
		//Make sure the FitnessInfo is valid
		Connection conn = null;
		PreparedStatement pstring = null;
		try {
			//System.out.println("Validating");
			validator.validate(fi); //need, at min, the pid and date not to be blank
			//System.out.println("Passed validation");
		}
		catch (FormValidationException e1) {
			throw new DBException(new SQLException(e1));
		}
		
		//we need to see if we need to do an insert or an update, so see
		//if we already have an entry for the given pid and date
		long pid = fi.getPid();
		String date = fi.getDate();
		//long generatedId = -1;
		try
		{
			String stmt = "SELECT * FROM fitness WHERE pid=" + pid + " AND dateOfData='" + date + "';";
			conn = ds.getConnection();
			pstring = conn.prepareStatement(stmt);
			
			ResultSet rs = pstring.executeQuery();
			boolean hasRows = rs.next(); //see if we returned anything at all
			
			//Now close the old connection
			if (rs != null)
			{
				rs.close();
			}
			DBUtil.closeConnection(conn, pstring);
			//reopen a new connection
			conn = ds.getConnection();
			pstring = null;
			
			if (hasRows)
			{
				//Need to do an update
				pstring = loader.loadParameters(conn, pstring, fi, false);
			}
			else //Need to do an insert b/c no entries are already there
			{
				pstring = loader.loadParameters(conn, pstring, fi, true);
			}
			int results = pstring.executeUpdate();
			if (results != 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, pstring);
		}
		return false;
	}

	/**
	 * Update should not be used.
	 * The functionality for updating and inserting is in the add method.
	 * This now just calls the add method.
	 * @param fi the FitnessInfo to put into the database
	 * @return true upon success, false otherwise
	 * @throws FormValidationException
	 * @throws DBException
	 */
	@Override
	public boolean update(FitnessInfo fi) throws DBException, FormValidationException {
		return add(fi);
		/*
		boolean retval = false;
		Connection conn = null;
		PreparedStatement pstring = null;
		try {
			validator.validate(fi);
		} catch (FormValidationException e1) {
			throw new DBException(new SQLException(e1.getMessage()));
		}
		int results;

		try {
			conn = ds.getConnection();
			pstring = loader.loadParameters(conn, pstring, fi, false);
			results = pstring.executeUpdate();
			retval = (results > 0);
		} catch (SQLException e) {
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, pstring);
		}
		return retval;
		*/
	}

	/**
	 * Gets the fitness info for a given PID and date from the database
	 * @param pid the pid of the fitness info to search for
	 * @param date the date of the fitness info in the format YYYY-MM-DD
	 * @return a FitnessInfo object containing all the relevant data in the database for the given pid and date
	 * @throws DBException
	 */
	@Override
	public FitnessInfo getFitnessInfo(long pid, String date) throws DBException {
		Connection conn = null;
		PreparedStatement pstring = null;
		ResultSet results = null;
		try {
			conn = ds.getConnection();
			String stmt = "SELECT * FROM fitness WHERE pid=" + pid + " AND dateOfData='" + date + "';";
			pstring = conn.prepareStatement(stmt);
			results = pstring.executeQuery();
			//Must call results.next() b/f calling loadSingle
			if (!results.next())
			{
				return null; //no information to return, nothing found
			}
			FitnessInfo fi = loader.loadSingle(results);
			return fi;
		}
		catch (SQLException e) {
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

	/**
	 * Gets the fitness info for a given PID and span of dates
	 * @param pid whose information we are looking for
	 * @param startDate the earliest date of info that will be returned in the search results
	 * @param endDate the latest date of info that will be returned in the search results
	 * @return a list of FitnessInfo objects containing the info relevant to the given PID and whose dates occur within the given span of dates
	 * @throws DBException
	 */
	@Override
	public FitnessInfo[] getFitnessInfo(long pid, String startDate, String endDate) throws DBException {
		Connection conn = null;
		PreparedStatement pstring = null;
		ResultSet results = null;
		try {
			conn = ds.getConnection();
			String stmt = "SELECT * FROM fitness WHERE pid=" + pid + " AND dateOfData BETWEEN '" + startDate + "' AND '" + endDate + "';";
			pstring = conn.prepareStatement(stmt);
			results = pstring.executeQuery();
			List<FitnessInfo> list = loader.loadList(results);
			if (list.isEmpty())
			{
				return null;
			}
			//convert list to array
			FitnessInfo[] fiArr = new FitnessInfo[list.size()];
			for (int i = 0; i < fiArr.length; i++)
			{
				fiArr[i] = list.get(i);
			}
			return fiArr;
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

	/**
	 * Removes the fitness data entry in the database corresponding to the given PID and date
	 * @param pid whose fitness data we are looking for
	 * @param date the date of the fitness data
	 * @return true if the data was successfully removed
	 * @throws DBException
	 */
	@Override
	public boolean removeFitnessInfo(long pid, String date) throws DBException {
		Connection conn = null;
		PreparedStatement pstring = null;
		try {
			conn = ds.getConnection();
			String stmt = "DELETE FROM fitness WHERE pid=" + pid + " AND dateOfData='" + date + "';";
			pstring = conn.prepareStatement(stmt);
			int results = pstring.executeUpdate();
			return results != 0;
		} catch (SQLException e) {
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, pstring);
		}
	}
	
	/**
	 * Removes the fitness data entries in the database corresponding to the given PID and having
	 * a date falling within a certain span.
	 * @param pid whose fitness data we are looking for
	 * @param startDate the earliest date of info that will be removed
	 * @param endDate the latest date of info that will be removed
	 * @return true if the data was successfully removed
	 * @throws DBException
	 */
	@Override
	public boolean removeFitnessInfo(long pid, String startDate, String endDate) throws DBException {
		Connection conn = null;
		PreparedStatement pstring = null;
		try {
			conn = ds.getConnection();
			String stmt = "DELETE FROM fitness WHERE pid=" + pid + " AND dateOfData BETWEEN '" + startDate + "' AND '" + endDate + "';";
			pstring = conn.prepareStatement(stmt);
			int results = pstring.executeUpdate();
			return results != 0;
		} catch (SQLException e) {
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, pstring);
		}
	}

}
