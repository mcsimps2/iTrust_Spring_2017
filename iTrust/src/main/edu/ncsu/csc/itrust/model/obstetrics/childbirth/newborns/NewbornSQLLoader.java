package edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.mysql.jdbc.Statement;

import edu.ncsu.csc.itrust.model.SQLLoader;

public class NewbornSQLLoader implements SQLLoader<Newborn>
{

	@Override
	public List<Newborn> loadList(ResultSet rs) throws SQLException
	{
		List<Newborn> list = new LinkedList<Newborn>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	@Override
	public Newborn loadSingle(ResultSet rs) throws SQLException
	{
		Newborn nb = new Newborn();
		//All fields must be non-null
		nb.setId(rs.getLong("id"));
		nb.setOfficeVisitID(rs.getLong("officeVisitID"));
		nb.setDateOfBirth(rs.getString("dateOfBirth"));
		nb.setTimeOfBirth(rs.getString("timeOfBirth"));
		nb.setSex(SexType.matchString(rs.getString("sex")));
		nb.setTimeEstimated(rs.getBoolean("timeEstimated"));
		return nb;
	}

	@Override
	public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, Newborn insertObject,
			boolean newInstance) throws SQLException
	{
		String stmt = "";
		if( newInstance ){ // IS NEW CODE
			stmt = "INSERT INTO childbirthNewborns(officeVisitID, dateOfBirth, timeOfBirth, sex, timeEstimated) VALUES (?, ?, ?, ?, ?);";
		} else { // NOT NEW
			long id = insertObject.getId();
			stmt = "UPDATE childbirthNewborns SET "
					+ "officeVisitID=?, "
					+ "dateOfBirth=?, "
					+ "timeOfBirth=?, "
					+ "sex=?, "
					+ "timeEstimated=? "
					+ "WHERE id=" + id + ";";
		}
		
		ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
		ps.setLong(1, insertObject.getOfficeVisitID());
		ps.setDate(2, java.sql.Date.valueOf(insertObject.getDateOfBirth()));
		ps.setTime(3, java.sql.Time.valueOf(insertObject.getTimeOfBirth()));
		ps.setString(4, insertObject.getSex().toString());
		ps.setBoolean(5, insertObject.getTimeEstimated());
		
		return ps;
	}

}
