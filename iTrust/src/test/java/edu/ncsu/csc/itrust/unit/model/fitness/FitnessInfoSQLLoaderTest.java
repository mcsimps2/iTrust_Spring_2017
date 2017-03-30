package edu.ncsu.csc.itrust.unit.model.fitness;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.*;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.fitness.FitnessInfo;
import edu.ncsu.csc.itrust.model.fitness.FitnessInfoSQLLoader;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

/**
 * Tests the FitnessInfoSQLLoader
 * @author mcsimps2
 *
 */
public class FitnessInfoSQLLoaderTest {
	FitnessInfoSQLLoader loader;
	/**
	 * Setup the tests
	 * @throws SQLException 
	 * @throws IOException 
	 */
	@Before
	public void setup() throws IOException, SQLException
	{
		// Reset test data
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
		
		loader = new FitnessInfoSQLLoader();
	}
	
	/**
	 * Try inserting an entry into the database (i.e. the date and pid of the info does not exist in the database yet, so don't need to do an update)
	 * @throws SQLException
	 * @throws DBException
	 */
	@Test
	public void testInsert() throws SQLException, DBException
	{
		DataSource ds = ConverterDAO.getDataSource();
		Connection conn = ds.getConnection();
		PreparedStatement ps = null;
		FitnessInfo fi = new FitnessInfo();
		fi.setPid(1);
		fi.setDate("1996-01-01");
		fi.setSteps(30);
		ps = loader.loadParameters(conn, ps, fi, true);
		ps.executeUpdate();
		
		String stmt = "SELECT * from fitness WHERE dateOfData='1996-01-01'";
		ps = conn.prepareStatement(stmt);
		ResultSet rs = ps.executeQuery();
		Assert.assertTrue(rs.next());
		Assert.assertEquals(rs.getInt("steps"), fi.getSteps());
		Assert.assertTrue(rs.getDate("dateOfData").toString().equals(fi.getDate()));
		Assert.assertEquals(rs.getLong("pid"), fi.getPid());
	}
	
	/**
	 * Try updating an entry in the database (i.e. the date and pid of the data exists in the database already, but we have new info)
	 * @throws SQLException
	 * @throws DBException
	 */
	@Test
	public void testUpdate() throws SQLException, DBException
	{
		//First insert something
		DataSource ds = ConverterDAO.getDataSource();
		Connection conn = ds.getConnection();
		PreparedStatement ps = null;
		FitnessInfo fi = new FitnessInfo();
		fi.setPid(1);
		fi.setDate("2001-12-21");
		fi.setSteps(100);
		fi.setMiles(1.5);
		ps = loader.loadParameters(conn, ps, fi, true);
		ps.executeUpdate();
		
		//Now update
		fi.setSteps(500);
		fi.setFloors(3);
		ps = loader.loadParameters(conn, ps, fi, false);
		ps.executeUpdate();
		
		//Now check
		String stmt = "SELECT * from fitness WHERE dateOfData='2001-12-21'";
		ps = conn.prepareStatement(stmt);
		ResultSet rs = ps.executeQuery();
		Assert.assertTrue(rs.next()); //move cursor to the right row
		Assert.assertEquals(rs.getInt("steps"), 500);
		Assert.assertTrue(rs.getDate("dateOfData").toString().equals(fi.getDate()));
		Assert.assertEquals(rs.getLong("pid"), fi.getPid());
		Assert.assertTrue(Math.abs(rs.getDouble("distance") - 1.5) < 0.001);
		Assert.assertEquals(rs.getInt("floors"), 3);
	}
}
