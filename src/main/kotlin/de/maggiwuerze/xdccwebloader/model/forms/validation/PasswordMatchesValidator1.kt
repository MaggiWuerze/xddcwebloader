package de.maggiwuerze.xdccwebloader.model.forms.validation

import de.maggiwuerze.xdccwebloader.model.forms.UserForm
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class PasswordMatchesValidator : ConstraintValidator<PasswordMatches?, Any?> {
    public override fun initialize(constraintAnnotation: PasswordMatches?) {
    }

    public override fun isValid(obj: Any?, p1: ConstraintValidatorContext): Boolean {
        val user: UserForm = obj as UserForm
        return user.password.equals(user.matchingPassword)
    }
}