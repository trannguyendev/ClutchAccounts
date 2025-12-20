package com.kingdomeprotocol.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kingdomeprotocol.model.UserDetailsProc;
import com.kingdomeprotocol.model.UserModel;
import com.kingdomeprotocol.service.UserService;
import com.kingdomeprotocol.service.UserService.userCheck;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MainController {
	
	private final UserService userService;
	
	@PostMapping("/check")
	public ResponseEntity<?> checkLogin(@Valid @RequestBody UserDetailsProc loginDetails){
		try {
			userCheck userCheck = userService.login(loginDetails);
			return ResponseEntity.ok(userCheck);
		}
		catch(BadCredentialsException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Wrong password or email"));
		}
		catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "System system is not online"));
		}
}
	
//Route for testing route only (may delete after test)
	@GetMapping("/test")
	public String testRoute() {
		return "It's worked LoL";
	}
	
}

