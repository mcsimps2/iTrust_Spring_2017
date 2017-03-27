package edu.ncsu.csc.itrust.model.obstetrics.visit;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.POJOValidator;

/**
 * Used to validate obstetrics visit object
 * @author jcgonzal
 */
public class ObstetricsVisitValidator extends POJOValidator<ObstetricsVisit> {

	@Override
	public void validate(ObstetricsVisit obj) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		
		if (obj.getWeeksPregnant() == null)
			errorList.addIfNotNull("Weeks pregnant is required");
		
		else if (obj.getWeeksPregnant() < 0 || obj.getWeeksPregnant() > 49)
			errorList.addIfNotNull("Weeks pregnant must be between 0 and 49");
		
		if (obj.getMultiplicity() == null)
			errorList.addIfNotNull("Multiplicity is required");
		
		else if (obj.getMultiplicity() <= 0)
			errorList.addIfNotNull("Multiplicity must be greater than zero");
		
		if (obj.getFhr() == null)
			errorList.addIfNotNull("Fetal heart rate is required");
		
		else if (obj.getFhr() <= 0)
			errorList.addIfNotNull("Fetal heart rate must be greater than zero");
		
		if (obj.isLowLyingPlacentaObserved() == null)
			errorList.addIfNotNull("Low lying placenta observation is required");
		
		if ( errorList.hasErrors() )
			throw new FormValidationException(errorList);
	}
	
	/**
	 * Validation for updating the obstetrics visit object.
	 * The multiplicity and fhr fields are allowed to be null.
	 * @param obj
	 * @throws FormValidationException
	 */
	public void validateUpdate(ObstetricsVisit obj) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		
		if (obj.getWeeksPregnant() == null)
			errorList.addIfNotNull("Weeks pregnant is required");
		
		else if (obj.getWeeksPregnant() < 0 || obj.getWeeksPregnant() > 42)
			errorList.addIfNotNull("Weeks pregnant must be between 0 and 42");
		
		if (obj.getMultiplicity() != null && obj.getMultiplicity() <= 0)
			errorList.addIfNotNull("Multiplicity must be greater than zero");
		
		if (obj.getFhr() != null && obj.getFhr() <= 0)
			errorList.addIfNotNull("Fetal heart rate must be greater than zero");
		
		if ( errorList.hasErrors() )
			throw new FormValidationException(errorList);
	}
}
