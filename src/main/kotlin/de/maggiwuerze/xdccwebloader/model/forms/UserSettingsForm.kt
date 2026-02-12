package de.maggiwuerze.xdccwebloader.model.forms

import de.maggiwuerze.xdccwebloader.model.download.DownloadSort

class UserSettingsForm(
    var downloadSortBy: DownloadSort,

    var sessionTimeout: Long,

    var downloadPath: String
)
