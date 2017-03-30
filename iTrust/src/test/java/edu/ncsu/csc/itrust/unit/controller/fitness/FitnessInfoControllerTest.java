/**
 * 
 */
package edu.ncsu.csc.itrust.unit.controller.fitness;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.controller.fitness.FitnessInfoController;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.fitness.FitnessInfo;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

/**
 * Test class for the FitnessInfoController class.
 * 
 * @author mcsimps2
 * @author amcheshi
 */
public class FitnessInfoControllerTest {
	/** FitnessInfoController used for testing */
	private FitnessInfoController controller;
	/** DataSource used to instantiate controller */
	DataSource ds;
	/** FitnessInfo that we know already exists in the database */
	FitnessInfo fi1, fi2, fi3, fi4;
	/** String holding the dates for today, yesterday, tomorrow, and the next day in the format: YYYY-MM-DD */
	String today, yesterday, tomorrow, tomorrowMorrow;

	/**
	 * Runs before every test.
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// Set up data source and controller
		ds = ConverterDAO.getDataSource();
		controller = new FitnessInfoController(ds);
		
		/* Clear the tables.
		   This will also populate the data tables, including the fitness tables
		   See fitnessData.sql for the values in the table put in there
		   by the test data generator */
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
		
		// Get the dates for today, tomorrow, and yesterday
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date dToday = new Date();
		Date dYesterday = DateUtils.addDays(new Date(), -1);
		Date dTomorrow = DateUtils.addDays(new Date(), 1);
		Date dTomorrowMorrow = DateUtils.addDays(new Date(), 2);
		today = formatter.format(dToday);
		yesterday = formatter.format(dYesterday);
		tomorrow = formatter.format(dTomorrow);
		tomorrowMorrow = formatter.format(dTomorrowMorrow);
		
		// Now use the dates to create FitnessInfo that we know already exists
		fi1 = new FitnessInfo(1, tomorrow, 500, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		fi2 = new FitnessInfo(1, today, 9255, 5000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		fi3 = new FitnessInfo(1, yesterday, 5252, 0, 3.25, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		fi4 = new FitnessInfo(2, yesterday, 5555, 0, 5.53, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
	}

	/**
	 * Test method for addFitnessInfo(FitnessInfo)
	 * {@link edu.ncsu.csc.itrust.controller.fitness.FitnessInfoController#addFitnessInfo(edu.ncsu.csc.itrust.model.fitness.FitnessInfo)}.
	 */
	@Test
	public void testAddFitnessInfo() {
		//Try doing an update
		//We know we have something for the current day, so modify that
		FitnessInfo fi = new FitnessInfo(1, today, 9256, 5000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		//In the database, this date for patient 1 should have 9255 steps, we update to 9256 steps
		controller.addFitnessInfo(fi);
		//now get the fitness info for that date
		FitnessInfo returnedFi = controller.getFitnessInfo(1, today);
		Assert.assertNotNull(returnedFi);
		Assert.assertTrue(fi.equals(returnedFi));
		
		//Try doing an insert
		fi = new FitnessInfo(1, "2015-05-05", 1000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		controller.addFitnessInfo(fi);
		returnedFi = controller.getFitnessInfo(1, "2015-05-05");
		Assert.assertNotNull(returnedFi);
		Assert.assertTrue(fi.equals(returnedFi));
		
		//Make sure this one isn't added, Andy Programmer already died
		fi = new FitnessInfo(2, tomorrowMorrow, 1000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		controller.addFitnessInfo(fi);
		returnedFi = controller.getFitnessInfo(2, tomorrowMorrow);
		Assert.assertNull(returnedFi);
		
		//Make sure this one isn't added, Andy Programmer has not yet been born
		fi = new FitnessInfo(2, "1900-01-01", 1000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		controller.addFitnessInfo(fi);
		returnedFi = controller.getFitnessInfo(2, "1900-01-01");
		Assert.assertNull(returnedFi);
	}

	/**
	 * Test method for getFitnessInfo(long, String)
	 * {@link edu.ncsu.csc.itrust.controller.fitness.FitnessInfoController#getFitnessInfo(long, java.lang.String)}.
	 */
	@Test
	public void testGetFitnessInfo() {
		//Single dates - data exists
		FitnessInfo fi = controller.getFitnessInfo(1, tomorrow);
		Assert.assertNotNull(fi);
		Assert.assertTrue(fi.equals(fi1));
		
		fi = controller.getFitnessInfo(1, today);
		Assert.assertNotNull(fi);
		Assert.assertTrue(fi.equals(fi2));
		
		fi = controller.getFitnessInfo(1, yesterday);
		Assert.assertNotNull(fi);
		Assert.assertTrue(fi.equals(fi3));
		
		fi = controller.getFitnessInfo(2, yesterday);
		Assert.assertNotNull(fi);
		Assert.assertTrue(fi.equals(fi4));
		
		//Single date - data does not exist
		fi = controller.getFitnessInfo(3, today);
		Assert.assertNull(fi);
		
		//Span of dates - data exists
		FitnessInfo[] fiArray = controller.getFitnessInfo(1, yesterday, tomorrow);
		Assert.assertNotNull(fiArray);
		List<FitnessInfo> fiList = Arrays.asList(fiArray);
		Assert.assertEquals(3, fiList.size());
		Assert.assertTrue(fiList.contains(fi1));
		Assert.assertTrue(fiList.contains(fi2));
		Assert.assertTrue(fiList.contains(fi3));
		Assert.assertFalse(fiList.contains(fi4));
		
		fiArray = controller.getFitnessInfo(2, yesterday, tomorrow);
		Assert.assertNotNull(fiArray);
		fiList = Arrays.asList(fiArray);
		Assert.assertEquals(3, fiList.size());
		Assert.assertFalse(fiList.contains(fi1));
		Assert.assertFalse(fiList.contains(fi2));
		Assert.assertFalse(fiList.contains(fi3));
		Assert.assertTrue(fiList.contains(fi4));
		
		//Span of dates - data does not exist
		fiArray = controller.getFitnessInfo(3, yesterday, tomorrow);
		Assert.assertNull(fiArray);
	}

}
