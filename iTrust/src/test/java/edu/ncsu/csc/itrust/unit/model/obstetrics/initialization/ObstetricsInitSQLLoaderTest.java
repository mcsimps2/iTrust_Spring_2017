package edu.ncsu.csc.itrust.unit.model.obstetrics.initialization;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInit;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInitSQLLoader;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class ObstetricsInitSQLLoaderTest 
{
	ObstetricsInitSQLLoader loader;
	DataSource ds;
	ObstetricsInit[] oiArr;
	
	@Before
	public void setup() throws Exception
	{
		// Reset test data
		DBBuilder.rebuildAll();		
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
		
		loader = new ObstetricsInitSQLLoader();
		ds = ConverterDAO.getDataSource();
		
		oiArr = new ObstetricsInit[2];
		oiArr[0] = new ObstetricsInit(1, "2017-03-16", "2017-01-01");
		oiArr[1] = new ObstetricsInit(1, "2016-02-03", "2015-11-21");
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
		try
		{
			ps = loader.loadParameters(conn, ps, oi, false);
			ps.executeUpdate();
			Assert.fail("Called an unimplemented method");
		} catch (IllegalStateException e)
		{
			Assert.assertTrue(true);
		}
	}
	
	@Test
	public void testLoadList()
	{
		try {
			Connection conn = ds.getConnection();
			String stmt = "SELECT * FROM obstetricsInit";
			PreparedStatement ps = conn.prepareStatement(stmt);
			ResultSet rs = ps.executeQuery();
			List<ObstetricsInit> list = loader.loadList(rs);
			for (int i = 0; i < oiArr.length; i++)
			{
				Assert.assertTrue(list.contains(oiArr[i]));
			}
		} catch (SQLException e)
		{
			Assert.fail(e.getMessage());
		}
	}
}
