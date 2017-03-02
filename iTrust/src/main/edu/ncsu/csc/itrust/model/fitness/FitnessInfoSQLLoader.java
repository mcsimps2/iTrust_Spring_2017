package edu.ncsu.csc.itrust.model.fitness;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.model.SQLLoader;

/**
 * Responsible for turning result sets into FitnessInfo objects and making
 * PreparedStatements to inserting and updating into the fitness database
 * @author mcsimps2
 *
 */
public class FitnessInfoSQLLoader implements SQLLoader<FitnessInfo>
{

	/**
	 * Takes a ResultSet from a query (e.g. SELECT) and turns the results into FitnessInfo
	 * objects
	 * @param rs the result set to examine
	 * @return a list of FitnessInfo objects that represent the results of the query
	 * @throws SQLException
	 */
	@Override
	public List<FitnessInfo> loadList(ResultSet rs) throws SQLException {
		//Take all the requests and put load them using the single functionality
		ArrayList<FitnessInfo> list = new ArrayList<FitnessInfo>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	/**
	 * Turns a single entry result set into a FitnessInfo object
	 * @param rs the result set to convert
	 * @return a FitnessInfo object represeting the results of the query
	 * @throws SQLException
	 */
	@Override
	public FitnessInfo loadSingle(ResultSet rs) throws SQLException {
		FitnessInfo fitnessInfo = new FitnessInfo();
		fitnessInfo.setDate(rs.getDate("dateOfData").toString());
		fitnessInfo.setPid(rs.getInt("pid"));
		fitnessInfo.setSteps(rs.getInt("steps"));
		fitnessInfo.setCaloriesBurned(rs.getInt("caloriesBurned"));
		fitnessInfo.setMiles(rs.getDouble("distance"));
		fitnessInfo.setFloors(rs.getInt("floors"));
		fitnessInfo.setActiveCalories(rs.getInt("caloriesActive"));
		fitnessInfo.setMinutesSedentary(rs.getInt("minutesSedentary"));
		fitnessInfo.setMinutesFairlyActive(rs.getInt("minutesFairlyActive"));
		fitnessInfo.setMinutesLightlyActive(rs.getInt("minutesLightlyActive"));
		fitnessInfo.setMinutesVeryActive(rs.getInt("minutesVeryActive"));
		fitnessInfo.setHeartRateLow(rs.getInt("heartRateLow"));
		fitnessInfo.setHeartRateHigh(rs.getInt("heartRateHigh"));
		fitnessInfo.setHeartRateAvg(rs.getInt("heartRateAvg"));
		fitnessInfo.setActiveHours(rs.getInt("activeHours"));
		fitnessInfo.setMinutesUVExposure(rs.getInt("minutesUVExposure"));
		return fitnessInfo;
	}

	/**
	 * Creates a PreparedStatement to insert or update a FitnessInfo object into the database
	 * Updates are done when there is already an entry for the given PID and date in the FItnessInfo object.
	 * Inserts are done otherwise.
	 * Specify newInstance = true for inserts and newInstance = false for udpates
	 * @param conn the connection to the database
	 * @param ps the prepared statement that will point to the one created
	 * @param fi the fitness info object to insert/update into the database
	 * @param newInstance true if we need to do an insert and false if we need to do an update
	 * @throws SQLException
	 */
	@Override
	public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, FitnessInfo fi,
			boolean newInstance) throws SQLException {
		String stmt = "";
		if (newInstance) {
			stmt = "INSERT INTO fitness(pid, dateOfData, steps, caloriesBurned, distance, floors, caloriesActive, minutesSedentary, minutesFairlyActive,"
					+ " minutesLightlyActive, minutesVeryActive, heartRateLow, heartRateHigh, heartRateAvg, activeHours, minutesUVExposure) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
			ps.setLong(1, fi.getPid());
			String fiDate = fi.getDate(); //YYYY-MM-DD 0123(4)56(7)89
			@SuppressWarnings("deprecation")
			Date date = new Date(Integer.parseInt(fiDate.substring(0,4)), Integer.parseInt(fiDate.substring(5,7)), Integer.parseInt(fiDate.substring(8)));
			//System.out.println("Date is :" + fi.getDate());
			//System.out.println("Year: " + date.getYear());
			ps.setDate(2, Date.valueOf(fiDate));
			ps.setInt(3, fi.getSteps());
			ps.setInt(4, fi.getCaloriesBurned());
			ps.setDouble(5,  fi.getMiles());
			ps.setInt(6, fi.getFloors());
			ps.setInt(7,  fi.getActiveCalories());
			ps.setInt(8,  fi.getMinutesSedentary());
			ps.setInt(9, fi.getMinutesFairlyActive());
			ps.setInt(10, fi.getMinutesLightlyActive());
			ps.setInt(11, fi.getMinutesVeryActive());
			ps.setInt(12, fi.getHeartRateLow());
			ps.setInt(13, fi.getHeartRateHigh());
			ps.setInt(14, fi.getHeartRateAvg());
			ps.setInt(15, fi.getActiveHours());
			ps.setInt(16, fi.getMinutesUVExposure());
		}
		else {
			stmt = "UPDATE fitness SET "
					+ "steps=?, "
					+ "caloriesBurned=?, "
					+ "distance=?, "
					+ "floors=?, "
					+ "caloriesActive=?, "
					+ "minutesSedentary=?, "
					+ "minutesFairlyActive=?, "
					+ "minutesLightlyActive=?, "
					+ "minutesVeryActive=?, "
					+ "heartRateLow=?, "
					+ "heartRateHigh=?, "
					+ "heartRateAvg=?, "
					+ "activeHours=?, "
					+ "minutesUVExposure=? "
					+ "WHERE pid=" + fi.getPid() + " AND dateOfData='" + fi.getDate() + "';";
			ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, fi.getSteps());
			ps.setInt(2, fi.getCaloriesBurned());
			ps.setDouble(3,  fi.getMiles());
			ps.setInt(4, fi.getFloors());
			ps.setInt(5,  fi.getActiveCalories());
			ps.setInt(6,  fi.getMinutesSedentary());
			ps.setInt(7, fi.getMinutesFairlyActive());
			ps.setInt(8, fi.getMinutesLightlyActive());
			ps.setInt(9, fi.getMinutesVeryActive());
			ps.setInt(10, fi.getHeartRateLow());
			ps.setInt(11, fi.getHeartRateHigh());
			ps.setInt(12, fi.getHeartRateAvg());
			ps.setInt(13, fi.getActiveHours());
			ps.setInt(14, fi.getMinutesUVExposure());
		}
		
		
		return ps;
	}

}
