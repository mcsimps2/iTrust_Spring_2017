package edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import edu.ncsu.csc.itrust.model.SQLLoader;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.DeliveryMethod;

public class ChildbirthVisitSQLLoader implements SQLLoader<ChildbirthVisit>
{
	/**
	 * Takes a ResultSet from a query (e.g. SELECT) and turns the results into ChildbirthVisit
	 * objects
	 * @param rs the result set to examine
	 * @return a list of ObstetricInit objects that represent the results of the query
	 * @throws SQLException
	 */
	@Override
	public List<ChildbirthVisit> loadList(ResultSet rs) throws SQLException {
		ArrayList<ChildbirthVisit> list = new ArrayList<ChildbirthVisit>();
		while (rs.next())
		{
			list.add(loadSingle(rs));
		}
		return list;
	}

	/**
	 * Turns a single entry result set into a ChildbirthVisit object
	 * @param rs the result set to convert
	 * @return an ObstetricInit object representing the results of the query
	 * @throws SQLException
	 */
	@Override
	public ChildbirthVisit loadSingle(ResultSet rs) throws SQLException {
		ChildbirthVisit cv = new ChildbirthVisit();
		cv.setId(rs.getLong("id"));
		cv.setOfficeVisitID(rs.getLong("officeVisitID"));
		cv.setDeliveryMethod(DeliveryMethod.matchString(rs.getString("deliveryType")));
		cv.setPitocin(rs.getInt("pitocin"));
		cv.setNitrousOxide(rs.getInt("nitrousOxide"));
		cv.setPethidine(rs.getInt("pethidine"));
		cv.setEpiduralAnaesthesia(rs.getInt("epiduralAnaesthesia"));
		cv.setMagnesiumSulfide(rs.getInt("magnesiumSulfide"));
		return cv;
	}

	/**
	 * Creates a PreparedStatement to insert or update a ChildbirthVisit object into the database
	 * Updates are done when there is already an entry for the recordID
	 * Inserts are done otherwise.
	 * Specify newInstance = true for inserts and newInstance = false for updates
	 * @param conn the connection to the database
	 * @param ps the prepared statement that will point to the one created
	 * @param insertObject the ChildbirthVisit object to insert/update into the database
	 * @param newInstance true if we need to do an insert and false if we need to do an update
	 * @throws SQLException
	 */
	@Override
	public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, ChildbirthVisit insertObject,
			boolean newInstance) throws SQLException {
		String stmt = "";
		if (newInstance)
		{
			stmt = "INSERT INTO childbirthVisits (officeVisitID, deliveryType, pitocin, nitrousOxide, pethidine, epiduralAnaesthesia, magnesiumSulfide) VALUES (?, ?, ?, ?, ?, ?, ?);";
			ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
			ps.setLong(1, insertObject.getOfficeVisitID());
			ps.setString(2, insertObject.getDeliveryMethod().toString());
			ps.setInt(3, insertObject.getPitocin());
			ps.setInt(4, insertObject.getNitrousOxide());
			ps.setInt(5, insertObject.getNitrousOxide());
			ps.setInt(6, insertObject.getPethidine());
			ps.setInt(7, insertObject.getEpiduralAnaesthesia());
			ps.setInt(8, insertObject.getMagnesiumSulfide());
		}
		else
		{
			stmt = "UPDATE childbirthVisits SET officeVisitID=?, deliveryType=?, pitocin=?, nitrousOxide=?, pethidine=?, epiduralAnaesthesia=?, magnesiumSulfide=? WHERE id=?;";
			ps.setLong(1, insertObject.getOfficeVisitID());
			ps.setString(2, insertObject.getDeliveryMethod().toString());
			ps.setInt(3, insertObject.getPitocin());
			ps.setInt(4, insertObject.getNitrousOxide());
			ps.setInt(5, insertObject.getNitrousOxide());
			ps.setInt(6, insertObject.getPethidine());
			ps.setInt(7, insertObject.getEpiduralAnaesthesia());
			ps.setInt(8, insertObject.getMagnesiumSulfide());
			ps.setLong(9, insertObject.getId());
		}
		return ps;
	}
}
