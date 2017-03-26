package edu.ncsu.csc.itrust.unit.model.obstetrics.ultrasound;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.obstetrics.ultrasound.Ultrasound;
import edu.ncsu.csc.itrust.model.obstetrics.ultrasound.UltrasoundValidator;

public class UltrasoundValidatorTest {

	UltrasoundValidator validator;
	
	@Before
	public void setUp() throws Exception {
			validator = new UltrasoundValidator();
	}

	@Test
	public void testValidateUltrasound() {
		
		Ultrasound goodObj = new Ultrasound(new Long(1), new Long(2), new Float(0.1), new Float(0.2), new Float(0.3), new Float(0.4), new Float(0.5), new Float(0.6), new Float(0.7), new Float(0.8));
		
		try {
			validator.validate(goodObj);
		} catch (FormValidationException e) {
			Assert.fail(e.getMessage());
		}
		
		Ultrasound badObj1 = new Ultrasound(new Long(2), new Float(0), new Float(0), new Float(0), new Float(0), new Float(0), new Float(0), new Float(0), new Float(0));
		Ultrasound badObj2 = new Ultrasound(new Long(2), new Float(-0.1), new Float(-0.1), new Float(-0.1), new Float(-0.1), new Float(-0.1), new Float(-0.1), new Float(-0.1), new Float(-0.1));
		Ultrasound badObj3 = new Ultrasound(new Long(2), null, null, null, null, null, null, null, null);
		
		
		try {
			validator.validate(badObj1);
			Assert.fail("Validation passed for an invalid Ultrasound");
		} catch (FormValidationException e) {
			Assert.assertTrue(e.getMessage().equals("This form has not been validated correctly. The following field are not properly filled in: [CRL must be greater than zero, BPD must be greater than zero, HC must be greater than zero, FL must be greater than zero, OFD must be greater than zero, AC must be greater than zero, HL must be greater than zero, EFW must be greater than zero]"));
		}
		
		try {
			validator.validate(badObj2);
			Assert.fail("Validation passed for an invalid Ultrasound");
		} catch (FormValidationException e) {
			Assert.assertTrue(e.getMessage().equals("This form has not been validated correctly. The following field are not properly filled in: [CRL must be greater than zero, BPD must be greater than zero, HC must be greater than zero, FL must be greater than zero, OFD must be greater than zero, AC must be greater than zero, HL must be greater than zero, EFW must be greater than zero]"));
		}
		
		try {
			validator.validate(badObj3);
			Assert.fail("Validation passed for an invalid Ultrasound");
		} catch (FormValidationException e) {
			Assert.assertTrue(e.getMessage().equals("This form has not been validated correctly. The following field are not properly filled in: [CRL is required, BPD is required, HC is required, FL is required, OFD is required, AC is required, HL is required, EFW is required]"));
		}
	}

}
