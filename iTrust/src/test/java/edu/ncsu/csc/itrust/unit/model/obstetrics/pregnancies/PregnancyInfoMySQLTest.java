package edu.ncsu.csc.itrust.unit.model.obstetrics.pregnancies;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.*;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.DeliveryMethod;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.PregnancyInfo;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.PregnancyInfoMySQL;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.PregnancyInfoSQLLoader;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class PregnancyInfoMySQLTest {
	PregnancyInfoMySQL pisql;
	DataSource ds;
	PregnancyInfoSQLLoader loader;
	PregnancyInfo[] piArr;
	
	@Before
	public void setup() throws Exception
	{
		// Reset test data
		DBBuilder.rebuildAll();		
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
				
		ds = ConverterDAO.getDataSource();
		pisql = new PregnancyInfoMySQL(ds);
		loader = new PregnancyInfoSQLLoader();
		
		piArr = new PregnancyInfo[2];
		piArr[0] = new PregnancyInfo(2, 1, 2014, 200, 10, 20, DeliveryMethod.VAGINAL_DELIVERY, 1);
		piArr[1] = new PregnancyInfo(3, 1, 2016, 300, 18, 25, DeliveryMethod.VAGINAL_DELIVERY, 2);
	}
	
	@Test
	public void testGetRecords()
	{
		try {
			List<PregnancyInfo> results = pisql.getRecords(1);
			for (int i = 0; i < piArr.length; i++)
			{
				Assert.assertTrue(results.get(i).equals(piArr[i]));
			}
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
		
	}
	
	@Test
	public void testGetRecordByID()
	{
		//First, get a record
		try
		{
			List<PregnancyInfo> results = pisql.getRecords(1);
			for (int i = 0; i < piArr.length; i++)
			{
				Assert.assertTrue(results.get(i).equals(piArr[i]));
			}
			PregnancyInfo result = results.get(0);
			//Now try to get that same record by id
			Assert.assertTrue(pisql.getByID(result.getID()).equals(result));
		}
		catch (DBException e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testAdd() 
	{
		//Try a valid add
		PregnancyInfo pi1 = new PregnancyInfo(2, 1, 2015, 500, 18, 35, DeliveryMethod.VAGINAL_DELIVERY, 4);
		try
		{
			pisql.add(pi1);
			List<PregnancyInfo> results = pisql.getRecords(1);
			Assert.assertTrue(results.contains(pi1));
			
		}
		catch (DBException e)
		{
			Assert.fail(e.getMessage());
		}
		
		//Try an invalid add
		PregnancyInfo pi2 = new PregnancyInfo(999999999, 1, 2015, 500, 18, 35, DeliveryMethod.VAGINAL_DELIVERY, 4);
		try
		{
			pisql.add(pi2);
			Assert.fail("Added an invalid record to priorPregnancies");
		}
		catch (DBException e)
		{
			Assert.assertTrue(true);
		}
		
	}
	
	@Test
	public void testConstructor()
	{
		try
		{
			new PregnancyInfoMySQL();
			Assert.fail("Should not have been able to call default constructor");
		}
		catch (DBException e)
		{
			Assert.assertNotNull(e);
		}
	}
	
	@Test
	public void testGetRecordsFromInit()
	{
		try
		{
			//See if this works for the second record for patient 1 in the DB
			List<PregnancyInfo> list = pisql.getRecordsFromInit(piArr[1].getObstetricsInitID());
			Assert.assertEquals(piArr.length, list.size());
			for (int i = 0; i < piArr.length; i++)
			{
				Assert.assertTrue(list.contains(piArr[i]));
			}
			
			//See if this works for the first record for patient 1 in the DB
			list = pisql.getRecordsFromInit(piArr[0].getObstetricsInitID());
			Assert.assertEquals(1, list.size());
			Assert.assertEquals(piArr[0], list.get(0));
			
			//Add another record
			PregnancyInfo pi = new PregnancyInfo(3, 1, 2000, 500, 10, 20, DeliveryMethod.VAGINAL_DELIVERY, 1);
			pisql.add(pi);
			//See if everything still works
			//Testing for obstetricsInitID 2
			list = pisql.getRecordsFromInit(3);
			Assert.assertEquals(3, list.size());
			for (int i = 0; i < piArr.length; i++)
			{
				Assert.assertTrue(list.contains(piArr[i]));
			}
			Assert.assertTrue(list.contains(pi));
		}
		catch (DBException e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetAll()
	{
		try
		{
			//See if this works for the second record in the DB
			List<PregnancyInfo> list = pisql.getAll();
			for (int i = 0; i < piArr.length; i++)
			{
				Assert.assertTrue(list.contains(piArr[i]));
			}
		}
		catch (DBException e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testUpdate()
	{
		try
		{
			pisql.update(piArr[0]);
			Assert.fail("Called an unimplemented method");
		}
		catch (IllegalStateException e)
		{
			Assert.assertNotNull(e);
		}
		catch (DBException e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testDiabolical() throws Exception
	{
		//Try dropping the databases right before an add
		try {
			Connection conn = ds.getConnection();
			String stmt = "DROP TABLE priorPregnancies";
			conn.prepareStatement(stmt).execute();
		} catch (SQLException e) {
			Assert.fail(e.getMessage());
		}
		
		try
		{
			pisql.getRecords(1);
			Assert.fail("Functioned without databases");
		}
		catch (DBException e)
		{
			Assert.assertNotNull(e);
		}
		
		try
		{
			pisql.getAll();
			Assert.fail("Functioned without databases");
		}
		catch (DBException e)
		{
			Assert.assertNotNull(e);
		}
		
		try
		{
			pisql.add(piArr[0]);
			Assert.fail("Functioned without databases");
		}
		catch (DBException e)
		{
			Assert.assertNotNull(e);
		}
		
		try
		{
			pisql.getByID(1);
			Assert.fail("Functioned without databases");
		}
		catch (DBException e)
		{
			Assert.assertNotNull(e);
		}
		
		try
		{
			pisql.getRecordsFromInit(1);
			Assert.fail("Functioned without databases");
		}
		catch (DBException e)
		{
			Assert.assertNotNull(e);
		}
		
		
		//Now rebuild everything to not screw up the whole system
		DBBuilder.rebuildAll();		
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
	}
	
	@Test
	public void random()
	{
		try
		{
			List<PregnancyInfo> list = pisql.getRecordsFromInit(1);
			System.out.println(list.size());
			list = pisql.getRecordsFromInit(2);
			System.out.println(list.size());
		} catch (DBException e)
		{
			
		}
	}
}
