package de.maggiwuerze.xdccwebloader.model.entity

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
}
