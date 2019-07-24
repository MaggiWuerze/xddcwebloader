package de.maggiwuerze.xdccloader.persistency;

import de.maggiwuerze.xdccloader.model.*;
import de.maggiwuerze.xdccloader.util.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;


@Component
class DatabaseLoader implements CommandLineRunner {


    @Autowired
    UserRepository userRepository;
    @Autowired
    DownloadRepository downloadRepository;
    @Autowired
    ChannelRepository channelRepository;
    @Autowired
    ServerRepository serverRepository;
    @Autowired
    IrcUserRepository ircUserRepository;



    @Transactional
    @Override
    public void run(String ... strings) {

        String username = "Bilbo";
        String password = new BCryptPasswordEncoder(11).encode("Baggins");
        this.userRepository.save(new User(username, password, Role.USER.getExternalString(), true));


        Channel channel = channelRepository.save(new Channel("#HorribleSubs"));
        Channel dbChannel = channelRepository.save(new Channel("#Lobby"));

        Server server = serverRepository.save(new Server("Rizon" , "irc.rizon.net"));

		String targetBotName = "Ginpachi-Sensei";
        TargetBot targetBot = this.ircUserRepository.save(new TargetBot(server, channel, targetBotName, "xdcc send %s"));

        targetBotName = "CR-HOLLAND|NEW_DOWNLOAD";
        TargetBot targetBot2 = this.ircUserRepository.save(new TargetBot(server, dbChannel, targetBotName, "xdcc send %s"));

        targetBotName = "CR-ARCHIVE|1080p";
        TargetBot targetBot3 = this.ircUserRepository.save(new TargetBot(server, channel, targetBotName, "xdcc send %s"));

    }
}