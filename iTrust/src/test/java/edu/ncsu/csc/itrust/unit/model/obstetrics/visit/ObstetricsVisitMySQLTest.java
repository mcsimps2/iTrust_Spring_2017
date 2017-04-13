package edu.ncsu.csc.itrust.unit.model.obstetrics.visit;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obstetrics.visit.ObstetricsVisit;
import edu.ncsu.csc.itrust.model.obstetrics.visit.ObstetricsVisitMySQL;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

/**
 * Test for ObstetricsVisitMySQL Class
 * @author jcgonzal
 */
public class ObstetricsVisitMySQLTest {
	
	/** Object to test */
	ObstetricsVisitMySQL ovsql;
	
	/** Data source for accessing db */
	DataSource ds;
	
	ObstetricsVisit ov;
	

	@Before
	public void setUp() throws Exception {
		// Reset test data
		DBBuilder.rebuildAll();		
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
		
		ds = ConverterDAO.getDataSource();
		ovsql = new ObstetricsVisitMySQL(ds);
		
		ov = new ObstetricsVisit(new Long(1), new Long(51), new Long(3), new Integer(22), new Integer(130), new Integer(1), new Boolean(false), null, "image.jpg");
	}

	@Test
	public void testConstructor() {
		//This constructor should not be usable by testing
		try
		{
			new ObstetricsVisitMySQL();
			Assert.fail("Constructor should have thrown a DBException");
		}
		catch (DBException e)
		{
			Assert.assertTrue(true);
		}
	}

	@Test
	public void testGetAll() {
		try {
			Assert.assertTrue(ovsql.getAll().get(0).equals(ov));
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testGetByID() {
		try {
			Assert.assertTrue(ovsql.getByID(1L).equals(ov));
			Assert.assertTrue(ovsql.getByID(500L) == null);
		}  catch (DBException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testAdd() {
		ObstetricsVisit toAdd = new ObstetricsVisit(new Long(51), new Long(3), new Integer(-1), new Integer(41), new Integer(1), new Boolean(false), null, "image.jpg");
		try {
			ovsql.add(toAdd);
			Assert.fail("Mistake in validation");
		} catch (DBException e) {
			//This should happen
		} catch (FormValidationException e) {
			Assert.fail(e.getMessage());
		}
		
		toAdd.setWeeksPregnant(new Integer(36));
		try {
			Assert.assertTrue(ovsql.add(toAdd));
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		} catch (FormValidationException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testUpdate() {
		ObstetricsVisit toAdd = new ObstetricsVisit(new Long(1), new Long(51), new Long(3), new Integer(-1), new Integer(41), new Integer(1), new Boolean(false), null, "image.jpg");
		try {
			ovsql.update(toAdd);
			Assert.fail("Mistake in validation");
		} catch (DBException e) {
			//This should happen
		} catch (FormValidationException e) {
			Assert.fail(e.getMessage());
		}
		
		toAdd.setWeeksPregnant(new Integer(36));
		try {
			Assert.assertTrue(ovsql.update(toAdd));
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		} catch (FormValidationException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testGetByOfficeVisit() {
		try {
			Assert.assertTrue(ovsql.getByOfficeVisit(51L).equals(ov));
			Assert.assertTrue(ovsql.getByOfficeVisit(125L) == null);
		}  catch (DBException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetByObstetricsInit() {
		try {
			Assert.assertTrue(ovsql.getByObstetricsInit(3L).get(1).equals(ov));
			Assert.assertTrue(ovsql.getByObstetricsInit(125L).isEmpty());
		}  catch (DBException e) {
			Assert.fail(e.getMessage());
		}
	}

}
