package edu.ncsu.csc.itrust.model.obstetrics.ultrasound;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.POJOValidator;

/**
 * Validator for ultrasound obj
 * @author jcgonzal
 */
public class UltrasoundValidator extends POJOValidator<Ultrasound> {

	@Override
	public void validate(Ultrasound obj) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		
		if (obj.getCrl() < 0)
			errorList.addIfNotNull("CRL must not be negative");
		
		if (obj.getBpd() < 0)
			errorList.addIfNotNull("BPD must not be negative");
		
		if (obj.getHc() < 0)
			errorList.addIfNotNull("HC must not be negative");
		
		if (obj.getFl() < 0)
			errorList.addIfNotNull("FL must not be negative");
		
		if (obj.getOfd() < 0)
			errorList.addIfNotNull("OFD must not be negative");
		
		if (obj.getAc() < 0)
			errorList.addIfNotNull("AC must not be negative");
		
		if (obj.getHl() < 0)
			errorList.addIfNotNull("HL must not be negative");
		
		if (obj.getEfw() < 0)
			errorList.addIfNotNull("EFW must not be negative");
		
		//TODO: There might need to be more validation here
		
		if ( errorList.hasErrors() )
			throw new FormValidationException(errorList);		
	}

}
