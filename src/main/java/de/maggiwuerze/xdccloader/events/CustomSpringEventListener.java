package de.maggiwuerze.xdccloader.events;

import de.maggiwuerze.xdccloader.events.download.DownloadCreationEvent;
import de.maggiwuerze.xdccloader.events.download.DownloadDeleteEvent;
import de.maggiwuerze.xdccloader.events.download.DownloadDoneEvent;
import de.maggiwuerze.xdccloader.events.download.DownloadUpdateEvent;
import de.maggiwuerze.xdccloader.irc.IrcBot;
import de.maggiwuerze.xdccloader.irc.IrcEventListener;
import de.maggiwuerze.xdccloader.model.download.Download;
import de.maggiwuerze.xdccloader.model.download.DownloadState;
import de.maggiwuerze.xdccloader.model.entity.Bot;
import de.maggiwuerze.xdccloader.service.DownloadService;
import de.maggiwuerze.xdccloader.util.IpHelper;
import de.maggiwuerze.xdccloader.util.ProgressWatcherFactory;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.pircbotx.Configuration;
import org.pircbotx.exception.IrcException;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomSpringEventListener {

	private final ProgressWatcherFactory progressWatcherFactory;
	private final IrcEventListener ircEventListener;
	private final EventPublisher eventPublisher;
	private final DownloadService downloadService;

	@EventListener
	public void onDownloadCreationEvent(DownloadCreationEvent event) {

		Download download = downloadService.getById(event.getPayload());

		eventPublisher.updateDownloadState(DownloadState.PREPARING, download);

		String username = RandomStringUtils.randomAlphabetic(1) + RandomStringUtils.random(7, true, true);
		Bot targetBot = download.getBot();

		Configuration configuration = new Configuration.Builder()
			.setName(username) //Set the nick of the bot. CHANGE IN YOUR CODE
			.addServer(targetBot.getServer().getServerUrl()) //Join the freenode network
			.addAutoJoinChannel(targetBot.getChannel().getName()) //Join the official #pircbotx channel
			.setAutoReconnect(true)
			.setAutoReconnectAttempts(5)
			.setAutoNickChange(true) //Automatically change nick when the current one is in use
			.addListener(ircEventListener) //Add our listener that will be called on Events
			.setDccPublicAddress(IpHelper.getPublicIp())
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

				log.error(e.toString());
			}
		});

		eventPublisher.updateDownloadState(DownloadState.CONNECTING, download);
	}

	@EventListener
	public void onDownloadUpdateEvent(DownloadUpdateEvent event) {

		downloadService.update(downloadService.getById(event.getPayload()));
		eventPublisher.sendWebsocketEvent(SocketEvents.UPDATED_DOWNLOAD, downloadService.getById(event.getPayload()));
	}

	@EventListener
	public void onDownloadDeleteEvent(DownloadDeleteEvent event) {

		eventPublisher.sendWebsocketEvent(SocketEvents.DELETED_DOWNLOAD, downloadService.getById(event.getPayload()));
		downloadService.remove(event.getPayload());
	}

	@EventListener
	public void onDownloadDoneEvent(DownloadDoneEvent event) {

		//Do some stuff here before setting to done!
		Download download = downloadService.getById(event.getPayload());
		download.setStatus(DownloadState.DONE);
		downloadService.update(download);

		eventPublisher.sendWebsocketEvent(SocketEvents.UPDATED_DOWNLOAD, downloadService.getById(event.getPayload()));
	}
}