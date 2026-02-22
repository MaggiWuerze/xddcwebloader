package de.maggiwuerze.xdccwebloader.persistence

import de.maggiwuerze.xdccwebloader.persistence.entity.Bot
import de.maggiwuerze.xdccwebloader.persistence.entity.Channel
import de.maggiwuerze.xdccwebloader.persistence.entity.Server
import de.maggiwuerze.xdccwebloader.persistence.entity.UserSettings
import jakarta.transaction.Transactional
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class DatabaseLoader(

    var settingsRepository: UserSettingsRepository,

    var channelRepository: ChannelRepository,

    var serverRepository: ServerRepository,

    var ircUserRepository: IrcUserRepository,

    var userSettingsRepository: UserSettingsRepository,
) : CommandLineRunner {

    @Transactional
    override fun run(vararg strings: String) {
        val userSettings: UserSettings = userSettingsRepository.save(UserSettings())
        this.settingsRepository.save(userSettings)

        val server: Server = serverRepository.save(Server(name = "Abjects", serverUrl = "irc.abjects.net"))
        val channel: Channel = channelRepository.save(Channel(name = "#beast-xdcc"))
        this.ircUserRepository.save(
            Bot(
                server = server,
                channel = channel,
                name = "BEAST-X-CHEDDAR",
                pattern = "xdcc send #%s",
                maxParallelDownloads = 3L
            )
        )
        this.ircUserRepository.save(
            Bot(
                server = server,
                channel = channel,
                name = "BEAST-X-REDRUNTZ",
                pattern = "xdcc send #%s",
                maxParallelDownloads = 3L
            )
        )
    }
}