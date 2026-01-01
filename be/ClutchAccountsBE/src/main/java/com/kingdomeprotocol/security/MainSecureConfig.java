package com.kingdomeprotocol.security;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.kingdomeprotocol.model.UserModel;
import com.kingdomeprotocol.repository.UserRepository;
import com.kingdomeprotocol.utils.JwtAuthenticationFilter;

@Configuration
@EnableMethodSecurity
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
				return new User(user.getEmail(), user.getUser_psw(), authorities);
	};
}


@Bean
public AuthenticationManager authenticationManager(AuthenticationConfiguration authenConfig) throws Exception {
	return authenConfig.getAuthenticationManager();
}

@Bean
public SecurityFilterChain configSecure(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
	http.csrf(config -> config.disable());
	http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
	http.authorizeHttpRequests(config -> {
		config.requestMatchers("/api/test").permitAll(); //This config security below  only for testing "/test" route XD
		config.requestMatchers("/api/auth/**").permitAll();
		config.requestMatchers("/api/user/**").authenticated();
		config.requestMatchers("/api/payment/**").authenticated();
		config.requestMatchers("/api/accounts/**").authenticated();
	});
	http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	return http.build();
}
}
