package de.maggiwuerze.xdccloader.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@Slf4j
@RequiredArgsConstructor
class MainController {

	@GetMapping("/")
	public String index() {
		return "index";
	}
}