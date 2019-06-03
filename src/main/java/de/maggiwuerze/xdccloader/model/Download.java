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
    @JoinColumn(name="CHANNEL_ID")
    Channel channel;

    @ManyToOne
    @JoinColumn(name="SERVER_ID")
    @RestResource(rel="server")
    Server server;

    @ManyToOne
    @JoinColumn(name="USER_ID")
    IrcUser user;

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

    public Download(Channel channel, Server server, IrcUser user, String fileRefId) {
        this.channel = channel;
        this.server = server;
        this.user = user;
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

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public IrcUser getUser() {
        return user;
    }

    public void setUser(IrcUser user) {
        this.user = user;
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
}
