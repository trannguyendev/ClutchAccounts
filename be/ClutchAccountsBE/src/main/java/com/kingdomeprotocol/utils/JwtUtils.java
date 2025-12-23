package com.kingdomeprotocol.utils;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
	
	private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	private final int exprieTime = 1000*60*60*168; 
	public String tokenGenerator(String email) {
		return Jwts.builder().setSubject(email).issuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + exprieTime)).signWith(secretKey).compact();
	}
	

	public String extractEmail(String token) {
		return extractAll(token).getSubject();
	}
	
	
	boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token);
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	
	//Decode method
	public Claims extractAll(String token) {
		return Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
	}
}
