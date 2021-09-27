package de.maggiwuerze.xdccloader.model.download;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.maggiwuerze.xdccloader.model.entity.Bot;
import de.maggiwuerze.xdccloader.util.FileTransferProgressWatcher;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Download {

	Long id;

	Bot bot;

	String fileRefId;

	LocalDateTime date = LocalDateTime.now();

	Double progress = 0.0;

	String filename = "unknown";

	String filesize = "-";

	String averageSpeed = "0 Kb/s";

	String timeRemaining = "-";

	DownloadState status = DownloadState.UNKNOWN;

	String statusMessage = "";

	@JsonIgnore
	private FileTransferProgressWatcher progressWatcher;

	public Download(Bot user, String fileRefId) {
		this.bot = user;
		this.fileRefId = fileRefId;
	}
}
