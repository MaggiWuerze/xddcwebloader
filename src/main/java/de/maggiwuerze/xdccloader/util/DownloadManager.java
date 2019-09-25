package de.maggiwuerze.xdccloader.util;

import de.maggiwuerze.xdccloader.model.download.Download;
import de.maggiwuerze.xdccloader.model.DownloadState;
import de.maggiwuerze.xdccloader.model.entity.Bot;
import de.maggiwuerze.xdccloader.model.entity.UserSettings;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public final class DownloadManager {

    private static DownloadManager INSTANCE;

    private AtomicLong idCount = new AtomicLong(0);

    Map<Long, Download> downloads = Collections.synchronizedMap(new HashMap<>());

    Map<Bot, List<Download>> bots = Collections.synchronizedMap(new HashMap<>());


    private DownloadManager() {
    }

    public static DownloadManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DownloadManager();
        }

        return INSTANCE;
    }

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
}
