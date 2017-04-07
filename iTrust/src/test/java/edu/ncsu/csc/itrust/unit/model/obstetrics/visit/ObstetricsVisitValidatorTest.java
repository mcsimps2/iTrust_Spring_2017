package edu.ncsu.csc.itrust.unit.model.obstetrics.visit;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obstetrics.visit.ObstetricsVisit;
import edu.ncsu.csc.itrust.model.obstetrics.visit.ObstetricsVisitValidator;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

/**
 * Test validator for obstetrics visit
 * @author jcgonzal
 */
public class ObstetricsVisitValidatorTest {

	/** Validator obj to test */
	ObstetricsVisitValidator validator;
	
	@Before
	public void setUp() throws Exception {
		// Reset test data
		DBBuilder.rebuildAll();
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
		
		validator = new ObstetricsVisitValidator(ConverterDAO.getDataSource());
	}
	
	@Test
	public void testConstructor() {
		try {
			validator = new ObstetricsVisitValidator();
			Assert.fail("DBException not thrown");
		} catch (DBException e) {
			Assert.assertTrue(e != null);
		}
	}

	@Test
	public void testValidateObstetricsVisit() {
		
		byte[] b1 = new byte[100];
		Arrays.fill(b1, (byte)0);
		ObstetricsVisit goodObj;
		try {
			goodObj = new ObstetricsVisit(new Long(2), new Long(3), new Integer(13), new Integer(54), new Integer(3), new Boolean(true), null, null);
			validator.validate(goodObj);
		} catch (FormValidationException e) {
			Assert.fail(e.getMessage());
		}
		
		
		try {
			ObstetricsVisit badObj1 = new ObstetricsVisit(new Long(2), new Long(3), null, null, null, null, null, null);
			validator.validate(badObj1);
			Assert.fail("Validation passed for an invalid Obstetrics Visit");
		} catch (FormValidationException e) {
			Assert.assertTrue(e.getMessage().equals("This form has not been validated correctly. The following field are not properly filled in: [Weeks pregnant is required, Multiplicity is required, Fetal heart rate is required, Low lying placenta observation is required]"));
		}
		
		try {
			ObstetricsVisit badObj2 = new ObstetricsVisit(new Long(2), new Long(3), new Integer(-1), new Integer(0), new Integer(0), new Boolean(true), null, null);
			validator.validate(badObj2);
			Assert.fail("Validation passed for an invalid Obstetrics Visit");
		} catch (FormValidationException e) {
			Assert.assertTrue(e.getMessage().equals("This form has not been validated correctly. The following field are not properly filled in: [Weeks pregnant must be between 0 and 48, Multiplicity must be greater than zero, Fetal heart rate must be greater than zero]"));
		}
		
		try {
			ObstetricsVisit badObj3 = new ObstetricsVisit(new Long(2), new Long(3), new Integer(49), new Integer(-1), new Integer(-1), new Boolean(false), null, null);
			validator.validate(badObj3);
			Assert.fail("Validation passed for an invalid Obstetrics Visit");
		} catch (FormValidationException e) {
			Assert.assertEquals("This form has not been validated correctly. The following field are not properly filled in: [Weeks pregnant must be between 0 and 48, Multiplicity must be greater than zero, Fetal heart rate must be greater than zero]", e.getMessage());
		}
	}
	
	@Test
	public void testValidateUpdate() {
		byte[] b1 = new byte[100];
		Arrays.fill(b1, (byte)0);
		try {
			ObstetricsVisit goodObj = new ObstetricsVisit(new Long(2), new Long(3), new Integer(13), new Integer(54), new Integer(3), new Boolean(true), null, null);
			validator.validateUpdate(goodObj);
		} catch (FormValidationException e) {
			Assert.fail(e.getMessage());
		}
		
		
		try {
			ObstetricsVisit goodObj2 = new ObstetricsVisit(new Long(2), new Long(3), new Integer(13), null, null, null, null, null);
			validator.validateUpdate(goodObj2);
		} catch (FormValidationException e) {
			Assert.fail(e.getMessage());
		}
		
		
		try {
			ObstetricsVisit badObj1 = new ObstetricsVisit(new Long(2), new Long(3), null, null, null, null, null, null);
			validator.validate(badObj1);
			Assert.fail("Validation passed for an invalid Obstetrics Visit");
		} catch (FormValidationException e) {
			Assert.assertTrue(e.getMessage().equals("This form has not been validated correctly. The following field are not properly filled in: [Weeks pregnant is required, Multiplicity is required, Fetal heart rate is required, Low lying placenta observation is required]"));
		}
		
		try {
			ObstetricsVisit badObj2 = new ObstetricsVisit(new Long(2), new Long(3), new Integer(-1), new Integer(0), new Integer(0), new Boolean(true), null, null);
			validator.validate(badObj2);
			Assert.fail("Validation passed for an invalid Obstetrics Visit");
		} catch (FormValidationException e) {
			Assert.assertTrue(e.getMessage().equals("This form has not been validated correctly. The following field are not properly filled in: [Weeks pregnant must be between 0 and 48, Multiplicity must be greater than zero, Fetal heart rate must be greater than zero]"));
		}
		
		try {
			ObstetricsVisit badObj3 = new ObstetricsVisit(new Long(2), new Long(3), new Integer(49), new Integer(-1), new Integer(-1), new Boolean(false), null, null);
			validator.validate(badObj3);
			Assert.fail("Validation passed for an invalid Obstetrics Visit");
		} catch (FormValidationException e) {
			Assert.assertEquals("This form has not been validated correctly. The following field are not properly filled in: [Weeks pregnant must be between 0 and 48, Multiplicity must be greater than zero, Fetal heart rate must be greater than zero]", e.getMessage());
		}
	}

}
