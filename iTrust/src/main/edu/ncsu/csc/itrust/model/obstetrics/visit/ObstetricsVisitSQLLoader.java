package edu.ncsu.csc.itrust.model.obstetrics.visit;

import java.sql.Blob;
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
		Integer weeksPregnant = rs.getInt("weeksPregnant");
		Integer daysPregnant = rs.getInt("daysPregnant");
		Integer fhr = rs.getInt("fhr");
		Integer multiplicity = rs.getInt("multiplicity");
		Boolean lowLyingPlacentaObserved = rs.getBoolean("lowLyingPlacentaObserved");
		Blob imageOfUltrasound = rs.getBlob("imageOfUltrasound");
		return new ObstetricsVisit(id, officeVisitID, weeksPregnant, daysPregnant, fhr, multiplicity, lowLyingPlacentaObserved, imageOfUltrasound);
	}

	@Override
	public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, ObstetricsVisit insertObject,
			boolean newInstance) throws SQLException {
		String stmt = "";
		if( newInstance ){ // IS NEW CODE
			stmt = "INSERT INTO obstetricsVisit(officeVisitID, weeksPregnant, daysPregnant, fhr, multiplicity, lowLyingPlacentaObserved, imageOfUltrasound) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?);";
		} else { // NOT NEW
			long id = insertObject.getId();
			stmt = "UPDATE immunization SET  "
					+ "officeVisitID=?"
					+ "weeksPregnant=?"
					+ "daysPregnant=?"
					+ "fhr=?"
					+ "multiplicity=?"
					+ "lowLyingPlacentaObserved=?"
					+ "imageOfUltrasound=?"
					+ "WHERE id=" + id + ";";
		}
		
		ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
		ps.setLong(1, insertObject.getOfficeVisitID());
		ps.setInt(2, insertObject.getWeeksPregnant());
		ps.setInt(3, insertObject.getDaysPregnant());
		ps.setInt(4, insertObject.getFhr());
		ps.setInt(5, insertObject.getMultiplicity());
		ps.setBoolean(6, insertObject.isLowLyingPlacentaObserved());
		ps.setBlob(7, insertObject.getImageOfUltrasound());		
		
		return ps;
	}

}
