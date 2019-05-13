package de.maggiwuerze.xdccloader.model;

import java.time.LocalDateTime;
import lombok.Data;
import javax.persistence.*

@Data
@Entity(name="Server")
class Request(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long,

        @Column(nullable = false)
        var creationDate: LocalDateTime,

        @Column(name="Server", nullable = false)
        var server: Server,

        @Column(name="Channel", nullable = false)
        var channel: Channel,

        @Column(name="User", nullable = false)
        var user: User

)
