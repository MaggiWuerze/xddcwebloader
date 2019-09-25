package de.maggiwuerze.xdccloader.events;

import de.maggiwuerze.xdccloader.events.download.DownloadCreationEvent;
import de.maggiwuerze.xdccloader.events.download.DownloadDeleteEvent;
import de.maggiwuerze.xdccloader.events.download.DownloadDoneEvent;
import de.maggiwuerze.xdccloader.events.download.DownloadUpdateEvent;
import de.maggiwuerze.xdccloader.irc.IrcBot;
import de.maggiwuerze.xdccloader.irc.IrcEventListener;
import de.maggiwuerze.xdccloader.model.DownloadState;
import de.maggiwuerze.xdccloader.model.download.Download;
import de.maggiwuerze.xdccloader.model.entity.Bot;
import de.maggiwuerze.xdccloader.util.DownloadManager;
import de.maggiwuerze.xdccloader.util.FileTransferProgressWatcher;
import de.maggiwuerze.xdccloader.util.ProgressWatcherFactory;
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

    Logger LOG = Logger.getLogger("Class CustomSpringEventListener");

    @Autowired
    ProgressWatcherFactory progressWatcherFactory;

    @Autowired
    IrcEventListener ircEventListener;

    @Autowired
    EventPublisher eventPublisher;

    DownloadManager downloadManager = DownloadManager.getInstance();

    @EventListener
    public void onDownloadCreationEvent(DownloadCreationEvent event) {

        Download download = downloadManager.getById(event.getPayload());

        eventPublisher.updateDownloadState(DownloadState.PREPARING, download);

        String username = RandomStringUtils.randomAlphabetic(1) + RandomStringUtils.random(7, true, true);
        Bot targetBot = download.getBot();

        Configuration configuration = new Configuration.Builder()
                .setName(username) //Set the nick of the bot. CHANGE IN YOUR CODE
                .addServer(targetBot.getServer().getServerUrl()) //Join the freenode network
                .addAutoJoinChannel(targetBot.getChannel().getName()) //Join the official #pircbotx channel
                .setAutoReconnectAttempts(5)
                .setAutoNickChange(true) //Automatically change nick when the current one is in use
                .addListener(ircEventListener) //Add our listener that will be called on Events
        .buildConfiguration();

        //Create our bot with the configuration
        download.setProgressWatcher(progressWatcherFactory.getProgressWatcher(download.getId()));

        IrcBot bot = new IrcBot(configuration, download.getId());

        eventPublisher.updateDownloadState(DownloadState.PREPARED, download);

        TaskExecutor taskExecutor = new SimpleAsyncTaskExecutor(bot.getNick() + "_bot");
        taskExecutor.execute(() -> {

            try {

                bot.startBot();

            } catch (IOException | IrcException e) {

                LOG.severe(e.toString());

            }

        });

        eventPublisher.updateDownloadState(DownloadState.CONNECTING, download);

    }

    @EventListener
    public void onDownloadUpdateEvent(DownloadUpdateEvent event) {

        downloadManager.update(downloadManager.getById(event.getPayload()));
        eventPublisher.sendWebsocketEvent(SocketEvents.UPDATED_DOWNLOAD, downloadManager.getById(event.getPayload()));

    }

    @EventListener
    public void onDownloadDeleteEvent(DownloadDeleteEvent event) {

        eventPublisher.sendWebsocketEvent(SocketEvents.DELETED_DOWNLOAD, downloadManager.getById(event.getPayload()));
        downloadManager.remove(event.getPayload());

    }

    @EventListener
    public void onDownloadDoneEvent(DownloadDoneEvent event) {

        //Do some stuff here before setting to done!
        Download download = downloadManager.getById(event.getPayload());
        download.setStatus(DownloadState.DONE);
        downloadManager.update(download);

        eventPublisher.sendWebsocketEvent(SocketEvents.UPDATED_DOWNLOAD, downloadManager.getById(event.getPayload()));

    }

}