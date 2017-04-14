package edu.ncsu.csc.itrust.unit.model.obstetrics.pregnancies;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.*;

import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.DeliveryMethod;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.PregnancyInfo;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.PregnancyInfoSQLLoader;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class PregnancyInfoSQLLoaderTest {
	PregnancyInfoSQLLoader loader;
	
	@Before
	public void setup() throws Exception
	{
		// Reset test data
		DBBuilder.rebuildAll();		
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
		
		loader = new PregnancyInfoSQLLoader();
	}
	
	@Test
	public void testInsert() throws SQLException
	{
		DataSource ds = ConverterDAO.getDataSource();
		try (Connection conn = ds.getConnection();)
		{
			PregnancyInfo pi = new PregnancyInfo(1, 1, 2017, 270, 15, 25, DeliveryMethod.CAESAREAN_SECTION, 1);
			try (PreparedStatement ps = loader.loadParameters(conn, null, pi, true);)
			{
				ps.executeUpdate();
			}
			
			String stmt = "SELECT * FROM priorPregnancies WHERE pid=1";
			try (PreparedStatement ps = conn.prepareStatement(stmt);)
			{
				List<PregnancyInfo> list = loader.loadList(ps.executeQuery());
				Assert.assertTrue(list.contains(pi));
			}
		}
		catch (SQLException e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testUpdate() throws SQLException
	{
		//Go ahead and insert something
		DataSource ds = ConverterDAO.getDataSource();
		try (Connection conn = ds.getConnection();)
		{
			PregnancyInfo pi = new PregnancyInfo(1, 1, 2014, 500, 150, 250, DeliveryMethod.CAESAREAN_SECTION, 8);
			try (PreparedStatement ps = loader.loadParameters(conn, null, pi, true);)
			{
				ps.executeUpdate();
			}
			
			//Try to update it
			pi.setNumDaysPregnant(1000);
			pi.setID(1);
			try
			{
				try (PreparedStatement ps = loader.loadParameters(conn, null, pi, false);)
				{
					ps.executeUpdate();
				}
				Assert.fail("Called an unimplemented method");
			} catch (IllegalStateException e)
			{
				Assert.assertTrue(true);
			}
		}
		catch (SQLException e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
}
