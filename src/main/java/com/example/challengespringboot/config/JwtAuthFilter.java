package com.example.challengespringboot.config;

import com.example.challengespringboot.entities.UserDetailsImpl;
import com.example.challengespringboot.services.JwtService;
import com.example.challengespringboot.services.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

@Component
public class JwtAuthFilter extends OncePerRequestFilter implements Filter {
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
		
			final String authorization = request.getHeader("Authorization");
			if (authorization != null && authorization.startsWith("Bearer ")) {
		
				final String token = authorization.substring(7);
				final Claims claims = jwtService.getClaims(token);
				if (claims.getExpiration().after(new Date())) {

					final String username = claims.getSubject();
					final UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(username); // raja3ha UserDetailImp w chouf aleh
					
					final UsernamePasswordAuthenticationToken authToken =
							new UsernamePasswordAuthenticationToken(
									userDetails, null, userDetails.getAuthorities());
				
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
				
			}

		}
		
		filterChain.doFilter(request, response);
	}


}
