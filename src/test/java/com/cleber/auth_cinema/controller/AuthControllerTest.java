package com.cleber.auth_cinema.controller;

import com.cleber.auth_cinema.dto.AuthRequestDTO;
import com.cleber.auth_cinema.dto.AuthResponseDTO;
import com.cleber.auth_cinema.dto.RegisterDTO;
import com.cleber.auth_cinema.model.Usuario;
import com.cleber.auth_cinema.repository.UsuarioRepository;
import com.cleber.auth_cinema.security.JwtUtil;
import com.cleber.auth_cinema.service.impl.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

	@Mock
	private AuthenticationManager authenticationManager;
	@Mock
	private JwtUtil jwtUtil;
	@Mock
	private CustomUserDetailsService userDetailsService;
	@Mock
	private UsuarioRepository usuarioRepository;
	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private AuthController authController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void login_deveRetornarToken() {
		AuthRequestDTO request = new AuthRequestDTO();
		request.setEmail("email@teste.com");
		request.setPassword("senha");

		UserDetails userDetails = mock(UserDetails.class);
		when(userDetailsService.loadUserByUsername("email@teste.com")).thenReturn(userDetails);
		when(jwtUtil.generateToken(userDetails)).thenReturn("jwt-token");

		ResponseEntity<AuthResponseDTO> response = authController.login(request);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("jwt-token", response.getBody().getJwt());
		verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
	}

	@Test
	void register_emailExistente_deveRetornarConflict() {
		RegisterDTO dto = new RegisterDTO();
		dto.setEmail("email@teste.com");
		when(usuarioRepository.findByEmail("email@teste.com")).thenReturn(Optional.of(new Usuario()));

		ResponseEntity<?> response = authController.register(dto);

		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		assertEquals("Email já cadastrado", response.getBody());
	}

	@Test
	void register_roleInvalida_deveRetornarBadRequest() {
		RegisterDTO dto = new RegisterDTO();
		dto.setEmail("novo@teste.com");
		dto.setRole("ROLE_INVALIDA");
		when(usuarioRepository.findByEmail("novo@teste.com")).thenReturn(Optional.empty());

		ResponseEntity<?> response = authController.register(dto);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().toString().contains("Role inválido"));
	}

	@Test
	void register_sucesso_deveRetornarCreated() {
		RegisterDTO dto = new RegisterDTO();
		dto.setEmail("novo@teste.com");
		dto.setPassword("senha");
		dto.setNome("Novo");
		dto.setRole("ROLE_USER");
		when(usuarioRepository.findByEmail("novo@teste.com")).thenReturn(Optional.empty());
		when(passwordEncoder.encode("senha")).thenReturn("senhaCriptografada");

		ResponseEntity<?> response = authController.register(dto);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals("Usuário cadastrado com sucesso!", response.getBody());
		verify(usuarioRepository).save(any(Usuario.class));
	}
}