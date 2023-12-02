package de.maggiwuerze.xdccloader.util;

import de.maggiwuerze.xdccloader.events.download.DownloadDeleteEvent;
import de.maggiwuerze.xdccloader.events.download.DownloadUpdateEvent;
import de.maggiwuerze.xdccloader.model.download.Download;
import de.maggiwuerze.xdccloader.model.download.DownloadState;
import de.maggiwuerze.xdccloader.service.DownloadService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.pircbotx.dcc.DccState;
import org.pircbotx.dcc.ReceiveFileTransfer;
import org.springframework.context.ApplicationEventPublisher;

@Slf4j
public class FileTransferProgressWatcher {

	private final ApplicationEventPublisher applicationEventPublisher;
	ReceiveFileTransfer fileTransfer;
	Long downloadId;
	DownloadService downloadService;
	ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
	ScheduledFuture<?> schedulerResult;

	public FileTransferProgressWatcher(
		Long downloadId,
		ApplicationEventPublisher applicationEventPublisher,
		DownloadService downloadService
	) {
		this.downloadId = downloadId;
		this.applicationEventPublisher = applicationEventPublisher;
		this.downloadService = downloadService;
	}

	public void run() {
		log.info("starting progress watcher for download with id :" + downloadId);
		downloadService.getById(downloadId).setStatus(DownloadState.TRANSMITTING);
		applicationEventPublisher.publishEvent(new DownloadUpdateEvent(this, downloadId));
		schedulerResult = exec.scheduleAtFixedRate(() -> {

			try {
				Download download = downloadService.getById(downloadId);

				if (download.getFilesize().equals("-")) {
					updateFileSize(download);
				}

				double newProgress = fileTransfer.getFileTransferStatus().getPercentageComplete();
				newProgress = BigDecimal.valueOf(newProgress)
					.setScale(2, RoundingMode.HALF_UP)
					.doubleValue();
				download.setTimeRemaining(formatRemainingTime());
				checkDownloadProgress(download, newProgress);
			} catch (Exception e) {
				log.warn("Error in progressWatcher", e);
			}
		}, 0, 1, TimeUnit.SECONDS);
	}

	private void checkDownloadProgress(Download download, double newProgress) {
		if (download.getStatus().equals(DownloadState.STOPPED)) {
			applicationEventPublisher.publishEvent(new DownloadDeleteEvent(this, download.getId()));
			schedulerResult.cancel(true);
		} else if (fileTransfer.getFileTransferStatus().getDccState().equals(DccState.ERROR)) {
			updateDownloadStatus(download, DownloadState.ERROR, fileTransfer.getFileTransferStatus().getException().getMessage());
			log.debug(String.format("error on filetransfer for fileID %s", download.getFileRefId()));
			log.debug(fileTransfer.getFileTransferStatus().getException().getMessage());
			schedulerResult.cancel(true);
		} else if (newProgress == 100.0 && fileTransfer.getFileTransferStatus().isSuccessful()) {
			updateDownloadStatus(download, DownloadState.FINALIZING, "");
			//TODO: some stuff to finalize
			updateDownloadStatus(download, DownloadState.DONE, "");
			schedulerResult.cancel(true);
		} else {
			download.setProgress(newProgress);
			String averageSpeed = FilesizeFormatter.createAutoReadableString(fileTransfer.getFileTransferStatus().getAverageBytesPerSecond());
			download.setAverageSpeed(averageSpeed);
			applicationEventPublisher.publishEvent(new DownloadUpdateEvent(this, download.getId()));
		}
	}

	private void updateDownloadStatus(Download download, DownloadState state, String statusMessage) {
		download.setStatus(state);
		download.setStatusMessage(statusMessage);
		applicationEventPublisher.publishEvent(new DownloadUpdateEvent(this, download.getId()));
	}

	public void cancel(boolean mayInterrupt) {
		if (schedulerResult != null) {
			schedulerResult.cancel(mayInterrupt);
		}
	}

	public void setFileTransfer(ReceiveFileTransfer fileTransfer) {
		this.fileTransfer = fileTransfer;
	}

	private String formatRemainingTime() {

		long secondsRemaining = fileTransfer.getFileTransferStatus().getFileSize() - fileTransfer.getFileTransferStatus().getBytesTransfered();
		if (fileTransfer.getFileTransferStatus().getAverageBytesPerSecond() != 0) {
			secondsRemaining = secondsRemaining / fileTransfer.getFileTransferStatus().getAverageBytesPerSecond();
		}

		long seconds = secondsRemaining;
		long minutes = 0;
		long hours = 0;
		long days = 0;

		String result = String.format("%02dm:%02ds", minutes, seconds);

		if (secondsRemaining > 60) {
			minutes = seconds / 60;
			seconds = seconds % 60;
			result = String.format("%02dm:%02ds", minutes, seconds);

			if (minutes > 60) {
				hours = minutes / 60;
				minutes = minutes % 60;
				result = String.format("%02dh:%02dm:%02ds", hours, minutes, seconds);

				if (hours > 24) {
					days = hours / 24;
					hours = hours % 24;
					result = String.format("%02dd:%02dh:%02dm:%02ds", days, hours, minutes, seconds);
				}
			}
		}

		return result;
	}

	private void updateFileSize(Download download) {
		long fileSize = fileTransfer.getFileTransferStatus().getFileSize();
		String readableString = FilesizeFormatter.createAutoReadableString(fileSize);
		download.setFilesize(readableString);
	}
}
