package edu.ncsu.csc.itrust.model.obstetrics.pregnancies;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import edu.ncsu.csc.itrust.model.SQLLoader;

public class PregnancyInfoSQLLoader implements SQLLoader<PregnancyInfo>
{
	/**
	 * Takes a ResultSet from a query (e.g. SELECT) and turns the results into PregnancyInfo
	 * objects
	 * @param rs the result set to examine
	 * @return a list of ObstetricInit objects that represent the results of the query
	 * @throws SQLException
	 */
	@Override
	public List<PregnancyInfo> loadList(ResultSet rs) throws SQLException {
		ArrayList<PregnancyInfo> list = new ArrayList<PregnancyInfo>();
		while (rs.next())
		{
			list.add(loadSingle(rs));
		}
		return list;
	}

	/**
	 * Turns a single entry result set into a PregnancyInfo object
	 * @param rs the result set to convert
	 * @return an ObstetricInit object representing the results of the query
	 * @throws SQLException
	 */
	@Override
	public PregnancyInfo loadSingle(ResultSet rs) throws SQLException {
		PregnancyInfo pi = new PregnancyInfo();
		pi.setRecordID(rs.getInt("id"));
		pi.setObstetricsInitID(rs.getInt("obstetricsInitID"));
		pi.setPid(rs.getLong("pid"));
		pi.setYearOfConception(rs.getInt("yearOfConception"));
		pi.setNumDaysPregnant(rs.getInt("numDaysPregnant"));
		pi.setNumHoursInLabor(rs.getInt("numHoursInLabor"));
		pi.setWeightGain(rs.getInt("weightGain"));
		pi.setDeliveryType(DeliveryMethod.matchString(rs.getString("deliveryType")));
		pi.setMultiplicity(rs.getInt("multiplicity"));
		return pi;
	}

	/**
	 * Creates a PreparedStatement to insert or update a PregnancyInfo object into the database
	 * Updates are done when there is already an entry for the recordID
	 * Inserts are done otherwise.
	 * Specify newInstance = true for inserts and newInstance = false for updates
	 * @param conn the connection to the database
	 * @param ps the prepared statement that will point to the one created
	 * @param insertObject the PregnancyInfo object to insert/update into the database
	 * @param newInstance true if we need to do an insert and false if we need to do an update
	 * @throws SQLException
	 */
	@Override
	public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, PregnancyInfo insertObject,
			boolean newInstance) throws SQLException {
		String stmt = "";
		if (newInstance)
		{
			stmt = "INSERT INTO priorPregnancies (obstetricsInitID, pid, yearOfConception, numDaysPregnant, numHoursInLabor, weightGain, deliveryType, multiplicity) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
			ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, insertObject.getObstetricsInitID());
			ps.setLong(2, insertObject.getPid());
			ps.setInt(3, insertObject.getYearOfConception());
			ps.setInt(4, insertObject.getNumDaysPregnant());
			ps.setInt(5, insertObject.getNumHoursInLabor());
			ps.setInt(6, insertObject.getWeightGain());
			ps.setString(7, insertObject.getDeliveryType().toString());
			ps.setInt(8, insertObject.getMultiplicity());
		}
		else
		{
			stmt = "UPDATE priorPregnancies SET obstetricsInitID=?, pid=?, yearOfConception=?, numDaysPregnant=?, numHoursInLabor=?, weightGain=?, deliveryType=?, multiplicity=? WHERE id=?";
			ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, insertObject.getObstetricsInitID());
			ps.setLong(2, insertObject.getPid());
			ps.setInt(3, insertObject.getYearOfConception());
			ps.setInt(4, insertObject.getNumDaysPregnant());
			ps.setInt(5, insertObject.getNumHoursInLabor());
			ps.setInt(6, insertObject.getWeightGain());
			ps.setString(7, insertObject.getDeliveryType().toString());
			ps.setInt(8, insertObject.getMultiplicity());
			ps.setInt(9, insertObject.getRecordID());
		}
		return ps;
	}
	
}
