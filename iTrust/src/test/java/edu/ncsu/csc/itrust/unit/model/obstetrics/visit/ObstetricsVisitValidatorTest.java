package edu.ncsu.csc.itrust.unit.model.obstetrics.visit;

import java.sql.SQLException;
import java.util.Arrays;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.obstetrics.visit.ObstetricsVisit;
import edu.ncsu.csc.itrust.model.obstetrics.visit.ObstetricsVisitValidator;

public class ObstetricsVisitValidatorTest {

	ObstetricsVisitValidator validator;
	
	@Before
	public void setUp() throws Exception {
			validator = new ObstetricsVisitValidator();
	}

	@Test
	public void testValidateUltrasound() {
		
		byte[] b1 = new byte[100];
		Arrays.fill(b1, (byte)0);
		ObstetricsVisit goodObj;
		try {
			goodObj = new ObstetricsVisit(new Long(2), new Integer(13), new Integer(54), new Integer(3), new Boolean(true), new SerialBlob(b1));
			validator.validate(goodObj);
		} catch (SerialException e1) {
			Assert.fail(e1.getMessage());
		} catch (SQLException e1) {
			Assert.fail(e1.getMessage());
		} catch (FormValidationException e) {
			Assert.fail(e.getMessage());
		}
		
		
		try {
			ObstetricsVisit badObj1 = new ObstetricsVisit(new Long(2), null, null, null, null, null);
			validator.validate(badObj1);
			Assert.fail("Validation passed for an invalid Obstetrics Visit");
		} catch (FormValidationException e) {
			Assert.assertTrue(e.getMessage().equals("This form has not been validated correctly. The following field are not properly filled in: [Weeks pregnant is required, Multiplicity is required, Fetal heart rate is required, Low lying placenta observation is required]"));
		}
		
		try {
			ObstetricsVisit badObj2 = new ObstetricsVisit(new Long(2), new Integer(-1), new Integer(0), new Integer(0), new Boolean(true), new SerialBlob(b1));
			validator.validate(badObj2);
			Assert.fail("Validation passed for an invalid Obstetrics Visit");
		} catch (FormValidationException e) {
			Assert.assertTrue(e.getMessage().equals("This form has not been validated correctly. The following field are not properly filled in: [Weeks pregnant must be between 0 and 42, Multiplicity must be greater than zero, Fetal heart rate must be greater than zero]"));
		} catch (SerialException e1) {
			Assert.fail(e1.getMessage());
		} catch (SQLException e1) {
			Assert.fail(e1.getMessage());
		}
		
		try {
			ObstetricsVisit badObj3 = new ObstetricsVisit(new Long(2), new Integer(43), new Integer(-1), new Integer(-1), new Boolean(false), new SerialBlob(b1));
			validator.validate(badObj3);
			Assert.fail("Validation passed for an invalid Obstetrics Visit");
		} catch (FormValidationException e) {
			Assert.assertTrue(e.getMessage().equals("This form has not been validated correctly. The following field are not properly filled in: [Weeks pregnant must be between 0 and 42, Multiplicity must be greater than zero, Fetal heart rate must be greater than zero]"));
		} catch (SerialException e1) {
			Assert.fail(e1.getMessage());
		} catch (SQLException e1) {
			Assert.fail(e1.getMessage());
		}
	}

}
