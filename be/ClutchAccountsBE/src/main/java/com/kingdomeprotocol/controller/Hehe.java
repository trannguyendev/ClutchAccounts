package com.kingdomeprotocol.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hehe {
	@GetMapping("/test-api")
	public String testEndpoint() {
		return "Hello World from test";
	}
}
