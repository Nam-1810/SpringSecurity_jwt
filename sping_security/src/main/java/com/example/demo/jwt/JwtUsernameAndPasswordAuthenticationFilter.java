package com.example.demo.jwt;

import java.io.IOException;
import java.security.Key;
import java.time.LocalDate;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;

	public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			UsernameAndPasswordAuthenticationRequest authenticationRequest = new ObjectMapper()
					.readValue(request.getInputStream(), UsernameAndPasswordAuthenticationRequest.class);

			Authentication authentication = new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(),
					authenticationRequest.getPassword());
			Authentication authenticate =  authenticationManager.authenticate(authentication);
			return authenticate;
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
	
		String token = Jwts.builder()
		   .setSubject(authResult.getName())
		   .claim("authorities", authResult.getAuthorities())
		   .setIssuedAt(new Date())
		   .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(2)))
		   .signWith(SignatureAlgorithm.HS256, "alalalallalalalalallalalalalalla")
		   .compact();
		response.addHeader("Authjwt", "Bearer " + token);
	}
}
