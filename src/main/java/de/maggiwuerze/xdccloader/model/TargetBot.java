package de.maggiwuerze.xdccloader.model;

import lombok.Data;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Entity
public class TargetBot {

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

    public TargetBot() {
    }

    public TargetBot(Server server, Channel channel, String name, String pattern) {
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
}
