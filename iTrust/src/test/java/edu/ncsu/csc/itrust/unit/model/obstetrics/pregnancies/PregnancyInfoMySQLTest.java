package edu.ncsu.csc.itrust.unit.model.obstetrics.pregnancies;

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
		DBBuilder.main(null);
		TestDataGenerator.main(null);
		ds = ConverterDAO.getDataSource();
		pisql = new PregnancyInfoMySQL(ds);
		loader = new PregnancyInfoSQLLoader();
		
		piArr = new PregnancyInfo[2];
		piArr[0] = new PregnancyInfo(1, 1, 2016, 300, 18, 25, DeliveryMethod.VAGINAL_DELIVERY, 2);
		piArr[1] = new PregnancyInfo(2, 1, 2014, 200, 10, 20, DeliveryMethod.VAGINAL_DELIVERY, 1);
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
}
