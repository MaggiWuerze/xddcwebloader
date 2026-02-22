package de.maggiwuerze.xdccwebloader.persistence.entity

import de.maggiwuerze.xdccwebloader.model.download.DownloadSort
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import java.time.LocalDateTime
import java.util.*

@Entity


class UserSettings(
    @Id
    var id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    var initialized: Boolean = false,

    @Column(nullable = false)
    var creationDate: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var downloadSortBy: DownloadSort? = DownloadSort.PROGRESS,

    @Column(nullable = false)
    var sessionTimeout: Long = 300L
) {
    fun toTO() = UserSettingsTO(id, downloadSortBy!!, sessionTimeout)
}

data class UserSettingsTO(val id: UUID, val downloadSortBy: DownloadSort, val sessionTimeout: Long)
