package de.maggiwuerze.xdccloader.irc;

import de.maggiwuerze.xdccloader.model.Channel;
import de.maggiwuerze.xdccloader.model.Download;
import de.maggiwuerze.xdccloader.model.TargetBot;
import de.maggiwuerze.xdccloader.model.Server;
import de.maggiwuerze.xdccloader.util.FileTransferProgressWatcher;
import lombok.NonNull;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;

import java.io.IOException;
import java.util.logging.Logger;

public class IrcBot extends PircBotX {

    private Download download;
    private FileTransferProgressWatcher progressChecker;
    Logger logger = Logger.getLogger("Class IrcBot");

    /**
     * Constructs a PircBotX with the provided configuration.
     *
     * @param configuration Fully built Configuration
     */
    public IrcBot(@NonNull Configuration configuration) {
        super(configuration);
    }

    public IrcBot(@NonNull Configuration configuration, Download download) {
        super(configuration);
        this.download = download;
    }
    public IrcBot(@NonNull Configuration configuration, Download download, FileTransferProgressWatcher watcher) {
        super(configuration);
        this.download = download;
        this.progressChecker = watcher;
    }

    public FileTransferProgressWatcher getProgressChecker() {
        return progressChecker;
    }

    public void setProgressChecker(FileTransferProgressWatcher progressChecker) {
        this.progressChecker = progressChecker;
    }

    public Server getServer() {
        return download.getTargetBot().getServer();
    }

    public Channel getChannel() {
        return download.getTargetBot().getChannel();
    }

    public TargetBot getIrcUser() {
        return download.getTargetBot();
    }

    public String getFileRefId() {
        return download.getFileRefId();
    }

    public Download getDownload() {
        return download;
    }
}
