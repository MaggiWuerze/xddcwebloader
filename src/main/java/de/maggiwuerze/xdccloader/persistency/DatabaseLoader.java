package de.maggiwuerze.xdccloader.persistency;

import de.maggiwuerze.xdccloader.model.entity.Bot;
import de.maggiwuerze.xdccloader.model.entity.Channel;
import de.maggiwuerze.xdccloader.model.entity.Server;
import de.maggiwuerze.xdccloader.model.entity.User;
import de.maggiwuerze.xdccloader.model.entity.UserSettings;
import de.maggiwuerze.xdccloader.security.UserRole;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


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
	public void run(String... strings) {

		String username = "Bilbo";
		String password = new BCryptPasswordEncoder(11).encode("Baggins");
		UserSettings userSettings = userSettingsRepository.save(new UserSettings());
		this.userRepository.save(new User(username, password, UserRole.USER.getExternalString(), true, userSettings));

		Server server = serverRepository.save(new Server("Rizon", "irc.rizon.net"));
		Channel channel = channelRepository.save(new Channel("#subsplease"));
		Bot bot = this.ircUserRepository.save(new Bot(server, channel, "Ginpachi-Sensei", "xdcc send #%s", 3L));
	}
}