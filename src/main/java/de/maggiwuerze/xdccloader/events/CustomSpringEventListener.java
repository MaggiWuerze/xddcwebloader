package de.maggiwuerze.xdccloader.events;

import de.maggiwuerze.xdccloader.irc.IrcBot;
import de.maggiwuerze.xdccloader.irc.IrcEventListener;
import de.maggiwuerze.xdccloader.model.Download;
import de.maggiwuerze.xdccloader.persistency.DownloadRepository;
import de.maggiwuerze.xdccloader.util.FileTransferProgressWatcher;
import de.maggiwuerze.xdccloader.util.ProgressWatcherFactory;
import de.maggiwuerze.xdccloader.util.SocketEvents;
import de.maggiwuerze.xdccloader.util.State;
import org.pircbotx.Configuration;
import org.pircbotx.exception.IrcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomSpringEventListener{
//public class CustomSpringEventListener implements ApplicationListener<DownloadCreationEvent>, ApplicationListener<DownloadUpdateEvent> {

    @Autowired
    DownloadRepository downloadRepository;

    @Autowired
    private SimpMessagingTemplate websocket;

    @Autowired
    ProgressWatcherFactory progressWatcherFactory;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    final String MESSAGE_PREFIX = "/topic";


    @EventListener
    public void onDownloadCreationEvent(DownloadCreationEvent event) {

        Download download = downloadRepository.findById(event.getDownload().getId()).get();

        updateDownloadState(State.PREPARING, download);

        Configuration configuration = new Configuration.Builder()
                .setName(event.getDownload().getUser().getName()) //Set the nick of the bot. CHANGE IN YOUR CODE
                .addServer(event.getDownload().getServer().getServerUrl()) //Join the freenode network
                .addAutoJoinChannel(event.getDownload().getChannel().getName()) //Join the official #pircbotx channel
                .setAutoNickChange(true) //Automatically change nick when the current one is in use
                .addListener(new IrcEventListener()) //Add our listener that will be called on Events
                .buildConfiguration();

        //Create our bot with the configuration

        FileTransferProgressWatcher progressWatcher = progressWatcherFactory.getProgressWatcher(event.getDownload());

        IrcBot bot = new IrcBot(configuration, event.getDownload(), progressWatcher);

        updateDownloadState(State.PREPARED, download);
        //Connect to the server

        TaskExecutor taskExecutor = new SimpleAsyncTaskExecutor(bot.getNick() + " bot");
        taskExecutor.execute(() -> {
            try {
                bot.startBot();
            } catch (IOException | IrcException e) {
                e.printStackTrace();
            }
        });

        updateDownloadState(State.CONNECTING, download);

    }

    @EventListener
    public void onDownloadUpdateEvent(DownloadUpdateEvent event){

        downloadRepository.save(event.getDownload());

        this.websocket.convertAndSend(
                MESSAGE_PREFIX + SocketEvents.UPDATE.getRoute(), event.getDownload());

    }

    @EventListener
    public void onDownloadDoneEvent(DownloadDoneEvent event){

        downloadRepository.save(event.getDownload());

        this.websocket.convertAndSend(
                MESSAGE_PREFIX + SocketEvents.UPDATE.getRoute(), event.getDownload());

    }



    public void updateDownloadState(State state, Download download){

        download.setStatus(State.PREPARING);
        downloadRepository.save(download);

        DownloadUpdateEvent downloadUpdateEvent = new DownloadUpdateEvent(this, download);
        applicationEventPublisher.publishEvent(downloadUpdateEvent);

    }
}