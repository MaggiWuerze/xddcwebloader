package de.maggiwuerze.xdccwebloader.irc

import de.maggiwuerze.xdccwebloader.service.DownloadService
import org.pircbotx.Configuration
import org.pircbotx.PircBotX
import java.util.*

/**
 * Constructs a PircBotX with the provided configuration.
 *
 * @param configuration Fully built Configuration
 */
class IrcBot(configuration: Configuration, val downloadId: UUID) : PircBotX(configuration) {
    private val downloadService: DownloadService? = null
}
