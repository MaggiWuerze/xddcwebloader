package de.maggiwuerze.xdccloader.service;

import de.maggiwuerze.xdccloader.model.entity.Server;
import de.maggiwuerze.xdccloader.persistency.ServerRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ServerService {

	private final ServerRepository serverRepository;

	public List<Server> list() {
		return serverRepository.findAll();
	}

	public Server save(Server server) {
		return serverRepository.save(server);
	}

	public Server findById(Long channelId) {
		return serverRepository.findById(channelId).orElse(null);
	}
}
