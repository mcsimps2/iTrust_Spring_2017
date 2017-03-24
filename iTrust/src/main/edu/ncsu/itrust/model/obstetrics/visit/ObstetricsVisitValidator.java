package edu.ncsu.itrust.model.obstetrics.visit;

//import javax.sql.DataSource;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.POJOValidator;

public class ObstetricsVisitValidator extends POJOValidator<ObstetricsVisit> {

//	private DataSource ds;
//	
//	public ObstetricsVisitValidator(DataSource ds) {
//
//	}

	@Override
	public void validate(ObstetricsVisit obj) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		
		if (obj.getMultiplicity() == 0)
			errorList.addIfNotNull("Multiplicity can not be 0");
		
		if (obj.getFhr() == 0)
			errorList.addIfNotNull("Fetal heart rate can not be 0");
		
		//TODO: There might need to be more validation here
		
		if ( errorList.hasErrors() )
			throw new FormValidationException(errorList);
	}
	
	
}
