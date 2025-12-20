package com.kingdomeprotocol.security;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.kingdomeprotocol.model.UserModel;
import com.kingdomeprotocol.repository.UserRepository;

@Configuration
public class MainSecureConfig {
	//encrypt psw using Bcrypt:)
@Bean
public PasswordEncoder pswEncoder() {
	return new BCryptPasswordEncoder();
}
@Bean
public UserDetailsService userDetails(UserRepository userRepo) {
	return email -> {
		UserModel user = userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Not found user matched with "+email));
		var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+user.getRole().toUpperCase()));
				return new User(user.getEmail(), user.getRole(), authorities);
	};
}

//This config security below  only for testing "/test" route XD
@Bean
public SecurityFilterChain configSecure(HttpSecurity http) throws Exception {
	http.authorizeHttpRequests(config -> {
		config.requestMatchers("/api/test").permitAll();
		config.requestMatchers("/api/check").permitAll();
	});
	return http.build();
}
}
