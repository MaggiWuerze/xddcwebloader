package de.maggiwuerze.xdccloader.irc;

import de.maggiwuerze.xdccloader.service.DownloadService;
import lombok.NonNull;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;

public class IrcBot extends PircBotX {

	private final Long downloadId;
	private DownloadService downloadService;

	/**
	 * Constructs a PircBotX with the provided configuration.
	 *
	 * @param configuration Fully built Configuration
	 */
	public IrcBot(@NonNull Configuration configuration, @NonNull Long downloadId) {
		super(configuration);
		this.downloadId = downloadId;
	}

	public Long getDownloadId() {
		return downloadId;
	}
}
