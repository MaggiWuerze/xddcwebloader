package de.maggiwuerze.xdccloader.model.forms;

import de.maggiwuerze.xdccloader.model.forms.validation.PasswordMatches;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@PasswordMatches
@Data
@NoArgsConstructor
public class UserForm {

	@NotNull
	@NotEmpty
	private String username;

	@NotNull
	@NotEmpty
	private String password;

	@NotNull
	@NotEmpty
	private String matchingPassword;
}
