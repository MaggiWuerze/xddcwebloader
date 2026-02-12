package de.maggiwuerze.xdccwebloader.persistence

import de.maggiwuerze.xdccwebloader.model.entity.Bot
import de.maggiwuerze.xdccwebloader.model.entity.Channel
import de.maggiwuerze.xdccwebloader.model.entity.Server
import de.maggiwuerze.xdccwebloader.model.entity.UserSettings
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

        val server: Server = serverRepository.save(Server(name = "Rizon", serverUrl = "irc.rizon.net"))
        val channel: Channel = channelRepository.save(Channel(name = "#subsplease"))
        this.ircUserRepository.save(
            Bot(
                server = server,
                channel = channel,
                name = "Ginpachi-Sensei",
                pattern = "xdcc send #%s",
                maxParallelDownloads = 3L
            )
        )
        this.ircUserRepository.save(
            Bot(
                server = server,
                channel = channel,
                name = "CR-ARUTHA-IPv6|NEW",
                pattern = "xdcc send #%s",
                maxParallelDownloads = 3L
            )
        )
    }
}