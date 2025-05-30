package com.adacho.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class ShareTokenProvider {
	
	@Value("${springboot.jwt.secret}")
	private String SECRET_KEY; 

	public String createShareToken(String userId) {
	    long now = System.currentTimeMillis();
	    long exp = now + 1000 * 60 * 60 * 24; // 24시간 유효

	    return Jwts.builder()
	            .setSubject(userId)
	            .setIssuedAt(new Date(now))
	            .setExpiration(new Date(exp))
	            .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
	            .compact();
	}
}
