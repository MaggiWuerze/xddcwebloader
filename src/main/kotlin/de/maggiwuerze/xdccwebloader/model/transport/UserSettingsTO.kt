package de.maggiwuerze.xdccwebloader.model.transport

import de.maggiwuerze.xdccwebloader.model.download.DownloadSort
import de.maggiwuerze.xdccwebloader.model.entity.Bot


class UserSettingsTO(var refreshrateInSeconds: Long?, var sessionTimeout: Long?) {
    var downloadSortBy: DownloadSort? = DownloadSort.PROGRESS
    var showAllBotsInQuickWindow: Boolean = false
    var botsVisibleInQuickWindow: MutableMap<Bot?, Boolean?> =
        HashMap<Bot?, Boolean?>()
    var showAllItemsInDownloadCard: Boolean = true
    var downloadPath: String = ""
    private val itemsVisibleInDownloadCard: MutableMap<String?, Boolean?>? = null

}
