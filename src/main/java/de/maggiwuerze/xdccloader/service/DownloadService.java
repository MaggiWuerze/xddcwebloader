package de.maggiwuerze.xdccloader.service;

import de.maggiwuerze.xdccloader.model.download.Download;
import de.maggiwuerze.xdccloader.model.download.DownloadState;
import de.maggiwuerze.xdccloader.model.entity.Bot;
import de.maggiwuerze.xdccloader.model.entity.UserSettings;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Service for adding Downloads to the queue and retrieving them
 */
@Component
@Slf4j
public final class DownloadService {

    private final AtomicLong idCount = new AtomicLong(0);
    private boolean downloadFolderReady = false;
    private Map<Long, Download> downloads = Collections.synchronizedMap(new HashMap<>());
    private Map<Bot, List<Download>> bots = Collections.synchronizedMap(new HashMap<>());

    public void addDownloadToBotQueue(Download download) {
        Long id = idCount.incrementAndGet();
        download.setId(id);
        downloads.put(id, download);
    }

    public void addDownloadToBotQueue(Bot bot,Download download) {

        Long id = idCount.incrementAndGet();
        download.setId(id);
        downloads.put(id, download);

        bots.get(bot);
    }

    public Download getById(Long id) {
        return downloads.get(id);
    }

    public void remove(Long id) {
        downloads.remove(id);
    }

    public List<Download> findAllByOrderByProgressDesc() {
        return downloads.values().stream().sorted(Comparator.comparing(Download::getProgress)).collect(Collectors.toList());
    }

    public List<Download> findAllByStatusInOrderByProgress(List<DownloadState> states) {
        return downloads.values().stream().filter(dl -> states.contains(dl.getStatus())).sorted(Comparator.comparing(Download::getProgress)).collect(Collectors.toList());
    }

    public List<Download> findAllByStatusOrderByProgressDesc(DownloadState state) {
        return downloads.values().stream().filter(dl -> state.equals(dl.getStatus())).sorted(Comparator.comparing(Download::getProgress)).collect(Collectors.toList());
    }

    public void update(Download download) {
        downloads.replace(download.getId(), download);
    }

    /**
     * creating download folder if necessary
     */
    @PostConstruct
    private void createDownloadFolderIfNecessary(){
        String path = "." + File.separator +"xdcc";
        File customDir = new File(path);

        if (customDir.exists()) {
            log.info("download folder exists in " + path);
        } else if (customDir.mkdirs()) {
            log.info("download folder was created in " + path);
        } else {
            throw new RuntimeException("target folder" + customDir + "could not be created");
        }
        downloadFolderReady = true;
    }
}
