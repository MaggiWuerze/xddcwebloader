package de.maggiwuerze.xdccloader.irc;

import de.maggiwuerze.xdccloader.events.EventPublisher;
import de.maggiwuerze.xdccloader.model.DownloadState;
import de.maggiwuerze.xdccloader.model.entity.Bot;

import java.util.logging.Level;

import de.maggiwuerze.xdccloader.model.download.Download;
import de.maggiwuerze.xdccloader.util.DownloadManager;
import org.pircbotx.dcc.DccState;
import org.pircbotx.dcc.ReceiveFileTransfer;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.logging.Logger;

@Component
public class IrcEventListener extends ListenerAdapter {

    //    final String DL_PATH = File.separator + "opt" + File.separator + "xdcc" + File.separator + "data";
    final String DL_PATH = File.separator + "home" + File.separator + "dap" + File.separator + "download";

    final Logger LOG = Logger.getLogger("Class IrcEventListener");

    @Autowired
    private EventPublisher eventPublisher;

    DownloadManager downloadManager = DownloadManager.getInstance();

    @Override
    public void onConnect(ConnectEvent event) {

        IrcBot bot = event.getBot();
        Bot targetBot = bot.getDownload().getBot();
        String message = String.format(targetBot.getPattern(), bot.getFileRefId());
        bot.sendIRC().message(targetBot.getName(), message);
    }

//    @Override
//    public void onGenericMessage(GenericMessageEvent event) {
////        When someone says ?helloworld respond with "Hello World"
//        if (event.getMessage().contains("You have a DCC pending"))
//            event.respond("Hello world!");
//    }

    @Override
    public void onException(ExceptionEvent event) {

        eventPublisher.handleError(event.getBot(), event.getException());

    }

    @Override
    public void onListenerException(ListenerExceptionEvent event) {

        eventPublisher.handleError(event.getBot(), event.getException());

    }

    @Override
    public void onOutput(OutputEvent event) {

        String output = event.getRawLine();

    }

    @Override
    public void onBanList(BanListEvent event) {

        LOG.info(String.format("Nick %s was banned", event.getBot().getNick()));

    }

    @Override
    public void onConnectAttemptFailed(ConnectAttemptFailedEvent event) {

        //TODO: handle errors better... if possible
        if (event.getRemainingAttempts() <= 0) {

            Download download = downloadManager.getById(((IrcBot) event.getBot()).getDownload().getId());
            download.setStatus(DownloadState.ERROR);
            download.setStatusMessage(event.getConnectExceptions().get(0).getLocalizedMessage());

            Exception exception = event.getConnectExceptions().get(event.getConnectExceptions().keySet().asList().get(0));
            eventPublisher.handleError(event.getBot(), exception);

        } else {

            LOG.info("Connection failed, remaining attempts: " + event.getRemainingAttempts());

        }


    }

    @Override
    public void onFileTransferComplete(FileTransferCompleteEvent event) {

        IrcBot bot = event.getBot();
        if (event.getTransferStatus().getDccState().equals(DccState.ERROR)) {

            bot.getDownload().setStatusMessage(event.getTransferStatus().getException().getLocalizedMessage());
            eventPublisher.updateDownloadState(DownloadState.ERROR, bot.getDownload());
            LOG.log(Level.SEVERE, String.format("error on filetransfer for bot %s", bot.getFileRefId()));
            LOG.log(Level.SEVERE, event.getTransferStatus().getException().toString());

        } else {

            //TODO: should be in FINALIZING when entering here. will have to possibly do stuff set up in configuration before setting to done
            eventPublisher.updateDownloadState(DownloadState.DONE, bot.getDownload());
            LOG.info(String.format("filetransfer completed for %s", bot.getFileRefId()));

        }
    }

    @Override
    public void onIncomingFileTransfer(IncomingFileTransferEvent event) throws Exception {

        IrcBot bot = event.getBot();
        Download download = bot.getDownload();
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
}