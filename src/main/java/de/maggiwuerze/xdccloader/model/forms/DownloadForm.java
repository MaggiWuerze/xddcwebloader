package de.maggiwuerze.xdccloader.model.forms;

public class DownloadForm {

    Long channelId;

    Long serverId;

    Long userId;

    String fileRefId;

    public DownloadForm(Long channelId, Long serverId, Long userId, String fileRefId) {
        this.channelId = channelId;
        this.serverId = serverId;
        this.userId = userId;
        this.fileRefId = fileRefId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFileRefId() {
        return fileRefId;
    }

    public void setFileRefId(String fileRefId) {
        this.fileRefId = fileRefId;
    }
}
