package edu.ncsu.csc.itrust.unit.model.obstetrics.pregnancies;

import java.io.IOException;
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
		DBBuilder.main(null);
		TestDataGenerator.main(null);
		loader = new PregnancyInfoSQLLoader();
	}
	
	@Test
	public void testInsert() throws SQLException
	{
		DataSource ds = ConverterDAO.getDataSource();
		Connection conn = ds.getConnection();
		PreparedStatement ps = null;
		PregnancyInfo pi = new PregnancyInfo(1, 1, 2017, 270, 15, 25, DeliveryMethod.CAESAREAN_SECTION, 1);
		ps = loader.loadParameters(conn, ps, pi, true);
		ps.executeUpdate();
		
		String stmt = "SELECT * FROM priorPregnancies WHERE pid=1";
		ps = conn.prepareStatement(stmt);
		List<PregnancyInfo> list = loader.loadList(ps.executeQuery());
		Assert.assertTrue(list.contains(pi));
	}
	
	@Test
	public void testUpdate() throws SQLException
	{
		//Go ahead and insert something
		DataSource ds = ConverterDAO.getDataSource();
		Connection conn = ds.getConnection();
		PreparedStatement ps = null;
		PregnancyInfo pi = new PregnancyInfo(1, 1, 2014, 500, 150, 250, DeliveryMethod.CAESAREAN_SECTION, 8);
		ps = loader.loadParameters(conn, ps, pi, true);
		ps.executeUpdate();
		
		//Try to update it
		pi.setNumDaysPregnant(1000);
		pi.setRecordID(1);
		ps = loader.loadParameters(conn, ps, pi, false);
		ps.executeUpdate();
		
		//Now check
		String stmt = "SELECT * FROM priorPregnancies WHERE pid=1";
		ps = conn.prepareStatement(stmt);
		List<PregnancyInfo> list = loader.loadList(ps.executeQuery());
		Assert.assertTrue(list.contains(pi));
	}
	
}
