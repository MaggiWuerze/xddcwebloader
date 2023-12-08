package de.maggiwuerze.xdccloader.model.forms.validation;

import de.maggiwuerze.xdccloader.model.forms.UserForm;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator
	implements ConstraintValidator<PasswordMatches, Object> {

	@Override
	public void initialize(PasswordMatches constraintAnnotation) {
	}

	@Override
	public boolean isValid(Object obj, ConstraintValidatorContext context) {
		UserForm user = (UserForm) obj;
		return user.getPassword().equals(user.getMatchingPassword());
	}
}