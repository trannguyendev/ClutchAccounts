package com.kingdomeprotocol.service;

import java.time.LocalDateTime;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.kingdomeprotocol.model.UserDetailsProc;
import com.kingdomeprotocol.model.UserModel;
import com.kingdomeprotocol.repository.UserRepository;
import com.kingdomeprotocol.utils.JwtUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepo;
	private final PasswordEncoder pswEncode;
	private final JwtUtils jwtUtil;
	private final AuthenticationManager authenManager;
	
	public UserModel signUp(String email, String psw) {
		if (userRepo.findByEmail(email).isPresent()) {
			throw new RuntimeException("Email has already existed!");
		}
		LocalDateTime created_at = LocalDateTime.now();
		UserModel userSignUp = new UserModel();
		userSignUp.setEmail(email);
		userSignUp.setUser_psw(pswEncode.encode(psw));
		userSignUp.setBalance(0);
		userSignUp.setCreated_at(created_at);
		userSignUp.setRole("USER");

		return userRepo.save(userSignUp);
	}
	
	public userCheck login(UserDetailsProc user) {
		Authentication authen = this.authenManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPsw()));

		UserDetails userDetail = (UserDetails) authen.getPrincipal();
		String jwtToken = jwtUtil.tokenGenerator(userDetail.getUsername());
		UserModel userChecking = userRepo.findByEmail(user.getEmail()).orElseThrow(() -> new RuntimeException("Wrong password or email is not existed"));
		
		return new userCheck(userChecking.getId(), jwtToken, userChecking.getRole().toLowerCase(), userChecking.getEmail(), userChecking.getBalance());
	}
	
	public void userLoggedHistory() {}
	public record userCheck(int id, String token, String role, String email, int balance) {}
}

