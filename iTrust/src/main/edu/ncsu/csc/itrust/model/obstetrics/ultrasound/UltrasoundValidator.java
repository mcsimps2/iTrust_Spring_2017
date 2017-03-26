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
		
		if (obj.getCrl() == null)
			errorList.addIfNotNull("CRL is required");
		
		else if (obj.getCrl() <= 0)
			errorList.addIfNotNull("CRL must be greater than zero");
		
		if (obj.getBpd() == null)
			errorList.addIfNotNull("BPD is required");
		
		else if (obj.getBpd() <= 0)
			errorList.addIfNotNull("BPD must be greater than zero");
		
		if (obj.getHc() == null)
			errorList.addIfNotNull("HC is required");
		
		else if (obj.getHc() <= 0)
			errorList.addIfNotNull("HC must be greater than zero");
		
		if (obj.getFl() == null)
			errorList.addIfNotNull("FL is required");
		
		else if (obj.getFl() <= 0)
			errorList.addIfNotNull("FL must be greater than zero");
		
		if (obj.getOfd() == null)
			errorList.addIfNotNull("OFD is required");
		
		else if (obj.getOfd() <= 0)
			errorList.addIfNotNull("OFD must be greater than zero");
		
		if (obj.getAc() == null)
			errorList.addIfNotNull("AC is required");
		
		else if (obj.getAc() <= 0)
			errorList.addIfNotNull("AC must be greater than zero");
		
		if (obj.getHl() == null)
			errorList.addIfNotNull("HL is required");
		
		else if (obj.getHl() <= 0)
			errorList.addIfNotNull("HL must be greater than zero");
		
		if (obj.getEfw() == null)
			errorList.addIfNotNull("EFW is required");
		
		else if (obj.getEfw() <= 0)
			errorList.addIfNotNull("EFW must be greater than zero");
		
		if ( errorList.hasErrors() )
			throw new FormValidationException(errorList);		
	}

}
