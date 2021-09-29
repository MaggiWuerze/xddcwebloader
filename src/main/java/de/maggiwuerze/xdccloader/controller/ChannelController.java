package de.maggiwuerze.xdccloader.controller;

import de.maggiwuerze.xdccloader.model.entity.Channel;
import de.maggiwuerze.xdccloader.model.forms.ChannelForm;
import de.maggiwuerze.xdccloader.service.ChannelService;
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
class ChannelController {

	private final ChannelService channelService;

	/**
	 * @return a list of all channels
	 */
	@GetMapping("/channels/")
	public ResponseEntity<List<Channel>> getAllChannels() {
		List<Channel> channels = channelService.list();
		return new ResponseEntity(channels, HttpStatus.OK);
	}

	@PostMapping("/channels/")
	public ResponseEntity<?> addChannel(@RequestBody ChannelForm channelForm) {
		Channel channel = channelService.save(new Channel(channelForm.getName()));
		return new ResponseEntity("Download added succcessfully. id=[" + channel.getId() + "]", HttpStatus.OK);
	}

}