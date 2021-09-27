package de.maggiwuerze.xdccloader.service;

import de.maggiwuerze.xdccloader.model.entity.Bot;
import de.maggiwuerze.xdccloader.model.entity.Channel;
import de.maggiwuerze.xdccloader.persistency.ChannelRepository;
import de.maggiwuerze.xdccloader.persistency.TargetBotRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BotService {

	private final TargetBotRepository targetBotRepository;

	public List<Bot> list() {
		return targetBotRepository.findAll();
	}

	public Bot save(Bot channel) {
		return targetBotRepository.save(channel);
	}

	public Bot findById(Long channelId) {
		return targetBotRepository.findById(channelId).orElse(null);
	}
}
