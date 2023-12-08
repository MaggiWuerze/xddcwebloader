package de.maggiwuerze.xdccloader.persistence;

import de.maggiwuerze.xdccloader.model.entity.Bot;
import de.maggiwuerze.xdccloader.model.entity.Channel;
import de.maggiwuerze.xdccloader.model.entity.Server;
import de.maggiwuerze.xdccloader.model.entity.UserSettings;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@Component
class DatabaseLoader implements CommandLineRunner {


	@Autowired
	SettingsRepository settingsRepository;
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
		UserSettings userSettings = userSettingsRepository.save(new UserSettings());
		this.settingsRepository.save(userSettings);

		Server server = serverRepository.save(new Server("Rizon", "irc.rizon.net"));
		Channel channel = channelRepository.save(new Channel("#subsplease"));
		Bot bot = this.ircUserRepository.save(new Bot(server, channel, "Ginpachi-Sensei", "xdcc send #%s", 3L));

	}
}