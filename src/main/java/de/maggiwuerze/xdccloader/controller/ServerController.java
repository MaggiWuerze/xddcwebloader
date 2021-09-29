package de.maggiwuerze.xdccloader.controller;

import de.maggiwuerze.xdccloader.model.entity.Server;
import de.maggiwuerze.xdccloader.model.forms.ServerForm;
import de.maggiwuerze.xdccloader.service.ServerService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
class ServerController {

	private final ServerService serverService;

	/**
	 * @return a list of all servers
	 */
	@GetMapping("/servers/")
	public ResponseEntity<List<Server>> getAllServers() {
		List<Server> servers = serverService.list();
		return new ResponseEntity(servers, HttpStatus.OK);
	}

	@PostMapping("/servers/")
	public ResponseEntity<?> addServer(@RequestBody ServerForm serverForm) {
		Server server = serverService.save(new Server(serverForm.getName(), serverForm.getServerUrl()));
		return new ResponseEntity("Download added succcessfully. id=[" + server.getId() + "]", HttpStatus.OK);
	}
}