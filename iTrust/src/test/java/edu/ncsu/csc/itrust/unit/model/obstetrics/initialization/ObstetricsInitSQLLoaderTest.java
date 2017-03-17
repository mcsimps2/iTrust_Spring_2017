package edu.ncsu.csc.itrust.unit.model.obstetrics.initialization;
import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInit;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInitSQLLoader;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class ObstetricsInitSQLLoaderTest 
{
	ObstetricsInitSQLLoader loader;
	
	@Before
	public void setup() throws IOException, SQLException
	{
		TestDataGenerator.main(null);
		loader = new ObstetricsInitSQLLoader();
	}
	
	@Test
	public void testInsert() throws SQLException
	{
		DataSource ds = ConverterDAO.getDataSource();
		Connection conn = ds.getConnection();
		PreparedStatement ps = null;
		ObstetricsInit oi = new ObstetricsInit(1111, "2016-03-16", "2015-12-20");
		ps = loader.loadParameters(conn, ps, oi, true);
		ps.executeUpdate();
		
		String stmt = "SELECT * FROM obstetricsInit WHERE PID = 1111 AND dateOfInit='2016-03-16'";
		ps = conn.prepareStatement(stmt);
		ResultSet rs = ps.executeQuery();
		Assert.assertTrue(rs.next());
		Assert.assertTrue(rs.getDate("dateOfInit").equals(oi.getSQLDate()));
		Assert.assertTrue(rs.getDate("LMP").equals(oi.getSQLLMP()));
		//Assert.assertFalse(rs.next());
		
	}
	
	@Test
	public void testUpdate() throws SQLException
	{
		//First, insert something
		DataSource ds = ConverterDAO.getDataSource();
		Connection conn = ds.getConnection();
		PreparedStatement ps = null;
		ObstetricsInit oi = new ObstetricsInit(1111, "2016-03-01", "2015-12-01");
		ps = loader.loadParameters(conn, ps, oi, true);
		ps.executeUpdate();
		
		//Now, update it
		oi.setLMP("2015-12-29");
		ps = loader.loadParameters(conn, ps, oi, false);
		ps.executeUpdate();
		
		//Now check
		String stmt = "SELECT * FROM obstetricsInit WHERE PID = 1111 AND dateOfInit='2016-03-01'";
		ps = conn.prepareStatement(stmt);
		ResultSet rs = ps.executeQuery();
		Assert.assertTrue(rs.next());
		Assert.assertTrue(rs.getDate("dateOfInit").equals(oi.getSQLDate()));
		Assert.assertTrue(rs.getDate("LMP").equals(oi.getSQLLMP()));
		//Assert.assertFalse(rs.next());
	}
}
