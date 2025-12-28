package com.kingdomeprotocol.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kingdomeprotocol.model.DTOChangePassLoggedIn;
import com.kingdomeprotocol.model.UserModel;
import com.kingdomeprotocol.repository.AuditLogRepository;
import com.kingdomeprotocol.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
	private final AuditLogRepository auditRepo;
	private final UserService userServ;

	@GetMapping("/login-history")
	public ResponseEntity<?> displayHistory(Authentication authen){
		String email = authen.getName();
		UserModel user = userServ.loadUserByEmail(email).orElseThrow();
		try {
			return ResponseEntity.ok(auditRepo.loadAuditById(user.getId()));
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
		}
	}
	@PostMapping("/change-psw")
	public ResponseEntity<?> changePassWhenLoggedIn(@Valid @RequestBody DTOChangePassLoggedIn userData){
		try {
			userServ.changePassLoggedIn(userData);
			return ResponseEntity.ok(Map.of("message", "Changed password successfully"));
		}
		catch(BadCredentialsException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Old password is wrong"));
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
		}
	}
}
