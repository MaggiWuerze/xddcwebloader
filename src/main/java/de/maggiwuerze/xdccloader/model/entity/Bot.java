package de.maggiwuerze.xdccloader.model.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Bot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name="CHANNEL_ID")
    Channel channel;

    @ManyToOne
    @JoinColumn(name="SERVER_ID")
    Server server;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String pattern;

    @Column(nullable = false)
    LocalDateTime creationDate = LocalDateTime.now();

    public Bot() {
    }

    public Bot(Server server, Channel channel, String name, String pattern) {
        this.name = name;
        this.pattern = pattern;
        this.server = server;
        this.channel = channel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
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
}
