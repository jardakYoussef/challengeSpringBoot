package com.example.challengespringboot.services;

import com.example.challengespringboot.dtos.AuthRequestDTO;
import com.example.challengespringboot.dtos.AuthResponseDTO;
import com.example.challengespringboot.dtos.RegisterRequestDTO;
import com.example.challengespringboot.entities.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	@Autowired
	private UsersService usersService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	public AuthResponseDTO register(RegisterRequestDTO dto) throws Exception {
		
		Users user = new Users();
		user.setName(dto.getName());
		user.setEmail(dto.getEmail());
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		
		user = usersService.create(user);
		
		return new AuthResponseDTO(jwtService.generateToken(user.getEmail()));
	}
	
	public AuthResponseDTO authenticate(AuthRequestDTO dto) {
		
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						dto.getEmail(), 
						dto.getPassword()));
		
		final Users user = usersService.findByEmail(dto.getEmail());
		return new AuthResponseDTO(jwtService.generateToken(user.getEmail()));
	}
	
	
	
}
