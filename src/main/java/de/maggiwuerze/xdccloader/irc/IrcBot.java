package de.maggiwuerze.xdccloader.irc;

import de.maggiwuerze.xdccloader.model.Channel;
import de.maggiwuerze.xdccloader.model.Download;
import de.maggiwuerze.xdccloader.model.IrcUser;
import de.maggiwuerze.xdccloader.model.Server;
import de.maggiwuerze.xdccloader.util.FileTransferProgressWatcher;
import lombok.NonNull;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;

public class IrcBot extends PircBotX {

    private Download download;
    private FileTransferProgressWatcher progressChecker;


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
        return download.getServer();
    }

    public Channel getChannel() {
        return download.getChannel();
    }

    public IrcUser getIrcUser() {
        return download.getUser();
    }

    public String getFileRefId() {
        return download.getFileRefId();
    }

    public Download getDownload() {
        return download;
    }
}
