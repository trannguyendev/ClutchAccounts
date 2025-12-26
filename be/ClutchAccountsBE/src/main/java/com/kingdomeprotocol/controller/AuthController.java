package com.kingdomeprotocol.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kingdomeprotocol.model.AuditLogModel;
import com.kingdomeprotocol.model.DTOAuditLogRequest;
import com.kingdomeprotocol.model.ForgotPassModel;
import com.kingdomeprotocol.model.UserDetailsProc;
import com.kingdomeprotocol.model.UserModel;
import com.kingdomeprotocol.repository.AuditLogRepository;
import com.kingdomeprotocol.service.UserService;
import com.kingdomeprotocol.service.UserService.userCheck;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
	private final AuditLogRepository auditRepo;
	private final UserService userService;

	@PostMapping("/auth/check")
	public ResponseEntity<?> checkLogin(@Valid @RequestBody UserDetailsProc loginDetails){
		try {
			userCheck user = userService.login(loginDetails);
			return ResponseEntity.ok(user);
		}
		catch(UsernameNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
		}
		catch(BadCredentialsException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Wrong password or email"));
		}
		catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "System is not online"));
		}
}
	@PostMapping("/auth/register")
	public ResponseEntity<?> signUp(@Valid @RequestBody UserDetailsProc signUpDetails){
		try {
			UserModel userSignUp  = userService.signUp(signUpDetails.getEmail(), signUpDetails.getPsw());
			return ResponseEntity.ok(userSignUp);
		}
		catch(RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Email already existed, pls contact to support team"));
		}
		catch(Exception e ) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
		}
	}
	@GetMapping("/auth/me")
	public ResponseEntity<?> getCurrentUser(Authentication authen){
		String email = authen.getName();
		try {
			UserModel fetchUser = userService.loadUserByEmail(email).orElseThrow();
			return ResponseEntity.ok(Map.of("id", fetchUser.getId(), "email", fetchUser.getEmail(), "role", fetchUser.getRole().toLowerCase(), "balance", fetchUser.getBalance()));
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
		}
	}
	@PostMapping("/auth/tracking")
	public ResponseEntity<?> trackingUser(@RequestBody DTOAuditLogRequest auditReq, Authentication authen){
		try {
			String email = authen.getName();
			UserModel user = userService.loadUserByEmail(email).orElseThrow();

			AuditLogModel auditLog = new AuditLogModel();
			auditLog.setLogged_device(auditReq.getLogged_device());
			auditLog.setLogged_ip(auditReq.getLogged_ip());
			auditLog.setLogged_time(auditReq.getLogged_time());
			auditLog.setAuditUser(user);

			auditRepo.save(auditLog);
			return ResponseEntity.ok("Logged success");
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
		}
	}
//Forogt psw route
	@PostMapping("/auth/sendOTP/{email}")
	public ResponseEntity<?> sendOTP(@PathVariable String email){
		try {
			userService.sentOTP(email);
			return ResponseEntity.ok(Map.of("message", "Your OTP code has been sent to your email"));
		}
		catch(RuntimeException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of("error", e.getMessage()));
		}
	}

	@PostMapping("/auth/verifyOTP")
	public ResponseEntity<?> verifyOTP(@RequestBody ForgotPassModel forgotModel){
		try {
			userService.verifyOTP(forgotModel);
			return ResponseEntity.ok("Password changed successfully");
		}
		catch(RuntimeException e ) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Wrong OTP"));
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of("error", e.getMessage()));
		}
	}
}

