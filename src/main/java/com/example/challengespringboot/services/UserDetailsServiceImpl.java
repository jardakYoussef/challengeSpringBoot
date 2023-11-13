package com.example.challengespringboot.services;

import com.example.challengespringboot.entities.UserDetailsImpl;
import com.example.challengespringboot.entities.Users;
import com.example.challengespringboot.repositories.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	private final UsersRepository userRepository;
	public UserDetailsServiceImpl(UsersRepository userRepository) {
		this.userRepository = userRepository;
	}
	@Override
	public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = userRepository.findByEmail(username).get();
		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}
		return new UserDetailsImpl(user);
	}
}
