package de.maggiwuerze.xdccloader.controller;

import de.maggiwuerze.xdccloader.model.entity.Bot;
import de.maggiwuerze.xdccloader.model.entity.Server;
import de.maggiwuerze.xdccloader.model.forms.ServerForm;
import de.maggiwuerze.xdccloader.model.transport.ChannelTO;
import de.maggiwuerze.xdccloader.model.transport.ServerTO;
import de.maggiwuerze.xdccloader.service.ServerService;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
	public ResponseEntity<List<ServerTO>> getAllServers() {
		List<ServerTO> servers = serverService.list().stream().map(ServerTO::new).collect(Collectors.toList());;
		return new ResponseEntity(servers, HttpStatus.OK);
	}

	@PostMapping("/servers/")
	public ResponseEntity<?> addServer(@RequestBody ServerForm serverForm) {
		try {
			Server server = serverService.save(new Server(serverForm.getName(), serverForm.getServerUrl()));
			return new ResponseEntity("Download added successfully. id=[" + server.getId() + "]", HttpStatus.OK);
		} catch (ConstraintViolationException e) {
			log.error(e.getMessage());
			String errormessage = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining("\n"));
			return new ResponseEntity(errormessage, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * @return an example server object to populate the attributes for server creation popover
	 */
	@GetMapping("/servers/example")
	public ResponseEntity<List<Server>> getExampleServer(Principal principal) {
		return new ResponseEntity(List.of(new Server()), HttpStatus.OK);
	}

	@DeleteMapping("/servers/")
	public ResponseEntity<?> delete( Long serverId) {
		try {
			serverService.delete(serverId);
			return new ResponseEntity("Server deleted successfully.", HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity("Server could not be deleted", HttpStatus.CONFLICT);
		}
	}
}