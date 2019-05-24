package de.maggiwuerze.xdccloader.controller

import de.maggiwuerze.xdccloader.model.Download
import de.maggiwuerze.xdccloader.persistency.ChannelRepository
import de.maggiwuerze.xdccloader.persistency.DownloadRepository
import de.maggiwuerze.xdccloader.persistency.ServerRepository
import de.maggiwuerze.xdccloader.persistency.UserRepository
import de.maggiwuerze.xdccloader.util.State
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping


@Controller
class MainController{

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

    @RequestMapping("/")
    fun index() : String{

        return "index"

    }

    @RequestMapping("")
    fun getActiveRequests() : List<Download>{

        return downloadRepository.findAllByStatusOrderByProgress(State.TRANSMITTING)

    }

    @RequestMapping("/login")
    fun login() : String{

        return "login"

    }




}