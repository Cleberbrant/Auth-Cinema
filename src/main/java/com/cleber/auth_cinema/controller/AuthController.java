package com.cleber.auth_cinema.controller;

import com.cleber.auth_cinema.dto.AuthRequestDTO;
import com.cleber.auth_cinema.dto.AuthResponseDTO;
import com.cleber.auth_cinema.dto.RegisterDTO;
import com.cleber.auth_cinema.enums.Role;
import com.cleber.auth_cinema.model.Localidade;
import com.cleber.auth_cinema.model.Usuario;
import com.cleber.auth_cinema.repository.UsuarioRepository;
import com.cleber.auth_cinema.security.JwtUtil;
import com.cleber.auth_cinema.service.impl.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	private final CustomUserDetailsService userDetailsService;
	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;

	@PostMapping("/login")
	public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
		final String jwt = jwtUtil.generateToken(userDetails);
		return ResponseEntity.ok(new AuthResponseDTO(jwt));
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO) {
		if (usuarioRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Email já cadastrado");
		}
		Role role = Role.ROLE_USER;
		if (registerDTO.getRole() != null) {
			try {
				role = Role.valueOf(registerDTO.getRole());
			} catch (IllegalArgumentException e) {
				return ResponseEntity.badRequest().body("Role inválido! Use apenas: ROLE_USER ou ROLE_ADMIN.");
			}
		}
		Usuario usuario = Usuario.builder()
				.email(registerDTO.getEmail())
				.password(passwordEncoder.encode(registerDTO.getPassword()))
				.nome(registerDTO.getNome())
				.dataNascimento(registerDTO.getDataNascimento())
				.cpf(registerDTO.getCpf())
				.role(role)
				.localidade(registerDTO.getLocalidade() != null
						? Localidade.builder()
						.endereco(registerDTO.getLocalidade().getEndereco())
						.cep(registerDTO.getLocalidade().getCep())
						.referencia(registerDTO.getLocalidade().getReferencia())
						.build()
						: null
				)
				.build();
		usuarioRepository.save(usuario);
		return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso!");
	}
}