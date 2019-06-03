package de.maggiwuerze.xdccloader.controller;

import de.maggiwuerze.xdccloader.model.Download;
import de.maggiwuerze.xdccloader.persistency.ChannelRepository;
import de.maggiwuerze.xdccloader.persistency.DownloadRepository;
import de.maggiwuerze.xdccloader.persistency.ServerRepository;
import de.maggiwuerze.xdccloader.persistency.UserRepository;
import de.maggiwuerze.xdccloader.util.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;


@Controller
class MainController{

    @Autowired
    UserRepository userRepository;
    @Autowired
    DownloadRepository downloadRepository;
    @Autowired
    ChannelRepository channelRepository;
    @Autowired
    ServerRepository serverRepository;

    @Autowired
    private SimpMessagingTemplate websocket;
    @Autowired
    private EntityLinks entityLinks;

    final String MESSAGE_PREFIX = "/topic";

    @RequestMapping("/")
    public String index(){

        return "index";

    }

    @RequestMapping(value = "/trigger", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void trigger(){

        this.websocket.convertAndSend(
                MESSAGE_PREFIX + "/newDownload", "please reload the downloadlist");


    }

    @RequestMapping("/login")
    public String login(){

        return "login";

    }


    private String getPath(Download download){
        return this.entityLinks.linkForSingleResource(download.getClass(),
                download.getId()).toUri().getPath();
    }

}