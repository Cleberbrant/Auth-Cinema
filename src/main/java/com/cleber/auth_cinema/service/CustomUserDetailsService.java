package com.cleber.auth_cinema.service.impl;

import com.cleber.auth_cinema.model.Usuario;
import com.cleber.auth_cinema.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
		// Log para debug
		System.out.println("Usuário autenticado: " + usuario.getEmail() + " | Role: " + usuario.getRole());

		return User.builder()
				.username(usuario.getEmail())
				.password(usuario.getPassword())
				.authorities(Collections.singletonList(new SimpleGrantedAuthority(usuario.getRole().name())))
				.accountExpired(false)
				.accountLocked(false)
				.credentialsExpired(false)
				.disabled(false)
				.build();
	}
}