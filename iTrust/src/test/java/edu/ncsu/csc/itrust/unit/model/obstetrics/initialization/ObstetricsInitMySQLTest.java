package edu.ncsu.csc.itrust.unit.model.obstetrics.initialization;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.*;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInit;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInitMySQL;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInitSQLLoader;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class ObstetricsInitMySQLTest {
	ObstetricsInitMySQL oisql;
	ObstetricsInitSQLLoader loader;
	DataSource ds;
	/** Holds records already in the database */
	ObstetricsInit[] oiArr;
	
	/**
	 * Sets up the test environment and variables
	 * @throws Exception
	 */
	@Before
	public void setup() throws Exception
	{
		DBBuilder.main(null);
		TestDataGenerator.main(null);
		ds = ConverterDAO.getDataSource();
		oisql = new ObstetricsInitMySQL(ds);
		loader = new ObstetricsInitSQLLoader();
		
		//Pickup some values already in the DB
		oiArr = new ObstetricsInit[2];
		oiArr[0] = new ObstetricsInit(1, "2017-03-16", "2017-01-01");
		oiArr[1] = new ObstetricsInit(1, "2016-02-03", "2015-11-21");
	}
	
	/**
	 * Verifies we can't use the default constructor for testing
	 */
	@Test
	public void testConstructor()
	{
		//This constructor should not be usable by testing
		try
		{
			new ObstetricsInitMySQL();
			Assert.fail("Constructor should have thrown a DBException");
		}
		catch (DBException e)
		{
			Assert.assertTrue(true);
		}
	}
	
	/**
	 * Verifies we can get records by an ID
	 */
	@Test
	public void testGetRecords()
	{
		try {
			List<ObstetricsInit> results = oisql.getRecords(1);
			for (int i = 0; i < oiArr.length; i++)
			{
				Assert.assertTrue(results.contains(oiArr[i]));
			}
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	/**
	 * Verifies we can retrieve a record by its ID
	 */
	@Test
	public void testGetByID()
	{
		try
		{
			//Try an existing record
			List<ObstetricsInit> results = oisql.getRecords(1);
			ObstetricsInit result = results.get(0);
			Assert.assertTrue(oisql.getByID(result.getID()).equals(result));
			
			//Try a record that doesn't exist
			Assert.assertNull(oisql.getByID(0));
		}
		catch (DBException e) {
			Assert.fail(e.getMessage());
		}
		
	}
	
	/**
	 * Verifies we can add valid data to the database and catch invalid data
	 * For a more thorough valid-invalid data testing, reference ObstetricsInitValidatorTest.java
	 */
	@Test
	public void testAdd()
	{
		//Valid adds
		ObstetricsInit[] toAddValid = {
				new ObstetricsInit(1, "2000-01-01", "1999-11-11"),
				new ObstetricsInit(1, "2017-01-01", "2016-05-05"),
				new ObstetricsInit(1, "2017-02-02", "2017-02-02"),
				new ObstetricsInit(1, "2017-02-02", "2017-02-01")
		};
		for (int i = 0; i < toAddValid.length; i++)
		{
			try
			{
				Assert.assertTrue(oisql.add(toAddValid[i]));
			}
			catch (DBException e)
			{
				Assert.fail(e.getMessage());
			}
		}
		
		try {
			List<ObstetricsInit> results = oisql.getRecords(1);
			for (int i = 0; i < toAddValid.length; i++)
			{
				Assert.assertTrue(results.contains(toAddValid[i]));
			}
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
		
		
		//Invalid adds
		ObstetricsInit[] toAddInvalid = {
				new ObstetricsInit(2, "2000-01-01", "1999-11-11"), //Patient 2 is not obstetrics eligible
				new ObstetricsInit(1, "1999-11-11", "2000-01-01"), //Initialization date is before LMP
				new ObstetricsInit(1, "2015-05-05", "2015-05-06") //Check for off by 1 errors
		};
		
		for (int i = 0; i < toAddInvalid.length; i++)
		{
			try
			{
				oisql.add(toAddInvalid[i]);
				Assert.fail("Added an invalid record");
			}
			catch (DBException e)
			{
				Assert.assertNotNull(e);
			}
		}
	}
	
	/**
	 * Verifies that the update method is unimplemented and can't be used
	 */
	@Test
	public void testUpdate()
	{
		ObstetricsInit oi = new ObstetricsInit(1, "2016-02-03", "2015-10-21");
		try
		{
			oisql.update(oi);
			Assert.fail("Did not catch an exception from an unimplemented method in the loader");
		} catch (IllegalStateException e)
		{
			Assert.assertNotNull(e);
		}
		catch (DBException e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	/**
	 * Verifies we can retrieve every obstetrics record in the database
	 */
	@Test
	public void testGetAll()
	{
		List<ObstetricsInit> oiList;
		try
		{
			oiList = oisql.getAll();
			for (int i = 0; i < oiArr.length; i++)
			{
				Assert.assertTrue(oiList.contains(oiArr[i]));
			}
		}
		catch (DBException e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	/**
	 * Attempts to drop the database to generate exceptional cases for testing (helps with coverage)
	 * @throws Exception
	 */
	@Test
	public void testDiabolical() throws Exception
	{
		//Try dropping the databases right before an add
		try {
			Connection conn = ds.getConnection();
			String stmt = "DROP TABLE obstetricsInit";
			conn.prepareStatement(stmt).execute();
		} catch (SQLException e) {
			Assert.fail(e.getMessage());
		}
		
		try
		{
			oisql.getRecords(1);
			Assert.fail("Functioned without databases");
		}
		catch (DBException e)
		{
			Assert.assertNotNull(e);
		}
		
		try
		{
			oisql.getAll();
			Assert.fail("Functioned without databases");
		}
		catch (DBException e)
		{
			Assert.assertNotNull(e);
		}
		
		try
		{
			oisql.add(oiArr[0]);
			Assert.fail("Functioned without databases");
		}
		catch (DBException e)
		{
			Assert.assertNotNull(e);
		}
		
		try
		{
			oisql.getByID(1);
			Assert.fail("Functioned without databases");
		}
		catch (DBException e)
		{
			Assert.assertNotNull(e);
		}
		
		//Now rebuild everything to not screw up the whole system
		DBBuilder.main(null);
		TestDataGenerator.main(null);
	}
}
