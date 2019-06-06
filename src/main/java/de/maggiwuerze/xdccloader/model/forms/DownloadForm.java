package de.maggiwuerze.xdccloader.model.forms;

public class DownloadForm {

    Long targetBotId;

    String fileRefId;

    public DownloadForm(Long targetBotId, String fileRefId) {
        this.targetBotId = targetBotId;
        this.fileRefId = fileRefId;
    }

    public Long getTargetBotId() {
        return targetBotId;
    }

    public void setTargetBotId(Long targetBotId) {
        this.targetBotId = targetBotId;
    }

    public String getFileRefId() {
        return fileRefId;
    }

    public void setFileRefId(String fileRefId) {
        this.fileRefId = fileRefId;
    }
}
