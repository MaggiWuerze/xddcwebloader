package de.maggiwuerze.xdccwebloader.model.forms

import de.maggiwuerze.xdccwebloader.model.download.DownloadSort

class UserSettingsFormTO(
    var downloadSortBy: DownloadSort,

    var sessionTimeout: Long,

    var downloadPath: String
)
