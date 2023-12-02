package de.maggiwuerze.xdccloader.service;

import de.maggiwuerze.xdccloader.model.entity.Bot;
import de.maggiwuerze.xdccloader.model.entity.Channel;
import de.maggiwuerze.xdccloader.model.entity.Server;
import de.maggiwuerze.xdccloader.model.entity.User;
import de.maggiwuerze.xdccloader.model.forms.TargetBotForm;
import de.maggiwuerze.xdccloader.persistence.TargetBotRepository;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BotService {

	private final TargetBotRepository targetBotRepository;
	private final UserService userService;
	private final ServerService serverService;
	private final ChannelService channelService;

	public List<Bot> list() {
		return targetBotRepository.findAll();
	}

	public Bot save(Principal principal, TargetBotForm form) {

		User user = userService.findUserByName(principal.getName());
		Server server = serverService.findById(form.getServerId());
		Channel channel = channelService.findById(form.getChannelId());
		Bot bot = new Bot(server, channel, form.getName(), form.getPattern(), form.getMaxParallelDownloads());

		user.getBots().add(bot);
		userService.saveUser(user);

		return targetBotRepository.save(bot);
	}

	public Bot findById(Long botId) {
		return targetBotRepository.findById(botId).orElse(null);
	}
}
