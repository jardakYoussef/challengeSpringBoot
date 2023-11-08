package com.example.challengespringboot.controllers;

import com.example.challengespringboot.dtos.AuthRequestDTO;
import com.example.challengespringboot.dtos.AuthResponseDTO;
import com.example.challengespringboot.dtos.RegisterRequestDTO;
import com.example.challengespringboot.services.AuthService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Transactional
public class AuthController {
	
	@Autowired
	private AuthService authService;

	@PostMapping("/registration")
	public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterRequestDTO dto) throws Exception {
		return ResponseEntity.ok(authService.register(dto));
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponseDTO> authenticate(@RequestBody AuthRequestDTO dto) {
		return ResponseEntity.ok(authService.authenticate(dto));
	}
}
