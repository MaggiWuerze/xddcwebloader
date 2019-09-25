package de.maggiwuerze.xdccloader.irc;

import de.maggiwuerze.xdccloader.model.download.Download;
import de.maggiwuerze.xdccloader.util.DownloadManager;
import lombok.NonNull;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;

public class IrcBot extends PircBotX {

    private Long downloadId;

    /**
     * Constructs a PircBotX with the provided configuration.
     *
     * @param configuration Fully built Configuration
     */
    public IrcBot(@NonNull Configuration configuration, @NonNull Long downloadId) {
        super(configuration);
        this.downloadId = downloadId;
    }

    public String getFileRefId() {
        return getDownload().getFileRefId();
    }

    public Long getDownloadId() {
        return downloadId;
    }

    public Download getDownload() {
        return DownloadManager.getInstance().getById(getDownloadId());
    }

}
