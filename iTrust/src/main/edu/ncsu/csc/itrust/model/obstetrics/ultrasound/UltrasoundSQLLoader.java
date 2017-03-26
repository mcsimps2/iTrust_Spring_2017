package edu.ncsu.csc.itrust.model.obstetrics.ultrasound;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.mysql.jdbc.Statement;

import edu.ncsu.csc.itrust.model.SQLLoader;

/**
 * SQL Loader to prepare statements for Ultrasound
 * @author jcgonzal
 */
public class UltrasoundSQLLoader implements SQLLoader<Ultrasound> {

	@Override
	public List<Ultrasound> loadList(ResultSet rs) throws SQLException {
		List<Ultrasound> list = new LinkedList<Ultrasound>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	@Override
	public Ultrasound loadSingle(ResultSet rs) throws SQLException {
		Long id = rs.getLong("id");
		Long officeVisitID = rs.getLong("officeVisitID");
		Integer crl = rs.getInt("crl");
		Integer bpd = rs.getInt("bpd");
		Integer hc = rs.getInt("hc");
		Integer fl = rs.getInt("fl");
		Integer ofd = rs.getInt("ofd");
		Integer ac = rs.getInt("ac");
		Integer hl = rs.getInt("hl");
		Integer efw = rs.getInt("efw");
		return new Ultrasound(id, officeVisitID, crl, bpd, hc, fl, ofd,
				ac, hl, efw);
	}

	@Override
	public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, Ultrasound insertObject,
			boolean newInstance) throws SQLException {
		String stmt = "";
		if( newInstance ){ // IS NEW CODE
			stmt = "INSERT INTO ultrasound(officeVisitID, crl, bpd, hc, fl, ofd, ac, hl, efw) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
		} else { // NOT NEW
			long id = insertObject.getId();
			stmt = "UPDATE ultrasound SET  "
					+ "officeVisitID=?"
					+ "crl=?"
					+ "bpd=?"
					+ "hc=?"
					+ "fl=?"
					+ "ofd=?"
					+ "ac=?"
					+ "hl=?"
					+ "efw=?"
					+ "WHERE id=" + id + ";";
		}
		
		ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
		ps.setLong(1, insertObject.getOfficeVisitID());
		ps.setInt(2, insertObject.getCrl());
		ps.setInt(3, insertObject.getBpd());
		ps.setInt(4, insertObject.getHc());
		ps.setInt(5, insertObject.getFl());
		ps.setInt(6, insertObject.getOfd());
		ps.setInt(7, insertObject.getAc());
		ps.setInt(8, insertObject.getHl());
		ps.setInt(9, insertObject.getEfw());
		
		return ps;
	}
	
	/**
	 * Loads the prepared statement to delete the 
	 * @param conn connection to the db
	 * @param id unique id to delete
	 * @return the prepared sql statement
	 * @throws SQLException
	 */
    public PreparedStatement loadDelete(Connection conn, long id) throws SQLException {
        PreparedStatement pstring = conn.prepareStatement("DELETE FROM ultrasound WHERE id=?");
        pstring.setLong(1, id);
        return pstring;
    }
    
}
