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
		Long id = rs.getLong("id");
		if (rs.wasNull()) id = null;
		cv.setId(id);
		
		Long oid = rs.getLong("officeVisitID");
		if (rs.wasNull()) oid = null;
		cv.setOfficeVisitID(oid);
		
		String devType = rs.getString("deliveryType");
		if (rs.wasNull())
		{
			cv.setDeliveryType(null);
		}
		else
		{
			cv.setDeliveryType(DeliveryMethod.matchString(devType));
		}
		
		String visitType = rs.getString("visitType");
		if (rs.wasNull())
		{
			cv.setVisitType(null);
		}
		else
		{
			cv.setVisitType(VisitType.matchString(visitType));
		}
		
		Integer pitocin = rs.getInt("pitocin");
		if (rs.wasNull()) pitocin = null;
		cv.setPitocin(pitocin);
		
		Integer no = rs.getInt("nitrousOxide");
		if (rs.wasNull()) no = null;
		cv.setNitrousOxide(no);
		
		Integer peth = rs.getInt("pethidine");
		if (rs.wasNull()) peth = null;
		cv.setPethidine(peth);
		
		Integer ea = rs.getInt("epiduralAnaesthesia");
		if (rs.wasNull()) ea = null;
		cv.setEpiduralAnaesthesia(ea);
		
		Integer ms = rs.getInt("magnesiumSulfate");
		if (rs.wasNull()) ms = null;
		cv.setMagnesiumSulfate(ms);
		
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
			stmt = "INSERT INTO childbirthVisits (officeVisitID, deliveryType, visitType, pitocin, nitrousOxide, pethidine, epiduralAnaesthesia, magnesiumSulfate) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
			ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
			ps.setLong(1, insertObject.getOfficeVisitID());
			if (insertObject.getDeliveryType() == null)
			{
				ps.setNull(2, java.sql.Types.VARCHAR);
			}
			else
			{
				ps.setString(2, insertObject.getDeliveryType().toString());
			}
			if (insertObject.getVisitType() == null)
			{
				ps.setNull(3, java.sql.Types.VARCHAR);
			}
			else
			{
				ps.setString(3, insertObject.getVisitType().toString());
			}
			if (insertObject.getPitocin() == null)
			{
				ps.setNull(4, java.sql.Types.BIGINT);
			}
			else
			{
				ps.setInt(4, insertObject.getPitocin());
			}
			if (insertObject.getNitrousOxide() == null)
			{
				ps.setNull(5, java.sql.Types.BIGINT);
			}
			else
			{
				ps.setInt(5, insertObject.getNitrousOxide());
			}
			if (insertObject.getPethidine() == null)
			{
				ps.setNull(6, java.sql.Types.BIGINT);
			}
			else
			{
				ps.setInt(6, insertObject.getPethidine());
			}
			if (insertObject.getEpiduralAnaesthesia() == null)
			{
				ps.setNull(7, java.sql.Types.BIGINT);
			}
			else
			{
				ps.setInt(7, insertObject.getEpiduralAnaesthesia());
			}
			if (insertObject.getMagnesiumSulfate() == null)
			{
				ps.setNull(8, java.sql.Types.BIGINT);
			}
			else
			{
				ps.setInt(8, insertObject.getMagnesiumSulfate());
			}
		}
		else
		{
			stmt = "UPDATE childbirthVisits SET officeVisitID=?, deliveryType=?, visitType=?, pitocin=?, nitrousOxide=?, pethidine=?, epiduralAnaesthesia=?, magnesiumSulfate=? WHERE id=?;";
			ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
			ps.setLong(1, insertObject.getOfficeVisitID());
			if (insertObject.getDeliveryType() == null)
			{
				ps.setNull(2, java.sql.Types.VARCHAR);
			}
			else
			{
				ps.setString(2, insertObject.getDeliveryType().toString());
			}
			if (insertObject.getVisitType() == null)
			{
				ps.setNull(3, java.sql.Types.VARCHAR);
			}
			else
			{
				ps.setString(3, insertObject.getVisitType().toString());
			}
			if (insertObject.getPitocin() == null)
			{
				ps.setNull(4, java.sql.Types.BIGINT);
			}
			else
			{
				ps.setInt(4, insertObject.getPitocin());
			}
			if (insertObject.getNitrousOxide() == null)
			{
				ps.setNull(5, java.sql.Types.BIGINT);
			}
			else
			{
				ps.setInt(5, insertObject.getNitrousOxide());
			}
			if (insertObject.getPethidine() == null)
			{
				ps.setNull(6, java.sql.Types.BIGINT);
			}
			else
			{
				ps.setInt(6, insertObject.getPethidine());
			}
			if (insertObject.getEpiduralAnaesthesia() == null)
			{
				ps.setNull(7, java.sql.Types.BIGINT);
			}
			else
			{
				ps.setInt(7, insertObject.getEpiduralAnaesthesia());
			}
			if (insertObject.getMagnesiumSulfate() == null)
			{
				ps.setNull(8, java.sql.Types.BIGINT);
			}
			else
			{
				ps.setInt(8, insertObject.getMagnesiumSulfate());
			}
			ps.setLong(9, insertObject.getId());
		}
		return ps;
	}
}