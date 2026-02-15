package de.maggiwuerze.xdccwebloader.model.entity

import de.maggiwuerze.xdccwebloader.model.download.DownloadSort
import jakarta.persistence.Column
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import java.util.*

@jakarta.persistence.Entity


class UserSettings(
    @Id
    var id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    var initialized: Boolean = false,

    @Column(nullable = false)
    var creationDate: java.time.LocalDateTime = java.time.LocalDateTime.now(),

    @Column(nullable = false)
    @Enumerated(jakarta.persistence.EnumType.STRING)
    var downloadSortBy: DownloadSort? = DownloadSort.PROGRESS,

    @Column(nullable = false)
    var sessionTimeout: Long = 300L
) {
    fun toTO() = UserSettingsTO(id, downloadSortBy!!, sessionTimeout)
}

data class UserSettingsTO(val id: UUID, val downloadSortBy: DownloadSort, val sessionTimeout: Long)
