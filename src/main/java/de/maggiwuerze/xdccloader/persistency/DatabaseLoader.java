package de.maggiwuerze.xdccloader.persistency;

import de.maggiwuerze.xdccloader.model.*;
import de.maggiwuerze.xdccloader.util.Role;
import de.maggiwuerze.xdccloader.util.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.*;
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
        this.userRepository.save(new User(username, password, Role.USER.getExternalString()));

		username = "Frodowefweweg";
        IrcUser userA = this.ircUserRepository.save(new IrcUser(username));

		username = "Samweiseg2332g";
        IrcUser userB = this.ircUserRepository.save(new IrcUser(username));

		username = "Merry2g24g34w";
        IrcUser userC = this.ircUserRepository.save(new IrcUser(username));

		username = "Pipping234g24w";
        IrcUser userD = this.ircUserRepository.save(new IrcUser(username));


        Channel channel = channelRepository.save(new Channel("#HorribleSubs"));
        Channel dbChannel = channelRepository.save(new Channel("#Lobby"));

        Server server = serverRepository.save(new Server("Rizon" , "irc.rizon.net"));

        downloadRepository.save(new Download(channel, server, userA,"#34235"));
        downloadRepository.save(new Download(channel, server, userB, "#12334"));
        downloadRepository.save(new Download(dbChannel, server, userC, "#3421"));

    }
}