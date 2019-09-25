package de.maggiwuerze.xdccloader.persistency;

import de.maggiwuerze.xdccloader.model.entity.*;
import de.maggiwuerze.xdccloader.security.UserRole;
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
    ChannelRepository channelRepository;
    @Autowired
    ServerRepository serverRepository;
    @Autowired
    IrcUserRepository ircUserRepository;
    @Autowired
    UserSettingsRepository userSettingsRepository;

    @Transactional
    @Override
    public void run(String ... strings) {

        String username = "Bilbo";
        String password = new BCryptPasswordEncoder(11).encode("Baggins");
        UserSettings userSettings = userSettingsRepository.save(new UserSettings());
        this.userRepository.save(new User(username, password, UserRole.USER.getExternalString(), true, userSettings));


        Channel channel = channelRepository.save(new Channel("#HorribleSubs"));
        Channel dbChannel = channelRepository.save(new Channel("#Lobby"));

        Server server = serverRepository.save(new Server("Rizon" , "irc.rizon.net"));

		String targetBotName = "Ginpachi-Sensei";
        Bot bot = this.ircUserRepository.save(new Bot(server, channel, targetBotName, "xdcc send #%s"));

        targetBotName = "CR-HOLLAND|NEW_DOWNLOAD";
        Bot bot2 = this.ircUserRepository.save(new Bot(server, dbChannel, targetBotName, "xdcc send #%s"));

        targetBotName = "CR-ARCHIVE|1080p";
        Bot bot3 = this.ircUserRepository.save(new Bot(server, channel, targetBotName, "xdcc send #%s"));

    }
}