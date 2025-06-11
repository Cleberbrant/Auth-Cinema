package com.cleber.auth_cinema.service.impl;

import com.cleber.auth_cinema.enums.Role;
import com.cleber.auth_cinema.model.Usuario;
import com.cleber.auth_cinema.repository.UsuarioRepository;
import com.cleber.auth_cinema.service.impl.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

	@Mock
	private UsuarioRepository usuarioRepository;

	@InjectMocks
	private CustomUserDetailsService customUserDetailsService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void loadUserByUsername_usuarioExistente_retornaUserDetails() {
		Usuario usuario = Usuario.builder()
				.id(1L)
				.email("teste@email.com")
				.password("senha123")
				.role(Role.ROLE_USER)
				.build();

		when(usuarioRepository.findByEmail("teste@email.com")).thenReturn(Optional.of(usuario));

		UserDetails userDetails = customUserDetailsService.loadUserByUsername("teste@email.com");

		assertEquals("teste@email.com", userDetails.getUsername());
		assertEquals("senha123", userDetails.getPassword());
		assertTrue(userDetails.getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
	}

	@Test
	void loadUserByUsername_usuarioNaoExistente_lancaExcecao() {
		when(usuarioRepository.findByEmail("naoexiste@email.com")).thenReturn(Optional.empty());

		assertThrows(UsernameNotFoundException.class, () ->
				customUserDetailsService.loadUserByUsername("naoexiste@email.com"));
	}
}