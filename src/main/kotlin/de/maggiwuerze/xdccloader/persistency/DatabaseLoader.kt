package de.maggiwuerze.xdccloader.persistency

import de.maggiwuerze.xdccloader.model.Channel
import de.maggiwuerze.xdccloader.model.Download
import de.maggiwuerze.xdccloader.model.Server
import de.maggiwuerze.xdccloader.model.User
import de.maggiwuerze.xdccloader.util.State
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*
import javax.transaction.Transactional


@Component
class DatabaseLoader : CommandLineRunner {

    private val userRepository: UserRepository
    private val downloadRepository: DownloadRepository
    private val channelRepository: ChannelRepository
    private val serverRepository: ServerRepository

    @Autowired
    constructor(userRepository: UserRepository, downloadRepository: DownloadRepository, channelRepository: ChannelRepository, serverRepository: ServerRepository) {
        this.userRepository = userRepository
        this.downloadRepository = downloadRepository
        this.serverRepository = serverRepository
        this.channelRepository = channelRepository
    }

    @Throws(Exception::class)
    @Transactional
    override fun run(vararg strings: String) {

        var username: String = "Bilbo"
        var password: String = BCryptPasswordEncoder(11).encode("Baggins")
        this.userRepository.save(User(username, password, LocalDateTime.now()))
//
//		username = "Frodo"
//		password = BCryptPasswordEncoder(11).encode("Baggins")
//		this.userRepository.save(User(2L, username, password, LocalDateTime.now()))
//
//		username = "Samweis"
//		password = BCryptPasswordEncoder(11).encode("Baggins")
//		this.userRepository.save(User(3L, username, password, LocalDateTime.now()))
//
//		username = "Merry"
//		password = BCryptPasswordEncoder(11).encode("Baggins")
//		this.userRepository.save(User(4L, username, password, LocalDateTime.now()))
//
//		username = "Pippin"
//		password = BCryptPasswordEncoder(11).encode("Baggins")
//		this.userRepository.save(User(5L, username, password, LocalDateTime.now()))

//
//        val channelFromDb = channelRepository.findById(channel.id);

        val channel = channelRepository.save(Channel(0, "#HorribleSubs", LocalDateTime.now()))
        val dbChannel = channelRepository.save(Channel(0, "#Lobby", LocalDateTime.now()))

        var server = serverRepository.save(
                Server(0,
                        "Rizon",
                        "irc.rizon.net",
                        LocalDateTime.now()))
        server.channels = mutableListOf(channel, dbChannel)
        serverRepository.save(server)

        var date = LocalDateTime.now()
        var progress: Long = Random().nextInt(100).toLong()
        var filename = "tentaclePorn.mkv"
        var status = State.values()[Random().nextInt(State.values().size)]

        downloadRepository.save(Download(channel, server, date, progress, filename, status))

        progress = Random().nextInt(100).toLong()
        status = State.values()[Random().nextInt(State.values().size)]
        filename = "somevideo.avi"
        downloadRepository.save(Download(channel, server, date, progress, filename, status))


        progress = Random().nextInt(100).toLong()
        status = State.values()[Random().nextInt(State.values().size)]
        filename = "hobbit.mp4"
        downloadRepository.save(Download(channel, server, date, progress, filename, status))

//        downloadRepository.save(Download(channel, server, date, progress, filename, status))
//
//        downloadRepository.save(Download(channel, server, date, progress, filename, status))
//
//        downloadRepository.save(Download(channel, server, date, progress, filename, status))

    }
}