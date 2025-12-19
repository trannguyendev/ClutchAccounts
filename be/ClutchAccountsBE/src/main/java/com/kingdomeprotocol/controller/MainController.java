package com.kingdomeprotocol.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

//	This route for checking password
	@PostMapping("/check")
	public String testMapping() {
		return "It's worked LoL";
	}
}
