package com.hms.user.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
	private static final Long JWT_TOKEN_VALIDITY=(long)1000*60*60*24;
	private static final String SECRET_KEY="uhtf1hPnXtYspX9lS61C25mXAJD/p6f64mwyKUG369DIdtEI/im36jpPlnM4n3r0MtW3uejnIiIAFYrb/kYqMA==";
	
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims=new HashMap<>();
		CustomUserDetails user=(CustomUserDetails) userDetails;
		claims.put("id", user.getId());
		claims.put("email",user.getEmail());
		claims.put("role", user.getRole());
		claims.put("name", user.getName());
		claims.put("profileId", user.getProfileId());
		return createToken(claims,userDetails.getUsername());
	}
	private String createToken(Map<String,Object> claims,String subject) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+JWT_TOKEN_VALIDITY))
				.signWith(SignatureAlgorithm.HS512,SECRET_KEY)
				.compact();
	}
}
