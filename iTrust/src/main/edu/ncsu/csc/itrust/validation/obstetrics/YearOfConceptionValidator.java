package edu.ncsu.csc.itrust.validation.obstetrics;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("obstetrics.YearOfConceptionValidator")
public class YearOfConceptionValidator implements Validator {
	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		String inputValue = value.toString();
		
		// Implement this
		boolean valid = false;
		
		if (!valid) {
			FacesMessage msg = new FacesMessage("Summary", "Detail");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}
	}
}