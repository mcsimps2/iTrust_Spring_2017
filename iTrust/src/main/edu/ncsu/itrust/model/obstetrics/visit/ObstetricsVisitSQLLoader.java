package edu.ncsu.itrust.model.obstetrics.visit;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.mysql.jdbc.Statement;

import edu.ncsu.csc.itrust.model.SQLLoader;

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
		long id = rs.getLong("id");
		long patientMID = rs.getLong("patientMID");
		long obstetricsInitID = rs.getLong("obstetricsInitID");
		long officeVisitID = rs.getLong("officeVisitID");
		int fhr = rs.getInt("fhr");
		int multiplicity = rs.getInt("multiplicity");
		boolean lowLyingPlacentaObserved = rs.getBoolean("lowLyingPlacentaObserved");
		Blob imageOfUltrasound = rs.getBlob("imageOfUltrasound");
		return new ObstetricsVisit(id, patientMID, officeVisitID, obstetricsInitID, fhr, multiplicity, lowLyingPlacentaObserved, imageOfUltrasound);
	}

	@Override
	public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, ObstetricsVisit insertObject,
			boolean newInstance) throws SQLException {
		String stmt = "";
		if( newInstance ){ // IS NEW CODE
			stmt = "INSERT INTO obstetricsVisit(patientMID, obstetricsInitID, officeVisitID, fhr, multiplicity, lowLyingPlacentaObserved, imageOfUltrasound) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?);";
		} else { // NOT NEW
			long id = insertObject.getId();
			stmt = "UPDATE immunization SET  "
					+ "patientMID=?, "
					+ "obstetricsInitID=? "
					+ "officeVisitID=?"
					+ "fhr=?"
					+ "multiplicity=?"
					+ "lowLyingPlacentaObserved=?"
					+ "imageOfUltrasound=?"
					+ "WHERE id=" + id + ";";
		}
		
		ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
		ps.setLong( 1, insertObject.getPatientMID() );
		ps.setLong( 2,  insertObject.getObstetricsInitID() );
		ps.setLong(3, insertObject.getOfficeVisitID());
		ps.setInt(4, insertObject.getFhr());
		ps.setInt(5, insertObject.getMultiplicity());
		ps.setBoolean(6, insertObject.isLowLyingPlacentaObserved());
		ps.setBlob(7, insertObject.getImageOfUltrasound());		
		
		return ps;
	}

}
