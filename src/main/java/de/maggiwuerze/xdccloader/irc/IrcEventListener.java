package de.maggiwuerze.xdccloader.irc;

import de.maggiwuerze.xdccloader.events.EventPublisher;
import de.maggiwuerze.xdccloader.model.TargetBot;
import de.maggiwuerze.xdccloader.util.SocketEvents;
import de.maggiwuerze.xdccloader.util.State;
import org.pircbotx.dcc.ReceiveFileTransfer;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectAttemptFailedEvent;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.ExceptionEvent;
import org.pircbotx.hooks.events.IncomingFileTransferEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

@Component
public class IrcEventListener extends ListenerAdapter {

    final String path = System.getProperty("user.home") + File.separator + "downloads";

    Logger logger = Logger.getLogger("Class IrcEventListener");

    @Autowired
    private EventPublisher eventPublisher;

    @Override
    public void onConnect(ConnectEvent event) {

        IrcBot bot = event.getBot();
        TargetBot targetBot = bot.getDownload().getTargetBot();
        String message = String.format(targetBot.getPattern(), bot.getFileRefId());
        bot.sendIRC().message(targetBot.getName(), message);
    }

    @Override
    public void onGenericMessage(GenericMessageEvent event) {
        //When someone says ?helloworld respond with "Hello World"
        if (event.getMessage().startsWith("?helloworld"))
            event.respond("Hello world!");
    }

    @Override
    public void onException(ExceptionEvent event) {

        eventPublisher.handleError(event.getBot(), event.getException());

    }

    @Override
    public void onConnectAttemptFailed(ConnectAttemptFailedEvent event) {

        Exception exception = event.getConnectExceptions().get(event.getConnectExceptions().keySet().asList().get(0));
        eventPublisher.handleError(event.getBot(), exception);

    }



    @Override
    public void onIncomingFileTransfer(IncomingFileTransferEvent event) throws Exception {

        IrcBot bot = event.getBot();
        bot.getDownload().setFilename(event.getSafeFilename());

        //Create this file in the temp directory
//        String path = "C:" + File.separatorChar + "ircDownload" + File.separatorChar + event.getSafeFilename();
        File downloadFile = new File(path);

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