package de.maggiwuerze.xdccloader.irc;

import org.apache.commons.io.FilenameUtils;
import org.pircbotx.dcc.ReceiveFileTransfer;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.ExceptionEvent;
import org.pircbotx.hooks.events.IncomingFileTransferEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class IrcEventListener extends ListenerAdapter {


    Logger logger = Logger.getLogger("Class IrcEventListener");

    @Override
    public void onConnect(ConnectEvent event) {

        IrcBot bot = event.getBot();
        String message = "xdcc send " + bot.getFileRefId();
        bot.sendIRC().message("Ginpachi-Sensei", message);
    }

    @Override
    public void onGenericMessage(GenericMessageEvent event) {
        //When someone says ?helloworld respond with "Hello World"
        if (event.getMessage().startsWith("?helloworld"))
            event.respond("Hello world!");
    }

    @Override
    public void onException(ExceptionEvent event) throws Exception {

        event.getMessage();

    }


    @Override
    public void onIncomingFileTransfer(IncomingFileTransferEvent event) throws Exception {

        IrcBot bot = event.getBot();
        bot.getDownload().setFilename(event.getSafeFilename());

        //Create this file in the temp directory
        File downloadFile = new File("/download/" + event.getSafeFilename());

        //Receive the file from the user
        ReceiveFileTransfer fileTransfer = event.accept(downloadFile);
        bot.getProgressChecker().setFileTransfer(fileTransfer);

        TaskExecutor taskExecutor = new SimpleAsyncTaskExecutor(event.getBot().getNick() + " transfer");
        taskExecutor.execute(() -> {
            try {
                logger.info("transferring file");
                fileTransfer.transfer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        if (fileTransfer == null) {
            logger.info(String.format("filetransfer is null for %s", bot.getFileRefId()));
        } else {


            bot.getProgressChecker().run();

        }

    }
}