package de.maggiwuerze.xdccloader.irc;

import de.maggiwuerze.xdccloader.events.EventPublisher;
import de.maggiwuerze.xdccloader.model.download.Download;
import de.maggiwuerze.xdccloader.model.download.DownloadState;
import de.maggiwuerze.xdccloader.model.entity.Bot;
import de.maggiwuerze.xdccloader.service.DownloadService;
import java.io.File;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pircbotx.dcc.DccState;
import org.pircbotx.dcc.ReceiveFileTransfer;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.BanListEvent;
import org.pircbotx.hooks.events.ConnectAttemptFailedEvent;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.ExceptionEvent;
import org.pircbotx.hooks.events.FileTransferCompleteEvent;
import org.pircbotx.hooks.events.IncomingFileTransferEvent;
import org.pircbotx.hooks.events.ListenerExceptionEvent;
import org.pircbotx.hooks.events.OutputEvent;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class IrcEventListener extends ListenerAdapter {

	//    final String DL_PATH = File.separator + "opt" + File.separator + "xdcc" + File.separator + "data";
	private static final String DL_PATH = "." + File.separator + "xdcc";
	private final EventPublisher eventPublisher;
	private final DownloadService downloadService;

	@Override
	public void onBanList(BanListEvent event) {
		log.info(String.format("Nick %s was banned", event.getBot().getNick()));
	}

	@Override
	public void onConnect(ConnectEvent event) {

		IrcBot bot = event.getBot();
		Download download = downloadService.getById(bot.getDownloadId());
		Bot targetBot = download.getBot();
		String message = String.format(targetBot.getPattern(), download.getFileRefId());
		bot.sendIRC().message(targetBot.getName(), message);
	}

	@Override
	public void onConnectAttemptFailed(ConnectAttemptFailedEvent event) {
		//TODO: handle errors better... if possible
		if (event.getRemainingAttempts() <= 0) {
			Download download = downloadService.getById(((IrcBot) event.getBot()).getDownloadId());
			download.setStatus(DownloadState.ERROR);
			download.setStatusMessage(event.getConnectExceptions().get(0).getLocalizedMessage());

			Exception exception = event.getConnectExceptions().get(event.getConnectExceptions().keySet().asList().get(0));
			eventPublisher.handleError(event.getBot(), exception);
		} else {
			log.info("Connection failed, remaining attempts: " + event.getRemainingAttempts());
		}
	}

	@Override
	public void onException(ExceptionEvent event) {
		eventPublisher.handleError(event.getBot(), event.getException());
	}

	@Override
	public void onIncomingFileTransfer(IncomingFileTransferEvent event) throws Exception {
		super.onIncomingFileTransfer(event);
		IrcBot bot = event.getBot();
		Download download = downloadService.getById(bot.getDownloadId());
		download.setFilename(event.getSafeFilename());
		String path = DL_PATH + File.separatorChar + event.getSafeFilename();
		File downloadFile = new File(path);

		//Receive the file from the user
		ReceiveFileTransfer fileTransfer = event.accept(downloadFile);
		download.getProgressWatcher().setFileTransfer(fileTransfer);
		TaskExecutor taskExecutor = new SimpleAsyncTaskExecutor(event.getBot().getNick() + " transfer");

		taskExecutor.execute(() -> {
			if (fileTransfer.getFileTransferStatus().getDccState() != DccState.CONNECTING) {
				fileTransfer.transfer();
			}
		});

		if (fileTransfer != null) {
			download.getProgressWatcher().run();
		}
	}

	@Override
	public void onFileTransferComplete(FileTransferCompleteEvent event) {
		IrcBot bot = event.getBot();
		Download download = downloadService.getById(bot.getDownloadId());

		if (event.getTransferStatus().getDccState().equals(DccState.ERROR)) {
			download.setStatusMessage(event.getTransferStatus().getException().getLocalizedMessage());
			eventPublisher.updateDownloadState(DownloadState.ERROR, download);
			log.error(String.format("error on filetransfer for fileID %s", download.getFileRefId()));
			log.error(event.getTransferStatus().getException().toString());
		} else {
			//TODO: should be in FINALIZING when entering here. will have to possibly do stuff set up in configuration before setting to done
			eventPublisher.updateDownloadState(DownloadState.DONE, download);
			log.info(String.format("filetransfer completed for %s", download.getFileRefId()));
		}
	}

	@Override
	public void onListenerException(ListenerExceptionEvent event) {
		eventPublisher.handleError(event.getBot(), event.getException());
	}

	@Override
	public void onOutput(OutputEvent event) {
		String output = event.getRawLine();
	}
}