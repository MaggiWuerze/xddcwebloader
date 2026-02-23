package de.maggiwuerze.xdccwebloader.persistence.entity

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size
import java.time.LocalDateTime
import java.util.*

@Entity
class Bot(

    @Id
    var id: UUID = UUID.randomUUID(),

    @JoinColumn(name = "SERVER_ID")
    @ManyToOne
    var server: Server,

    @JoinColumn(
        name = "CHANNEL_ID"
    )
    @ManyToOne
    var channel: Channel,

    @Column(
        nullable = false
    )
    @Size(min = 1, message = "Bot name must be at least 1 character long")
    var name: String,

    @Column(
        nullable = false
    )
    @Size(min = 1, message = "Bot pattern must be at least 1 character long")
    var pattern: String,

    @Column(nullable = false)
    var creationDate: LocalDateTime = LocalDateTime.now(),

    @Min(1L)
    @Column(nullable = false)
    var maxParallelDownloads: Long? = 3L

) {
    fun toTO() = BotTO(
        id, server, channel, name, pattern, creationDate, maxParallelDownloads!!
    )
}

@Schema(
    name = "BotTO",
    requiredProperties = ["id", "server", "channel", "name", "pattern", "creationDate", "maxParallelDownloads"]
)
data class BotTO(
    val id: UUID,
    val server: Server,
    val channel: Channel,
    val name: String,
    val pattern: String,
    val creationDate: LocalDateTime,
    val maxParallelDownloads: Long
)
