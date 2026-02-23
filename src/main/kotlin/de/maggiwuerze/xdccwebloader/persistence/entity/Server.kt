package de.maggiwuerze.xdccwebloader.persistence.entity

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.LocalDateTime
import java.util.*

@Entity
class Server(

    @Id
    var id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false) var serverUrl: String,

    @Column(nullable = false)
    var creationDate: LocalDateTime = LocalDateTime.now()
) {
    constructor() : this(name = "", serverUrl = "")

    fun toTO() = ServerTO(id, name, serverUrl, creationDate)
}

@Schema(name = "ServerTO", requiredProperties = ["id", "name", "serverUrl", "creationDate"])
data class ServerTO(val id: UUID, val name: String, val serverUrl: String, val creationDate: LocalDateTime)
