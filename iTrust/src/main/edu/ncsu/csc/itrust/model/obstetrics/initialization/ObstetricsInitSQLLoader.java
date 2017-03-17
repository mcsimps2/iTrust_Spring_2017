package edu.ncsu.csc.itrust.model.obstetrics.initialization;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import edu.ncsu.csc.itrust.model.SQLLoader;

public class ObstetricsInitSQLLoader implements SQLLoader<ObstetricsInit>
{

	/**
	 * Takes a ResultSet from a query (e.g. SELECT) and turns the results into ObstetricInit
	 * objects
	 * @param rs the result set to examine
	 * @return a list of ObstetricInit objects that represent the results of the query
	 * @throws SQLException
	 */
	@Override
	public List<ObstetricsInit> loadList(ResultSet rs) throws SQLException {
		ArrayList<ObstetricsInit> list = new ArrayList<ObstetricsInit>();
		while (rs.next())
		{
			list.add(loadSingle(rs));
		}
		return list;
	}

	/**
	 * Turns a single entry result set into an ObstetricInit object
	 * @param rs the result set to convert
	 * @return an ObstetricInit object representing the results of the query
	 * @throws SQLException
	 */
	@Override
	public ObstetricsInit loadSingle(ResultSet rs) throws SQLException {
		ObstetricsInit oi = new ObstetricsInit();
		oi.setPid(rs.getLong("pid"));
		oi.setDate(rs.getDate("dateOfInit"));
		oi.setLMP(rs.getDate("LMP"));
		return oi;
	}

	/**
	 * Creates a PreparedStatement to insert or update an ObstetricInit object into the database
	 * Updates are done when there is already an entry for the given PID and date in the ObstetricInit object.
	 * Inserts are done otherwise.
	 * Specify newInstance = true for inserts and newInstance = false for updates
	 * @param conn the connection to the database
	 * @param ps the prepared statement that will point to the one created
	 * @param insertObject the ObstetricInit object to insert/update into the database
	 * @param newInstance true if we need to do an insert and false if we need to do an update
	 * @throws SQLException
	 */
	@Override
	public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, ObstetricsInit insertObject,
			boolean newInstance) throws SQLException {
		String stmt = "";
		if (newInstance) {
			stmt = "INSERT INTO obstetricsInit(pid, dateOfInit, LMP) VALUES (?, ?, ?);";
			ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
			String dateOfInit = insertObject.getDate();
			String dateOfLMP = insertObject.getLMP();
			ps.setLong(1, insertObject.getPid());
			ps.setDate(2, Date.valueOf(dateOfInit));
			ps.setDate(3, Date.valueOf(dateOfLMP));
		}
		else
		{
			stmt = "UPDATE obstetricsInit SET LMP=? WHERE pid=" + insertObject.getPid() + " AND dateOfInit='" + insertObject.getDate() + "';";
			ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
			ps.setDate(1, Date.valueOf(insertObject.getLMP()));
		}
		return ps;
	}

}
