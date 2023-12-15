package de.maggiwuerze.xdccloader.service;

import de.maggiwuerze.xdccloader.irc.IrcBot;
import de.maggiwuerze.xdccloader.irc.IrcEventListener;
import de.maggiwuerze.xdccloader.model.download.Download;
import de.maggiwuerze.xdccloader.model.entity.Bot;
import de.maggiwuerze.xdccloader.model.entity.Channel;
import de.maggiwuerze.xdccloader.model.entity.Server;
import de.maggiwuerze.xdccloader.model.forms.TargetBotForm;
import de.maggiwuerze.xdccloader.persistence.TargetBotRepository;
import de.maggiwuerze.xdccloader.util.IpHelper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.pircbotx.Configuration;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class IrcBotService {

	private final IrcEventListener ircEventListener;
	private final Map<Bot, IrcBot> activeBots = new HashMap<>();

	public IrcBot getIrcBotForDownload(Download download) {
		return Optional.ofNullable(activeBots.get(download.getBot())).orElseGet(() -> getNewBot(download));
	}

	public IrcBot getNewBot(Download download) {

		String username = RandomStringUtils.randomAlphabetic(1) + RandomStringUtils.random(7, true, true);
		Bot targetBot = download.getBot();

		Configuration configuration = new Configuration.Builder()
			.setName(username) //Set the nick of the bot. CHANGE IN YOUR CODE
			.addServer(targetBot.getServer().getServerUrl()) //Join the freenode network
			.addAutoJoinChannel(targetBot.getChannel().getName()) //Join the official #pircbotx channel
			.setAutoReconnect(true)
			.setAutoReconnectAttempts(5)
			.setAutoNickChange(true) //Automatically change nick when the current one is in use
			.addListener(ircEventListener) //Add our listener that will be called on Events
			.setDccPublicAddress(IpHelper.getPublicIp())
			.setDccPorts(List.of(10000))
			.buildConfiguration();
		IrcBot bot = new IrcBot(configuration, download.getId());
		activeBots.put(targetBot, bot);

		return bot;
	}
}
