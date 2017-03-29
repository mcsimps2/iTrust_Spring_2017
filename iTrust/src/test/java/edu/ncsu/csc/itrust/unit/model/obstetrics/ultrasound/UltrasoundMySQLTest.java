package edu.ncsu.csc.itrust.unit.model.obstetrics.ultrasound;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obstetrics.ultrasound.Ultrasound;
import edu.ncsu.csc.itrust.model.obstetrics.ultrasound.UltrasoundMySQL;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

/**
 * Test for ultrasound my sql class
 * @author jcgonzal
 */
public class UltrasoundMySQLTest {

	/** Object to test */
	UltrasoundMySQL usql;
	
	/** Data source for db */
	DataSource ds;
	
	/** Dummy data for testing */
	Ultrasound[] uArr;
	
	@Before
	public void setUp() throws Exception {
		DBBuilder.main(null);
		TestDataGenerator.main(null);
		ds = ConverterDAO.getDataSource();
		usql = new UltrasoundMySQL(ds);
		
		uArr = new Ultrasound[2];
		uArr[0] = new Ultrasound(new Long(1), new Long(51), new Float(1.1), new Float(2.2), new Float(3.3), new Float(4.4), new Float(5.5), new Float(6.6), new Float(7.7), new Float(8.8));
		uArr[1] = new Ultrasound(new Long(2), new Long(51), new Float(1.2), new Float(2.3), new Float(3.4), new Float(4.5), new Float(5.6), new Float(6.7), new Float(7.8), new Float(8.9));
	}

	@Test
	public void testConsructor() {
		//This constructor should not be usable by testing
		try
		{
			new UltrasoundMySQL();
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
			Assert.assertTrue(usql.getAll().contains(uArr[0]));
			Assert.assertTrue(usql.getAll().contains(uArr[1]));
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testGetByID() {
		try {
			Assert.assertTrue(usql.getByID(1L).equals(uArr[0]));
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testAdd() {
		Ultrasound toAdd = new Ultrasound(new Long(51), new Float(-13.1), new Float(2.2), new Float(3.3), new Float(4.4), new Float(5.5), new Float(6.6), new Float(7.7), new Float(8.8));
		try {
			usql.add(toAdd);
			Assert.fail("Mistake in validation");
		} catch (DBException e) {
			//This should happen
		} catch (FormValidationException e) {
			Assert.fail(e.getMessage());
		}
		
		toAdd.setCrl(new Float(13.1));
		try {
			Assert.assertTrue(usql.add(toAdd));
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		} catch (FormValidationException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testUpdate() {
		Ultrasound toAdd = new Ultrasound(new Long(51), new Float(-13.1), new Float(2.2), new Float(3.3), new Float(4.4), new Float(5.5), new Float(6.6), new Float(7.7), new Float(8.8));
		try {
			usql.add(toAdd);
			Assert.fail("Mistake in validation");
		} catch (DBException e) {
			//This should happen
		} catch (FormValidationException e) {
			Assert.fail(e.getMessage());
		}
		
		toAdd.setCrl(new Float(13.1));
		try {
			Assert.assertTrue(usql.add(toAdd));
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		} catch (FormValidationException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testGetByOfficeVisit() {
		try {
			Assert.assertTrue(usql.getByOfficeVisit(51L).contains(uArr[0]));
			Assert.assertTrue(usql.getByOfficeVisit(51L).contains(uArr[1]));
			Assert.assertTrue(usql.getByOfficeVisit(125L).isEmpty());
		}  catch (DBException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testDelete() {
		try {
			Assert.assertTrue(usql.delete(1L));
			Assert.assertFalse(usql.delete(500L));
		}  catch (DBException e) {
			Assert.fail(e.getMessage());
		}
	}

}
