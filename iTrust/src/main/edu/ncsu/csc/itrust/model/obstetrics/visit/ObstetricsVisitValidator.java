package edu.ncsu.csc.itrust.model.obstetrics.visit;

//import javax.sql.DataSource;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.POJOValidator;

/**
 * Used to validate obstetrics visit object
 * @author jcgonzal
 */
public class ObstetricsVisitValidator extends POJOValidator<ObstetricsVisit> {

//	private DataSource ds;
//	
//	public ObstetricsVisitValidator(DataSource ds) {
//
//	}

	@Override
	public void validate(ObstetricsVisit obj) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		
		if (obj.getWeeksPregnant() < 0 || obj.getWeeksPregnant() > 42)
			errorList.addIfNotNull("Weeks pregnant must be between 0 and 42");
		
		if (obj.getDaysPregnant() < 0 || obj.getDaysPregnant() > 6)
			errorList.addIfNotNull("Days pregnant must be between 0 and 6");
		
		if (obj.getMultiplicity() == 0)
			errorList.addIfNotNull("Multiplicity can not be 0");
		
		if (obj.getFhr() == 0)
			errorList.addIfNotNull("Fetal heart rate can not be 0");	
		
		//TODO: There might need to be more validation here
		
		if ( errorList.hasErrors() )
			throw new FormValidationException(errorList);
	}	
}
