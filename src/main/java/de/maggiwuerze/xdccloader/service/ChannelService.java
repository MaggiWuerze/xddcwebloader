package de.maggiwuerze.xdccloader.service;

import de.maggiwuerze.xdccloader.model.entity.Channel;
import de.maggiwuerze.xdccloader.model.transport.ChannelTO;
import de.maggiwuerze.xdccloader.persistency.ChannelRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChannelService {

	private final ChannelRepository channelRepository;


	public List<Channel> list() {
		return channelRepository.findAll();
	}

	public Channel save(Channel channel) {
		return channelRepository.save(channel);
	}

	public Channel findById(Long channelId) {
		return channelRepository.findById(channelId).orElse(null);
	}

	public void delete(Long channelId) {channelRepository.deleteById(channelId);}
}
