package com.kingdomeprotocol.utils;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{
private final JwtUtils jwtUtils;
private final UserDetailsService userDetailServ;

@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String header = request.getHeader("Authorization");
		if (header != null && header.trim().length() > 0 && header.startsWith("Bearer ")) {
			String token = header.substring(7);
			if (jwtUtils.validateToken(token)) {
				String email = jwtUtils.extractEmail(token);
				if (SecurityContextHolder.getContext().getAuthentication() == null) {
					UserDetails userDetail = userDetailServ.loadUserByUsername(email);
					var authToken = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities()); //Create + add
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));									//auth token
					SecurityContextHolder.getContext().setAuthentication(authToken);																//when token is valid
				}
			}
		}
		filterChain.doFilter(request, response);
	}
}
