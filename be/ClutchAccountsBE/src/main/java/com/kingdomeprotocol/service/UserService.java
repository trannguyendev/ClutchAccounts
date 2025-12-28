package com.kingdomeprotocol.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.kingdomeprotocol.model.DTOChangePassLoggedIn;
import com.kingdomeprotocol.model.ForgotPassModel;
import com.kingdomeprotocol.model.UserDetailsProc;
import com.kingdomeprotocol.model.UserModel;
import com.kingdomeprotocol.repository.UserRepository;
import com.kingdomeprotocol.utils.JwtUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepo;
	private final PasswordEncoder pswEncode;
	private final JwtUtils jwtUtil;
	private final AuthenticationManager authenManager;
	private final JavaMailSender javaMailSender;
	private final RedisTemplate<String, String> redisTemp;

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

	public Optional<UserModel> loadUserByEmail(String email) {
		return userRepo.findByEmail(email);
	}
	public void sentOTP(String email) {
		UserModel user = loadUserByEmail(email).orElseThrow(() -> new RuntimeException("Not found user: "+email));
		String OTP = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999));
		redisTemp.opsForValue().set("OTP:"+user.getEmail(), OTP, 15, TimeUnit.MINUTES);

		//Send to user using SimpleMailSender
		SimpleMailMessage sendTool = new SimpleMailMessage();
		sendTool.setFrom("clutch-accounts@support.com");
		sendTool.setTo(user.getEmail());
		sendTool.setSubject("OTP verify change password");
		sendTool.setText("Your OTP code is: "+OTP+" | "+" This code will expire in 15 mins | DON'T SHARE WITH ANYONE");
		javaMailSender.send(sendTool);
	}

	public void verifyOTP(ForgotPassModel forgot) {
		String email = forgot.getEmail();
		String key = "OTP:"+email;
		String secretOTP = redisTemp.opsForValue().get(key);

		if (secretOTP == null) {
			throw new RuntimeException("OTP is not exist or expired");
		}
		if (secretOTP.equals(forgot.getOtp())) {
			UserModel user = loadUserByEmail(email).orElseThrow(() -> new RuntimeException("Not found user"));
			user.setUser_psw(pswEncode.encode(forgot.getNewPass()));
			userRepo.save(user);
			redisTemp.delete(secretOTP);
		}
		else {
			throw new RuntimeException("Wrong OTP");
		}
	}

	public void changePassLoggedIn(@Valid @RequestBody DTOChangePassLoggedIn data) {
		Authentication authen = this.authenManager.authenticate(new UsernamePasswordAuthenticationToken(data.getEmail(), data.getOldPass()));
		if (authen != null) {
			UserModel user = loadUserByEmail(data.getEmail()).orElseThrow(() -> new RuntimeException("Error while getting data"));
			user.setUser_psw(pswEncode.encode(data.getNewPass()));
			userRepo.save(user);
		}
		else {
			throw new BadCredentialsException("Current password is not matched");
		}
	}
	public record userCheck(int id, String token, String role, String email, int balance) {}
}

