package de.maggiwuerze.xdccloader.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import javax.persistence.*;

@Entity
public class Server{

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long id;

        @Column(nullable = false)
        String name;

        @Column(nullable = false)
        String serverUrl;

        @Column(nullable = false)
        LocalDateTime creationDate = LocalDateTime.now();

        public Server(String name, String serverUrl) {
                this.name = name;
                this.serverUrl = serverUrl;
        }

        public Server() {
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

        public String getServerUrl() {
                return serverUrl;
        }

        public void setServerUrl(String serverUrl) {
                this.serverUrl = serverUrl;
        }

        public LocalDateTime getCreationDate() {
                return creationDate;
        }

        public void setCreationDate(LocalDateTime creationDate) {
                this.creationDate = creationDate;
        }

}
