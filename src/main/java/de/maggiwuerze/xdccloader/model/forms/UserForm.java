package de.maggiwuerze.xdccloader.model.forms;

import de.maggiwuerze.xdccloader.model.forms.validation.PasswordMatches;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@PasswordMatches
public class UserForm {

    @NotNull
    @NotEmpty
    String username;

    @NotNull
    @NotEmpty
    String password;

    @NotNull
    @NotEmpty
    String matchingPassword;


    public UserForm() {}

    public UserForm(String username, String password, String matchingPassword) {
        this.username = username;
        this.password = password;
        this.matchingPassword = matchingPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }
}
