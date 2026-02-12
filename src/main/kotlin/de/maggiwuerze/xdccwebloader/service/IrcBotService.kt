package de.maggiwuerze.xdccwebloader.service

import de.maggiwuerze.xdccwebloader.irc.IrcBot
import de.maggiwuerze.xdccwebloader.irc.IrcEventListener
import de.maggiwuerze.xdccwebloader.model.download.Download
import de.maggiwuerze.xdccwebloader.model.entity.Bot
import de.maggiwuerze.xdccwebloader.util.IpHelper
import org.pircbotx.Configuration
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class IrcBotService {
    private val ircEventListener: IrcEventListener? = null
    private val activeBots: MutableMap<Bot, IrcBot> = HashMap()

    fun getIrcBotForDownload(download: Download): IrcBot {
        return activeBots[download.bot] ?: getNewBot(download)
    }

    fun getNewBot(download: Download): IrcBot {
        val username = "${Random.nextInt(10)}${randomString()}"
        val targetBot: Bot = download.bot

        val configuration: Configuration = Configuration.Builder()
            .setName(username) //Set the nick of the bot. CHANGE IN YOUR CODE
            .addServer(targetBot.server.serverUrl) //Join the freenode network
            .addAutoJoinChannel(targetBot.channel.name) //Join the official #pircbotx channel
            .setAutoReconnect(true)
            .setAutoReconnectAttempts(5)
            .setAutoNickChange(true) //Automatically change nick when the current one is in use
            .addListener(ircEventListener) //Add our listener that will be called on Events
            .setDccPublicAddress(IpHelper.publicIp)
            .buildConfiguration()

        val bot: IrcBot = IrcBot(configuration, download.id)
        activeBots[targetBot] = bot

        return bot
    }

    val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    fun randomString() = (1..7)
        .map { Random.nextInt(0, charPool.size).let { charPool[it] } }
        .joinToString("")
}


