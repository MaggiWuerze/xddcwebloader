package de.maggiwuerze.xdccloader.model.forms;

public class TargetBotForm {

    String name;

    String pattern;

    String fileRefId;

    Long serverId;

    Long channelId;

    public TargetBotForm(String name, String pattern, Long serverId, Long channelId, String fileRefId) {

        this.name = name;
        this.pattern = pattern;
        this.serverId = serverId;
        this.channelId = channelId;
        this.fileRefId = fileRefId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getFileRefId() {
        return fileRefId;
    }

    public void setFileRefId(String fileRefId) {
        this.fileRefId = fileRefId;
    }
}
