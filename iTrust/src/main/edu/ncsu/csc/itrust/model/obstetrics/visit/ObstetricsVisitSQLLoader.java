package edu.ncsu.csc.itrust.model.obstetrics.visit;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.mysql.jdbc.Statement;

import edu.ncsu.csc.itrust.model.SQLLoader;

/**
 * SQL loader to prepare statements to access and update obstetrics visit objects
 * @author jcgonzal
 */
public class ObstetricsVisitSQLLoader implements SQLLoader<ObstetricsVisit> {

	@Override
	public List<ObstetricsVisit> loadList(ResultSet rs) throws SQLException {
		List<ObstetricsVisit> list = new LinkedList<ObstetricsVisit>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	@Override
	public ObstetricsVisit loadSingle(ResultSet rs) throws SQLException {
		Long id = rs.getLong("id");
		Long officeVisitID = rs.getLong("officeVisitID");
		Long obstetricsInitID = rs.getLong("obstetricsInitID");
		Integer weeksPregnant = rs.getInt("weeksPregnant");
		Integer fhr = rs.getInt("fhr");
		if (rs.wasNull()) fhr = null;
		Integer multiplicity = rs.getInt("multiplicity");
		if (rs.wasNull()) multiplicity = null;
		Boolean lowLyingPlacentaObserved = rs.getBoolean("lowLyingPlacentaObserved");
		InputStream imageOfUltrasound = rs.getBinaryStream("imageOfUltrasound");
		String imageType = rs.getString("imageType");
		return new ObstetricsVisit(id, officeVisitID, obstetricsInitID, weeksPregnant, fhr, multiplicity, lowLyingPlacentaObserved, imageOfUltrasound, imageType);
	}

	@Override
	public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, ObstetricsVisit insertObject,
			boolean newInstance) throws SQLException {
		String stmt = "";
		if( newInstance ){ // IS NEW CODE
			stmt = "INSERT INTO obstetricsVisit(officeVisitID, obstetricsInitID, weeksPregnant, fhr, multiplicity, lowLyingPlacentaObserved, imageOfUltrasound, imageType) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
		} else { // NOT NEW
			long id = insertObject.getId();
			stmt = "UPDATE obstetricsVisit SET "
					+ "officeVisitID=?, "
					+ "obstetricsInitID=?, "
					+ "weeksPregnant=?, "
					+ "fhr=?, "
					+ "multiplicity=?, "
					+ "lowLyingPlacentaObserved=?, "
					+ "imageOfUltrasound=?, "
					+ "imageType=? "
					+ "WHERE id=" + id + ";";
		}
		
		ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
		ps.setLong(1, insertObject.getOfficeVisitID());
		ps.setLong(2, insertObject.getObstetricsInitID());
		ps.setInt(3, insertObject.getWeeksPregnant());
		
		if (insertObject.getFhr() == null)
			ps.setNull(4, java.sql.Types.INTEGER);
		else
			ps.setInt(4, insertObject.getFhr());
		
		if (insertObject.getMultiplicity() == null)
			ps.setNull(5, java.sql.Types.INTEGER);
		else
			ps.setInt(5, insertObject.getMultiplicity());
		
		ps.setBoolean(6, insertObject.isLowLyingPlacentaObserved());
		ps.setBinaryStream(7, insertObject.getImageOfUltrasound());
		ps.setString(8, insertObject.getImageType());
		
		return ps;
	}

}
