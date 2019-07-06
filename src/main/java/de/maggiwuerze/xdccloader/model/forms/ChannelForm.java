package de.maggiwuerze.xdccloader.model.forms;

public class ChannelForm {

    String name;
    String bla;


    public ChannelForm(String name, String bla) {
        this.name = name;
        this.bla = bla;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBla() {
        return bla;
    }

    public void setBla(String bla) {
        this.bla = bla;
    }
}
