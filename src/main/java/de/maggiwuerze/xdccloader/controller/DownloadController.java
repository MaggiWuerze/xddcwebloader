package de.maggiwuerze.xdccloader.controller;

import de.maggiwuerze.xdccloader.events.SocketEvents;
import de.maggiwuerze.xdccloader.model.download.Download;
import de.maggiwuerze.xdccloader.model.download.DownloadState;
import de.maggiwuerze.xdccloader.model.entity.Bot;
import de.maggiwuerze.xdccloader.model.entity.User;
import de.maggiwuerze.xdccloader.model.entity.UserSettings;
import de.maggiwuerze.xdccloader.model.forms.DownloadForm;
import de.maggiwuerze.xdccloader.model.forms.UserForm;
import de.maggiwuerze.xdccloader.model.transport.DownloadTO;
import de.maggiwuerze.xdccloader.model.transport.UserTO;
import de.maggiwuerze.xdccloader.security.UserRole;
import de.maggiwuerze.xdccloader.service.BotService;
import de.maggiwuerze.xdccloader.service.DownloadService;
import de.maggiwuerze.xdccloader.service.EventService;
import de.maggiwuerze.xdccloader.service.UserService;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
@Slf4j
@RequiredArgsConstructor
class DownloadController{

	private final DownloadService downloadService;
	private final BotService botService;
	private final EventService eventService;

	/**
	 * @return a list of all downloads
	 */
	@GetMapping("/downloads/")
	public ResponseEntity<List<Object>> getAllDownloads() {
		return new ResponseEntity(DownloadTO.getListOfTOs(downloadService.findAllByOrderByProgressDesc()), HttpStatus.OK);
	}

	/**
	 * @return a list of downloads. if active, then it return all that are still working. if not it returns all that have stopped, this includes errors
	 */
	@GetMapping("/downloads/active/")
	public ResponseEntity<List<Object>> getActiveDownloads(boolean active) {
		List<DownloadState> states;
		if (!active) {

			states = Arrays.asList(DownloadState.UNKNOWN, DownloadState.DONE);
		} else {

			states = Arrays.asList(DownloadState.PREPARING,
				DownloadState.PREPARED,
				DownloadState.READY,
				DownloadState.CONNECTING,
				DownloadState.TRANSMITTING,
				DownloadState.FINALIZING
			);
		}

		return new ResponseEntity(DownloadTO.getListOfTOs(downloadService.findAllByStatusInOrderByProgress(states)), HttpStatus.OK);
	}

	@GetMapping("/downloads/failed")
	public ResponseEntity<List<Object>> getFailedDownloads() {
		List<DownloadTO> failedDownloads = DownloadTO.getListOfTOs(downloadService.findAllByStatusOrderByProgressDesc(DownloadState.ERROR));

		return new ResponseEntity(failedDownloads, HttpStatus.OK);
	}

	@DeleteMapping("/downloads/")
	public ResponseEntity<?> removeDownloads(Long downloadId) {
		Download download = downloadService.getById(downloadId);

		if (download != null) {

			download.setStatus(DownloadState.STOPPED);
			download.getProgressWatcher().cancel(true);
			eventService.publishEvent(SocketEvents.DELETED_DOWNLOAD, download);

			return new ResponseEntity("Download marked for deletion", HttpStatus.OK);
		}

		return new ResponseEntity("Illegal Arguments in Request", HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/downloads/")
	public ResponseEntity<?> addDownload(@RequestBody DownloadForm downloadForm) {
		Bot bot = botService.findById(downloadForm.getTargetBotId());

		if (bot == null) {
			return new ResponseEntity("Illegal Arguments in Request", HttpStatus.BAD_REQUEST);
		}

		String fileRefId = downloadForm.getFileRefId();
		if (fileRefId.contains(",")) {

			for (String id : fileRefId.split(",")) {

				Download download = new Download(bot, id);
				downloadService.addDownloadToBotQueue(download);
				eventService.publishEvent(SocketEvents.NEW_DOWNLOAD, download);
			}
		} else {
			Download download = new Download(bot, fileRefId);
			downloadService.addDownloadToBotQueue(download);
			eventService.publishEvent(SocketEvents.NEW_DOWNLOAD, download);
		}

		return new ResponseEntity("Download(s) added succcessfully.", HttpStatus.OK);
	}
}