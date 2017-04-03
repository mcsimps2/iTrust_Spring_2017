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
		Long pid = rs.getLong("pid");
		nb.setPid(rs.wasNull() ? null : pid);
		Long id = rs.getLong("id");
		nb.setId(rs.wasNull() ? null : id);
		Long oid = rs.getLong("officeVisitID");
		nb.setOfficeVisitID(rs.wasNull() ? null : oid);
		String dob = rs.getString("dateOfBirth");
		nb.setDateOfBirth(rs.wasNull() ? null : dob);
		String tob = rs.getString("timeOfBirth");
		nb.setTimeOfBirth(rs.wasNull() ? null : tob.substring(0, tob.length() - 3)); //take off the seconds
		String sex = rs.getString("sex");
		if (rs.wasNull())
		{
			nb.setSex(null);
		}
		else
		{
			nb.setSex(SexType.matchString(sex));
		}
		Boolean te = rs.getBoolean("timeEstimated");
		nb.setTimeEstimated(rs.wasNull() ? null : te);
		return nb;
	}

	@Override
	public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, Newborn insertObject,
			boolean newInstance) throws SQLException
	{
		String stmt = "";
		if( newInstance ){ // IS NEW CODE
			stmt = "INSERT INTO childbirthNewborns(officeVisitID, dateOfBirth, timeOfBirth, sex, timeEstimated, pid) VALUES (?, ?, ?, ?, ?, ?);";
		} else { // NOT NEW
			long id = insertObject.getId();
			stmt = "UPDATE childbirthNewborns SET "
					+ "officeVisitID=?, "
					+ "dateOfBirth=?, "
					+ "timeOfBirth=?, "
					+ "sex=?, "
					+ "timeEstimated=?, "
					+ "pid=? "
					+ "WHERE id=" + id + ";";
		}
		
		ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
		ps.setLong(1, insertObject.getOfficeVisitID());
		if (insertObject.getDateOfBirth() == null || insertObject.getDateOfBirth().equals(""))
		{
			ps.setNull(2, java.sql.Types.DATE);
		}
		else
		{
			ps.setDate(2, java.sql.Date.valueOf(insertObject.getDateOfBirth()));
		}
		if (insertObject.getTimeOfBirth() == null || insertObject.getTimeOfBirth().equals(""))
		{
			ps.setNull(3, java.sql.Types.TIME);
		}
		else
		{
			ps.setTime(3, java.sql.Time.valueOf(insertObject.getTimeOfBirth() + ":00"));
		}
		if (insertObject.getSex() == null)
		{
			ps.setNull(4, java.sql.Types.VARCHAR);
		}
		else
		{
			ps.setString(4, insertObject.getSex().toString());
		}
		if (insertObject.getTimeEstimated() == null)
		{
			ps.setNull(5, java.sql.Types.BOOLEAN);
		}
		else
		{
			ps.setBoolean(5, insertObject.getTimeEstimated());
		}
		if (insertObject.getPid() == null)
		{
			ps.setNull(6, java.sql.Types.BIGINT);
		}
		else
		{
			ps.setLong(6, insertObject.getPid());
		}
		return ps;
	}

}
