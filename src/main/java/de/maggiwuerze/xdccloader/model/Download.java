package de.maggiwuerze.xdccloader.model;

import de.maggiwuerze.xdccloader.util.State;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;

@Data
@Entity
public class Download {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name="USER_ID")
    TargetBot targetBot;

    @Column(nullable = false)
    String fileRefId;

    @Column(nullable = false)
    LocalDateTime date =LocalDateTime.now();

    @Column(nullable = false)
    Long progress = 0L;

    @Column(nullable = false)
    String filename = "unknown";

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    State status = State.UNKNOWN;

    @Column(nullable = false)
    String statusMessage = "";

    public Download(TargetBot user, String fileRefId) {
        this.targetBot = user;
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

    public TargetBot getTargetBot() {
        return targetBot;
    }

    public void setTargetBot(TargetBot targetBot) {
        this.targetBot = targetBot;
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

    public Long getProgress() {
        return progress;
    }

    public void setProgress(Long progress) {
        this.progress = progress;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public State getStatus() {
        return status;
    }

    public void setStatus(State status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
