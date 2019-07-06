package de.maggiwuerze.xdccloader.events;

import de.maggiwuerze.xdccloader.events.download.DownloadCreationEvent;
import de.maggiwuerze.xdccloader.events.download.DownloadDoneEvent;
import de.maggiwuerze.xdccloader.events.download.DownloadUpdateEvent;
import de.maggiwuerze.xdccloader.irc.IrcBot;
import de.maggiwuerze.xdccloader.irc.IrcEventListener;
import de.maggiwuerze.xdccloader.model.Download;
import de.maggiwuerze.xdccloader.persistency.DownloadRepository;
import de.maggiwuerze.xdccloader.util.FileTransferProgressWatcher;
import de.maggiwuerze.xdccloader.util.ProgressWatcherFactory;
import de.maggiwuerze.xdccloader.util.SocketEvents;
import de.maggiwuerze.xdccloader.util.State;
import org.apache.commons.lang3.RandomStringUtils;
import org.pircbotx.Configuration;
import org.pircbotx.exception.IrcException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.Logger;

@Component
public class CustomSpringEventListener {

    public static final int DCC_TRANSFER_BUFFER_SIZE = 1024 * 10;
    Logger logger = Logger.getLogger("Class CustomSpringEventListener");

    @Autowired
    DownloadRepository downloadRepository;

    @Autowired
    ProgressWatcherFactory progressWatcherFactory;

    @Autowired
    IrcEventListener ircEventListener;

    @Autowired
    EventPublisher eventPublisher;

    @EventListener
    public void onDownloadCreationEvent(DownloadCreationEvent event) {

        Download download = downloadRepository.findById(event.getPayload().getId()).get();

        eventPublisher.updateDownloadState(State.PREPARING, download);
        String username = RandomStringUtils.randomAlphabetic(1) + RandomStringUtils.random(7, true, true);

        Configuration configuration = new Configuration.Builder()
//                .setUsername(event.getPayload().getTargetBot().getUsername()) //Set the nick of the bot. CHANGE IN YOUR CODE
                .setName(username) //Set the nick of the bot. CHANGE IN YOUR CODE
                .addServer(event.getPayload().getTargetBot().getServer().getServerUrl()) //Join the freenode network
                .addAutoJoinChannel(event.getPayload().getTargetBot().getChannel().getName()) //Join the official #pircbotx channel
                .setAutoReconnectAttempts(5)
                .setDccTransferBufferSize(DCC_TRANSFER_BUFFER_SIZE)
//                .setAutoReconnect(false)
                .setAutoNickChange(true) //Automatically change nick when the current one is in use
                .addListener(ircEventListener) //Add our listener that will be called on Events
                .buildConfiguration();

        //Create our bot with the configuration

        FileTransferProgressWatcher progressWatcher = progressWatcherFactory.getProgressWatcher(event.getPayload());

        IrcBot bot = new IrcBot(configuration, event.getPayload(), progressWatcher);

        eventPublisher.updateDownloadState(State.PREPARED, download);
        //Connect to the server

        TaskExecutor taskExecutor = new SimpleAsyncTaskExecutor(bot.getNick() + " bot");
        taskExecutor.execute(() -> {

            try {

                bot.startBot();

            } catch (IOException | IrcException e) {



            }

        });


        eventPublisher.updateDownloadState(State.CONNECTING, download);

    }

    @EventListener
    public void onDownloadUpdateEvent(DownloadUpdateEvent event) {

        downloadRepository.save(event.getPayload());

        eventPublisher.sendWebsocketEvent(SocketEvents.UPDATED_DOWNLOAD, event.getPayload());

    }

    @EventListener
    public void onDownloadDoneEvent(DownloadDoneEvent event) {

        //Do some stuff here before setting to done!
        event.getPayload().setStatus(State.DONE);
        Download download = downloadRepository.save(event.getPayload());

        eventPublisher.sendWebsocketEvent(SocketEvents.UPDATED_DOWNLOAD, event.getPayload());

    }

}