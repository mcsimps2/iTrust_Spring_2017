package edu.ncsu.csc.itrust.unit.model.fitness;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.*;


import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.fitness.FitnessInfo;
import edu.ncsu.csc.itrust.model.fitness.FitnessInfoMySQL;
import edu.ncsu.csc.itrust.model.fitness.FitnessInfoSQLLoader;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

/**
 * Tests the FitnessInfoMySQL object
 * @author mcsimps2
 *
 */
public class FitnessInfoMySQLTest {
	
	FitnessInfoMySQL fisql;
	FitnessInfoSQLLoader loader;
	DataSource ds;
	/* Sample fitness info objects to test against.  These are entries already existing in the test database */
	FitnessInfo fi1, fi2, fi3, fi4;
	/* Dates of today, tomorrow, and yesterday */
	String currDate, yesterday, tmrw;
	
	/**
	 * Setup the tests, include generating the dates of today, tomorrow, and yesterday
	 * Make sample FitnessInfo objects
	 * Run TestDataGenerator to generate the sample data
	 * @throws DBException
	 * @throws SQLException
	 * @throws IOException
	 */
	@Before
	public void setup() throws DBException, SQLException, IOException
	{
		TestDataGenerator.main(null);
		
		ds = ConverterDAO.getDataSource();
		fisql = new FitnessInfoMySQL(ds);
		loader = new FitnessInfoSQLLoader();
		
		//Clear the tables
		//TestDataGenerator.main(null);
		
		//This will also populate the data tables, including the fitness tables
		//See fitnessData.sql for the values in the table put in there
		//by the test data generator
		//Create some FitnessInfos we know exist
		//First, get the current date, yesterday, and tomorrow
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date dcurrDate = new Date();
		Date dyesterday = DateUtils.addDays(new Date(), -1);
		Date dtmrw = DateUtils.addDays(new Date(), 1);
		currDate = formatter.format(dcurrDate);
		yesterday = formatter.format(dyesterday);
		tmrw = formatter.format(dtmrw);
		
		//These here are existing entries in the database
		fi1 = new FitnessInfo(1, tmrw, 500, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		fi2 = new FitnessInfo(1, currDate, 9255, 5000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		fi3 = new FitnessInfo(1, yesterday, 5252, 0, 3.25, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		fi4 = new FitnessInfo(2, yesterday, 5555, 0, 5.53, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
	}
	
	/**
	 * Get all the entries in the fitness database
	 * Make sure the results include the ones we know are already in the database
	 */
	@Test
	public void testGetAll()
	{
		try
		{
			List<FitnessInfo> list = fisql.getAll();
			
			Assert.assertTrue(list.contains(fi1));
			Assert.assertTrue(list.contains(fi2));
			Assert.assertTrue(list.contains(fi3));
			Assert.assertTrue(list.contains(fi4));
		}
		catch (DBException e)
		{
			Assert.fail("Got a DBException in GetAll");
		}
	}
	
	/**
	 * Make sure we can get a fitness info object by the pid
	 */
	@Test
	public void testGetById()
	{
		try
		{
			FitnessInfo fi = fisql.getByID(1);
			
			FitnessInfo expected = new FitnessInfo(1, "2016-12-06", 58, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0); 
			Assert.assertTrue(fi.equals(expected));
		}
		catch (DBException e)
		{
			Assert.fail("Got a DBException in GetById");
		}
	}
	
	/**
	 * Tests that we can get fitness info for a given pid and
	 * (a) a single date
	 * (b) a span of dates from start date to end date
	 */
	@Test
	public void testGetFitnessInfo()
	{
		//Single date
		try
		{
			FitnessInfo fi = fisql.getFitnessInfo(1, currDate);
			Assert.assertTrue(fi.equals(fi2));
			fi = fisql.getFitnessInfo(2, yesterday);
			Assert.assertTrue(fi.equals(fi4));
		}
		catch (DBException e)
		{
			Assert.fail(e.getMessage());
		}
		//Span of dates
		try
		{
			FitnessInfo[] fi = fisql.getFitnessInfo(1, yesterday, currDate);
			
			List<FitnessInfo> fiList = Arrays.asList(fi);
			Assert.assertFalse(fiList.contains(fi1));
			Assert.assertTrue(fiList.contains(fi2));
			Assert.assertTrue(fiList.contains(fi3));
			Assert.assertFalse(fiList.contains(fi4));
		}
		catch (DBException e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	/**
	 * Tests that we can remove fitness info given a pid and
	 * (a) a single date
	 * or
	 * (b) a span of dates, from start date to end date
	 * @throws IOException
	 * @throws SQLException
	 */
	@Test
	public void testRemoveFitnessInfo() throws IOException, SQLException
	{
		//single date
		try
		{
			boolean removed = fisql.removeFitnessInfo(1, currDate);
			Assert.assertTrue(removed);
			FitnessInfo fi = fisql.getFitnessInfo(1, currDate);
			Assert.assertNull(fi);
		}
		catch (DBException e)
		{
			Assert.fail(e.getMessage());
		}
		
		//Span of dates
		try
		{
			boolean removed = fisql.removeFitnessInfo(1, yesterday, currDate);
			Assert.assertTrue(removed);
			FitnessInfo fi = fisql.getFitnessInfo(1, tmrw);
			Assert.assertTrue(fi.equals(fi1));
			FitnessInfo[] fiArr = fisql.getFitnessInfo(1, yesterday, currDate);
			Assert.assertNull(fiArr);
		}
		catch (DBException e)
		{
			Assert.fail(e.getMessage());
		}
		finally
		{
			//Repopulate table
			TestDataGenerator.main(null);
		}
	}

	/**
	 * Ensures we can insert and update values in the database
	 * Tests updates for data whose pid and date already exist in the database
	 * Tests inserts for data whose pid and date are not already in the database
	 * Tests that invalid objects are not successfully added
	 */
	@Test
	public void testAdd()
	{
		
		//Try doing an update
		//We know we have something for the current day, so modify that
		FitnessInfo fi = new FitnessInfo(1, currDate, 9256, 5000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		//In the database, this date for patient 1 should have 9255 steps, we update to 9256 steps
		try
		{
			fisql.add(fi);
			//now get the fitness info for that date
			FitnessInfo returnedFi = fisql.getFitnessInfo(1, currDate);
			Assert.assertTrue(fi.equals(returnedFi));
		}
		catch (DBException | FormValidationException e)
		{
			Assert.fail(e.getMessage());
			e.printStackTrace();
		}
		
		//Try doing an insert
		fi = new FitnessInfo(1, "2015-05-05", 1000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		try
		{
			fisql.add(fi);
			FitnessInfo returnedFi = fisql.getFitnessInfo(1, "2015-05-05");
			Assert.assertTrue(fi.equals(returnedFi));
		}
		catch (DBException | FormValidationException e)
		{
			Assert.fail(e.getMessage());
			e.printStackTrace();
		}
		
		//Make sure this one isn't added
		//Andy programmer already died
		fi = new FitnessInfo(2, currDate, 1000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		try
		{
			fisql.add(fi);
			Assert.fail("Added info after the date of death");
		}
		catch (DBException | FormValidationException e)
		{
			//Should have passed
		}
		fi = new FitnessInfo(2, "1900-01-01", 1000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		try
		{
			fisql.add(fi);
			Assert.fail("Added info before date of birth");
		}
		catch (DBException | FormValidationException e)
		{
			//Should have passed
		}
	}
	
	@Test
	public void testAddBoundaries()
	{
		//Test boundaries for PID
		FitnessInfo fi1 = new FitnessInfo(0, "2000-10-11", 500, 200, 1, 3, 5, 7, 9, 10, 12, 15, 16, 17, 18, 19);
		try
		{
			fisql.add(fi1);
			Assert.fail("Added something with an invalid PID");
		}
		catch (DBException | FormValidationException e)
		{
			Assert.assertNotNull(e);
		}
		FitnessInfo fi2 = new FitnessInfo(1, "2000-10-11", 500, 200, 1, 3, 5, 7, 9, 10, 12, 15, 16, 17, 18, 19);
		try
		{
			Assert.assertTrue(fisql.add(fi2));
		}
		catch (DBException | FormValidationException e)
		{
			Assert.fail("Did not add a valid entry");
		}
		FitnessInfo fi3 = new FitnessInfo(2, "1990-10-11", 500, 200, 1, 3, 5, 7, 9, 10, 12, 15, 16, 17, 18, 19);
		try
		{
			
			Assert.assertTrue(fisql.add(fi3));
		}
		catch (DBException | FormValidationException e)
		{
			Assert.fail("Did not add a valid entry");
		}
		
		//Test boundaries for calories active
		fi1 = new FitnessInfo(1, "2000-10-11", 1, 200, 1, 3, -1, 7, 9, 10, 12, 15, 16, 17, 18, 19);
		try
		{
			fisql.add(fi1);
			Assert.fail("Added something with an invalid active calorie count");
		}
		catch (DBException | FormValidationException e)
		{
			Assert.assertNotNull(e);
		}
		fi2 = new FitnessInfo(1, "2000-10-11", 1, 200, 1, 3, 5, 0, 9, 10, 12, 15, 16, 17, 18, 19);
		try
		{
			Assert.assertTrue(fisql.add(fi2));
		}
		catch (DBException | FormValidationException e)
		{
			Assert.fail("Did not add a valid entry");
		}
		fi3 = new FitnessInfo(1, "2000-10-11", 1, 200, 1, 3, 5, 7, 1, 10, 12, 15, 16, 17, 18, 19);
		try
		{
			
			Assert.assertTrue(fisql.add(fi3));
		}
		catch (DBException | FormValidationException e)
		{
			Assert.fail("Did not add a valid entry");
		}
		
		//Test boundaries for calories active
		fi1 = new FitnessInfo(1, "2000-10-11", 1, 200, 1, 3, 201, 7, 9, 10, 12, 15, 16, 17, 18, 19);
		try
		{
			fisql.add(fi1);
			Assert.fail("Added something with an invalid active calorie count");
		}
		catch (DBException | FormValidationException e)
		{
			Assert.assertNotNull(e);
		}
		fi2 = new FitnessInfo(1, "2000-10-11", 1, 200, 1, 3, 5, 200, 9, 10, 12, 15, 16, 17, 18, 19);
		try
		{
			Assert.assertTrue(fisql.add(fi2));
		}
		catch (DBException | FormValidationException e)
		{
			Assert.fail("Did not add a valid entry");
		}
		fi3 = new FitnessInfo(1, "2000-10-11", 1, 200, 1, 3, 5, 7, 199, 10, 12, 15, 16, 17, 18, 19);
		try
		{
			
			Assert.assertTrue(fisql.add(fi3));
		}
		catch (DBException | FormValidationException e)
		{
			Assert.fail("Did not add a valid entry");
		}
		
	}
	
	@Test
	public void testConstructor()
	{
		//The default constructor should not be working for JUnit test cases
		FitnessInfoMySQL fisql = null;
		try
		{
			fisql = new FitnessInfoMySQL();
			Assert.fail("Created a FitnessInfoMySQL when should not have been able to");
		}
		catch (DBException e)
		{
			//Good!
			Assert.assertNull(fisql);
		}
	}
	
	/*
	@Test
	public void invalidRemoveFitnessInfo()
	{
		try
		{
			fisql.removeFitnessInfo(1, "abcd");
			Assert.fail("Allowed an invalid date");
		}
		catch (DBException e)
		{
			//Good!
			Assert.assertNotNull(e);
		}
		try
		{
			fisql.removeFitnessInfo(1, "1996-01-01", "abcdefg");
			Assert.fail("Allowed an invalid date");
		}
		catch (DBException e)
		{
			//Good!
			Assert.assertNotNull(e);
		}
	}
	*/
	
	@Test
	public void invalidGetFitnessInfo()
	{
		try
		{
			fisql.getFitnessInfo(1, "sneaky' WHERE 'oops");
			Assert.fail("Allowed an invalid date");
		}
		catch (DBException e)
		{
			//Good!
			Assert.assertNotNull(e);
		}
		try
		{
			fisql.getFitnessInfo(1, "1996-01-01", "sneaky' WHERE 'oops");
			Assert.fail("Allowed an invalid date");
		}
		catch (DBException e)
		{
			//Good!
			Assert.assertNotNull(e);
		}
	}
}
