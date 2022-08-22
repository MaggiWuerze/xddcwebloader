package de.maggiwuerze.xdccloader.controller;

import de.maggiwuerze.xdccloader.model.entity.Bot;
import de.maggiwuerze.xdccloader.model.entity.Channel;
import de.maggiwuerze.xdccloader.model.forms.ChannelForm;
import de.maggiwuerze.xdccloader.model.transport.ChannelTO;
import de.maggiwuerze.xdccloader.service.ChannelService;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
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
class ChannelController {

	private final ChannelService channelService;

	/**
	 * @return a list of all channels
	 */
	@GetMapping("/channels/")
	public ResponseEntity<List<ChannelTO>> getAllChannels() {
		List<ChannelTO> channels = channelService.list().stream().map(ChannelTO::new).collect(Collectors.toList());
		return new ResponseEntity<>(channels, HttpStatus.OK);
	}

	@PostMapping("/channels/")
	public ResponseEntity<?> addChannel(@RequestBody ChannelForm channelForm) {
		try {
			Channel channel = channelService.save(new Channel(channelForm.getName()));
			return new ResponseEntity<>("Download added successfully. id=[" + channel.getId() + "]", HttpStatus.OK);
		} catch (ConstraintViolationException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getConstraintViolations().stream().findFirst().get().getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * @return an example channel object to populate the attributes for channel creation popover
	 */
	@GetMapping("/channels/example")
	public ResponseEntity<List<Channel>> getExampleChannel(Principal principal) {
		return new ResponseEntity<>(List.of(new Channel()), HttpStatus.OK);
	}

	@DeleteMapping("/channels/")
	public ResponseEntity<?> delete( Long channelId) {
		try {
			channelService.delete(channelId);
			return new ResponseEntity<>("Channel deleted successfully.", HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>("Channel could not be deleted", HttpStatus.CONFLICT);
		}

	}

}