package de.maggiwuerze.xdccloader.model;

import java.time.LocalDateTime;
import lombok.Data;
import javax.persistence.*

@Entity
data class Channel(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long,

        @Column(nullable = false)
        var name: String,

        @Column(nullable = false)
        var date: LocalDateTime

)