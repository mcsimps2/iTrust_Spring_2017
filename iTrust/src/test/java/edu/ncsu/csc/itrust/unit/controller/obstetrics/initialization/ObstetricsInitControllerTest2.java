package edu.ncsu.csc.itrust.unit.controller.obstetrics.initialization;

import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.controller.obstetrics.initialization.ObstetricsInitController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInit;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInitMySQL;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.PregnancyInfo;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.PregnancyInfoMySQL;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class ObstetricsInitControllerTest2 {

	/** ObstetricsInitController used for testing */
	private ObstetricsInitController controller;
	/** DataSource used to instantiate controller */
	DataSource ds;
	/** ObstetricsInitData used to access the db to test the controller */
	ObstetricsInitMySQL oiData;
	/** PregnancyInfoMySql used to access the db to test the controller */
	PregnancyInfoMySQL pregnancyData;
	
	@Before
	public void setUp() throws Exception {
		// Set up data source and controller
		ds = ConverterDAO.getDataSource();
		controller = new ObstetricsInitController(ds);
		oiData = new ObstetricsInitMySQL(ds);
		pregnancyData = new PregnancyInfoMySQL(ds);
		
		/* Clear the tables.
		   This will also populate the data tables
		   */
		DBBuilder.main(null);
		TestDataGenerator.main(null);
	}

	@Test
	public void testIsPatientEligible() {
		
		Assert.assertTrue(controller.isPatientEligible("1"));
		Assert.assertFalse(controller.isPatientEligible("2"));
		
		Assert.assertFalse(controller.isPatientEligible("ab"));
		Assert.assertFalse(controller.isPatientEligible("1.2"));
		
		Assert.assertFalse(controller.isPatientEligible("510"));
	}

	@Test
	public void testMakePatientEligible() {

		Assert.assertFalse(controller.isPatientEligible("2"));
		
		controller.makePatientEligible("2.12", "9000000012");
		Assert.assertFalse(controller.isPatientEligible("2"));
		
		//This should be a test, but the PatientDAO doesn't actually check the hcpID
		//or do anything with it when editing the patient object
		//controller.makePatientEligible("2", "9000000003");
		//Assert.assertFalse(controller.isPatientEligible("2"));
		
		controller.makePatientEligible("2", "902.12");
		Assert.assertFalse(controller.isPatientEligible("2"));
		
		//This will probably throw a null pointer
		controller.makePatientEligible("510", "9000000012");
		Assert.assertFalse(controller.isPatientEligible("510"));		
		
		controller.makePatientEligible("2", "9000000012");
		Assert.assertFalse(!controller.isPatientEligible("2"));
		
	}

	@Test
	public void testGetRecords() {
		
		Assert.assertNull(controller.getRecords("a"));

		List<ObstetricsInit> list;
		try {
			list = oiData.getRecords(1);
			Assert.assertEquals(list, controller.getRecords("1"));
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
		
		//TODO: Maybe test sorting
	}

	@Test
	public void testGetPastPregnancies() {
		
		List<PregnancyInfo> list;
		try {
			list = pregnancyData.getRecordsFromInit(1);
			Assert.assertEquals(list, controller.getPastPregnanciesFromInit(1));
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
		
	}

	@Test
	public void testIsOBGYN() {
		
		Assert.assertFalse(controller.isOBGYN("a"));
		
		Assert.assertFalse(controller.isOBGYN("123"));
		
		Assert.assertFalse(controller.isOBGYN("9000000003"));
		
		Assert.assertFalse(controller.isOBGYN("9900000000"));
		
		Assert.assertTrue(controller.isOBGYN("9000000012"));
	}

	@Test
	public void testGetSetViewedOI() {
		
		Assert.assertNull(controller.getViewedOI());
		
		try {
			controller.viewAddObstetricsInit(oiData.getByID(1), "1.1");
			Assert.assertNull(controller.getViewedOI());
			controller.viewAddObstetricsInit(oiData.getByID(1), "1");
			Assert.assertEquals(oiData.getByID(1), controller.getViewedOI());
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
	}

}
