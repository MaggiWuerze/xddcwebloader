package de.maggiwuerze.xdccloader.model;

import java.time.LocalDateTime;
import lombok.Data;
import javax.persistence.*

@Data
@Entity
class Server(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long,

        @Column(nullable = false)
        var name: String,

        @Column(nullable = false)
        var url: String,

        @Column(nullable = false)
        var creationDate: LocalDateTime,

        @Column(nullable = true)
        @OneToMany(cascade = [CascadeType.ALL])
        val channels: List<Channel>? = null

)
