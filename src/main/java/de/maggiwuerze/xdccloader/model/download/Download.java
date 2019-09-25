package de.maggiwuerze.xdccloader.model.download;

import de.maggiwuerze.xdccloader.model.DownloadState;
import de.maggiwuerze.xdccloader.model.entity.Bot;
import de.maggiwuerze.xdccloader.util.FileTransferProgressWatcher;

import java.time.LocalDateTime;

public class Download {

    Long id;

    Bot bot;

    String fileRefId;

    LocalDateTime date = LocalDateTime.now();

    Double progress = 0.0;

    String filename = "unknown";

    String filesize = "-";

    String averageSpeed = "0 Kb/s";

    String timeRemaining = "-";

    DownloadState status = DownloadState.UNKNOWN;

    String statusMessage = "";

    private FileTransferProgressWatcher progressWatcher;

    public Download(Bot user, String fileRefId) {
        this.bot = user;
        this.fileRefId = fileRefId;
    }

    public Download() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bot getBot() {
        return bot;
    }

    public void setBot(Bot bot) {
        this.bot = bot;
    }

    public String getFileRefId() {
        return fileRefId;
    }

    public void setFileRefId(String fileRefId) {
        this.fileRefId = fileRefId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Double getProgress() {
        return progress;
    }

    public void setProgress(Double progress) {
        this.progress = progress;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public DownloadState getStatus() {
        return status;
    }

    public void setStatus(DownloadState status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    public String getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(String averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public String getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(String timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    public FileTransferProgressWatcher getProgressWatcher() {
        return progressWatcher;
    }

    public void setProgressWatcher(FileTransferProgressWatcher progressWatcher) {
        this.progressWatcher = progressWatcher;
    }

}
