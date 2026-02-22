package de.maggiwuerze.xdccwebloader.persistence.entity

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.LocalDateTime
import java.util.*

@Entity
class Channel(
    @Id
    var id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var date: LocalDateTime = LocalDateTime.now()
) {
    fun toTO() = ChannelTO(id, name, date)
}

@Schema(name = "ChannelTO", requiredProperties = ["id", "name", "date"])
data class ChannelTO(val id: UUID, val name: String, val date: LocalDateTime)