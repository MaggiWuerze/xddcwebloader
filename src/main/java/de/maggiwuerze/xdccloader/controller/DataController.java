package de.maggiwuerze.xdccloader.controller;

import de.maggiwuerze.xdccloader.events.DownloadCreationEvent;
import de.maggiwuerze.xdccloader.model.*;
import de.maggiwuerze.xdccloader.model.forms.DownloadForm;
import de.maggiwuerze.xdccloader.persistency.*;
import de.maggiwuerze.xdccloader.util.SocketEvents;
import de.maggiwuerze.xdccloader.util.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/data")
class DataController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    IrcUserRepository ircUserRepository;
    @Autowired
    DownloadRepository downloadRepository;
    @Autowired
    ChannelRepository channelRepository;
    @Autowired
    ServerRepository serverRepository;

    @Autowired
    private SimpMessagingTemplate websocket;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    final String MESSAGE_PREFIX = "/topic";

    //DOWNLOADS

    /**
     * @return a list of all downloads
     */
    @GetMapping("/downloads/")
    public ResponseEntity<List<Download>> getAllDownloads() {

        List<Download> downloads = downloadRepository.findAllByOrderByProgressDesc();
        return new ResponseEntity(downloads, HttpStatus.OK);

    }

    /**
     * @param active
     * @return a list of downloads. if active, then it return all that are still working. if not it returns all that have stopped, this includes errors
     */
    @GetMapping("/downloads/active/{active}")
    public ResponseEntity<List<Download>> getActiveDownloads(@PathVariable("active") boolean active) {

        List<State> states;
        if (active) {

            states = Arrays.asList(State.UNKNOWN, State.DONE);

        } else {

            states = Arrays.asList(State.PREPARING, State.PREPARED, State.READY, State.CONNECTING, State.TRANSMITTING, State.FINALIZING);

        }

        return new ResponseEntity(downloadRepository.findAllByStatusInOrderByProgress(states), HttpStatus.OK);

    }

    @GetMapping("/downloads/failed")
    public ResponseEntity<List<Download>> getFailedDownloads(@PathVariable("active") boolean active) {

        return new ResponseEntity(downloadRepository.findAllByStatusOrderByProgressDesc(State.ERROR), HttpStatus.OK);

    }

    @PostMapping("/downloads/")
    public ResponseEntity<?> addDownload(@RequestBody DownloadForm downloadForm) {

        Optional<Server> server = serverRepository.findById(downloadForm.getServerId());
        Optional<Channel> channel = channelRepository.findById(downloadForm.getChannelId());
        Optional<IrcUser> ircUser = ircUserRepository.findById(downloadForm.getUserId());
        String fileRefId = downloadForm.getFileRefId();

        Download download = downloadRepository.save(new Download(channel.get(), server.get(), ircUser.get(), fileRefId));
        publishDownloadEvent(SocketEvents.NEW, download);

        return new ResponseEntity("Download added succcessfully", HttpStatus.OK);
    }


    //SERVERS

    /**
     * @return a list of all servers
     */
    @GetMapping("/servers/")
    public ResponseEntity<List<Server>> getAllServers() {

        List<Server> servers = serverRepository.findAll();
        return new ResponseEntity(servers, HttpStatus.OK);

    }


    //CHANNELS

    /**
     * @return a list of all channels
     */
    @GetMapping("/channels/")
    public ResponseEntity<List<Channel>> getAllChannels() {

        List<Channel> channels = channelRepository.findAll();
        return new ResponseEntity(channels, HttpStatus.OK);

    }


    //USERS

    /**
     * @return a list of all users
     */
    @GetMapping("/users/")
    public ResponseEntity<List<User>> getAllUsers() {

        List<User> users = userRepository.findAll();
        return new ResponseEntity(users, HttpStatus.OK);

    }

    //IRC_USERS

    /**
     * @return a list of all users
     */
    @GetMapping("/ircUsers/")
    public ResponseEntity<List<IrcUser>> getAllIrcUsers() {

        List<IrcUser> users = ircUserRepository.findAll();
        return new ResponseEntity(users, HttpStatus.OK);

    }

    //UTIL

    /**
     * @param destinationSuffix
     * @param payload
     */
    private void publishDownloadEvent(SocketEvents destinationSuffix, Download payload) {

        DownloadCreationEvent downloadCreationEvent = new DownloadCreationEvent(this, payload);
        applicationEventPublisher.publishEvent(downloadCreationEvent);

        this.websocket.convertAndSend(
                MESSAGE_PREFIX + destinationSuffix.getRoute(), payload);

    }

}