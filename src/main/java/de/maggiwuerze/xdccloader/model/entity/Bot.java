package de.maggiwuerze.xdccloader.model.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Data
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

    @Column(nullable = false)
    Long maxParallelDownloads = 3L;

    public Bot() {
    }

    public Bot(Server server, Channel channel, String name, String pattern, Long maxParallelDownloads) {
        this.name = name;
        this.pattern = pattern;
        this.server = server;
        this.channel = channel;
        this.maxParallelDownloads = maxParallelDownloads;
    }

}
