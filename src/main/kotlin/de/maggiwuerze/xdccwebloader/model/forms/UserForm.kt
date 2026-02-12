package de.maggiwuerze.xdccwebloader.model.forms

import de.maggiwuerze.xdccwebloader.model.forms.validation.PasswordMatches
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

@PasswordMatches


class UserForm {
    @NotNull
    @NotEmpty
    var username: String? = null

    @NotNull
    @NotEmpty
    var password: String? = null

    @NotNull
    @NotEmpty
    var matchingPassword: String? = null
}
