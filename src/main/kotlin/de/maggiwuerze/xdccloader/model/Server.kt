package de.maggiwuerze.xdccloader.model;

import java.time.LocalDateTime;
import lombok.Data;
import javax.persistence.*

@Data
@Entity
class Server(


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,

        @Column(nullable = false)
        var name: String,

        @Column(nullable = false)
        var url: String,

        @Column(nullable = false)
        var creationDate: LocalDateTime,

        @OneToMany(cascade = [CascadeType.ALL])
        var channels: MutableList<Channel> ?= mutableListOf()

)
