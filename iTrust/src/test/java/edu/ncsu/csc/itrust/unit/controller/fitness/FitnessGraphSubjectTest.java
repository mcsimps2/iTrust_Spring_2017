/**
 * 
 */
package edu.ncsu.csc.itrust.unit.controller.fitness;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.controller.fitness.FitnessGraphSubject;
import edu.ncsu.csc.itrust.controller.fitness.FitnessInfoController.GraphKeyValue;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

/**
 * Test class for the FitnessInfoController class.
 * 
 * @author jcgonzal
 */
public class FitnessGraphSubjectTest {
	/** FitnessInfoController used for testing */
	private FitnessGraphSubject subject;
	/** DataSource used to instantiate controller */
	private DataSource ds;

	/**
	 * Runs before every test.
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		ds = ConverterDAO.getDataSource();
		subject = new FitnessGraphSubject(ds);
		
		/* Clear the tables.
		   This will also populate the data tables, including the fitness tables
		   See fitnessData.sql for the values in the table put in there
		   by the test data generator */
		TestDataGenerator.main(null);
	}

	/**
	 * Test method for updateAndNotify()
	 * {@link edu.ncsu.csc.itrust.controller.fitness.FitnessGraphSubject#updateAndNofity()}.
	 */
	@Test
	public void testUpdateAndNotify() {
		ArrayList<GraphKeyValue> dailyExpected = new ArrayList<GraphKeyValue>();
		dailyExpected.add(new GraphKeyValue("2016-12-06", 58));
		dailyExpected.add(new GraphKeyValue("2016-12-07", 60));
		dailyExpected.add(new GraphKeyValue("2016-12-08", 62));
		ArrayList<GraphKeyValue> deltaExpected = new ArrayList<GraphKeyValue>();
		deltaExpected.add(new GraphKeyValue("2016-12-06", null));
		deltaExpected.add(new GraphKeyValue("2016-12-07", 2));
		deltaExpected.add(new GraphKeyValue("2016-12-08", 2));
		ArrayList<GraphKeyValue> weeklyAvgExpected = new ArrayList<GraphKeyValue>();
		weeklyAvgExpected.add(new GraphKeyValue("2016-12-04", 61));
		
		subject.setStartDate("2016-12-06");
		subject.setEndDate("2016-12-08");
		try {
			subject.updateAndNotify(new Long(1));
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
		
		List<GraphKeyValue> dailyActual = subject.getDailyGraphObserver().getData();
		for (int i = 0; i < dailyActual.size(); i++) {
			Assert.assertTrue(dailyActual.get(i).equals(dailyExpected.get(i)));
		}
	}

}
