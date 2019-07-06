package de.maggiwuerze.xdccloader.model.forms;

public class ServerForm {

    String name;

    String serverUrl;

    public ServerForm(String name, String serverUrl) {
        this.name = name;
        this.serverUrl = serverUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
