package de.maggiwuerze.xdccloader.model.transport;

import de.maggiwuerze.xdccloader.model.DownloadCardField;
import de.maggiwuerze.xdccloader.model.entity.User;
import org.springframework.beans.BeanUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserTO{

    String name;

    String userRole;

    Boolean initialized = false;

    UserSettingsTO userSettings;

    public UserTO(String name, String password, String userRole, boolean initialized, UserSettingsTO userSettings) {
        this.name = name;
        this.userRole = userRole;
        this.initialized = initialized;
        this.userSettings = userSettings;
    }

    public UserTO(User user) {

        BeanUtils.copyProperties(user, this);
        userSettings = new UserSettingsTO(user.getUserSettings());

        if (user.getUserSettings().getItemsVisibleInDownloadCard().isEmpty() && user.getUserSettings().getShowAllItemsInDownloadCard()) {

            List<DownloadCardField> fields = Arrays.asList(DownloadCardField.values());
            Map<String, Boolean> fieldsVisible = fields.stream().collect(
                    Collectors.toMap(DownloadCardField::getExternalString, DownloadCardField::getVisible));

            this.getUserSettings()
                    .getItemsVisibleInDownloadCard().putAll(fieldsVisible);

        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public Boolean getInitialized() {
        return initialized;
    }

    public void setInitialized(Boolean initialized) {
        this.initialized = initialized;
    }

    public UserSettingsTO getUserSettings() {
        return userSettings;
    }

    public void setUserSettings(UserSettingsTO userSettings) {
        this.userSettings = userSettings;
    }
}
