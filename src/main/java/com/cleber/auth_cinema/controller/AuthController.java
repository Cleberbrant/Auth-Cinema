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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {
		"http://localhost:4200", "http://localhost:4201",
		"http://localhost:4300", "http://localhost:4301"
}, allowCredentials = "true")
@RequiredArgsConstructor
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	private final CustomUserDetailsService userDetailsService;
	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;

	@PostMapping("/login")
	public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request) {
		try {
			System.out.println("=== LOGIN REQUEST RECEBIDA NO CONTROLLER ===");
			System.out.println("Email: " + request.getEmail());

			// Validar entrada
			if (request.getEmail() == null || request.getPassword() == null) {
				return ResponseEntity.badRequest().build();
			}

			// Autenticar usuário
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
			);

			// Carregar detalhes do usuário
			final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

			// Buscar dados completos do usuário
			Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(request.getEmail());
			if (usuarioOpt.isEmpty()) {
				return ResponseEntity.badRequest().build();
			}

			Usuario usuario = usuarioOpt.get();

			// Gerar JWT
			final String jwt = jwtUtil.generateToken(userDetails);

			// Criar resposta
			AuthResponseDTO response = new AuthResponseDTO(jwt);

			System.out.println("=== LOGIN SUCESSO NO CONTROLLER ===");
			System.out.println("Token gerado para: " + usuario.getEmail());

			return ResponseEntity.ok(response);

		} catch (BadCredentialsException e) {
			System.out.println("=== LOGIN ERRO: Credenciais inválidas ===");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

		} catch (Exception e) {
			System.out.println("=== LOGIN ERRO GENÉRICO: " + e.getMessage() + " ===");
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO) {
		try {
			System.out.println("=== REGISTER REQUEST RECEBIDA NO CONTROLLER ===");
			System.out.println("Email: " + registerDTO.getEmail());

			// Validações básicas
			if (registerDTO.getEmail() == null || registerDTO.getEmail().trim().isEmpty()) {
				return ResponseEntity.badRequest()
						.body(Map.of("error", "Email é obrigatório"));
			}

			if (registerDTO.getPassword() == null || registerDTO.getPassword().length() < 6) {
				return ResponseEntity.badRequest()
						.body(Map.of("error", "Senha deve ter pelo menos 6 caracteres"));
			}

			if (registerDTO.getNome() == null || registerDTO.getNome().trim().isEmpty()) {
				return ResponseEntity.badRequest()
						.body(Map.of("error", "Nome é obrigatório"));
			}

			// Verificar se email já existe
			if (usuarioRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
				return ResponseEntity.status(HttpStatus.CONFLICT)
						.body(Map.of("error", "Email já cadastrado"));
			}

			// Definir role
			Role role = Role.ROLE_USER;
			if (registerDTO.getRole() != null && !registerDTO.getRole().trim().isEmpty()) {
				try {
					role = Role.valueOf(registerDTO.getRole().toUpperCase());
				} catch (IllegalArgumentException e) {
					return ResponseEntity.badRequest()
							.body(Map.of("error", "Role inválido! Use apenas: ROLE_USER ou ROLE_ADMIN"));
				}
			}

			// Criar usuário
			Usuario usuario = Usuario.builder()
					.email(registerDTO.getEmail().trim().toLowerCase())
					.password(passwordEncoder.encode(registerDTO.getPassword()))
					.nome(registerDTO.getNome().trim())
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

			Usuario usuarioSalvo = usuarioRepository.save(usuario);

			System.out.println("=== REGISTER SUCESSO NO CONTROLLER ===");
			System.out.println("Usuário criado: " + usuarioSalvo.getEmail());

			return ResponseEntity.status(HttpStatus.CREATED)
					.body(Map.of("message", "Usuário cadastrado com sucesso!"));

		} catch (Exception e) {
			System.out.println("=== REGISTER ERRO NO CONTROLLER: " + e.getMessage() + " ===");
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Erro interno do servidor"));
		}
	}

	@GetMapping("/me")
	public ResponseEntity<?> getCurrentUser(Authentication authentication) {
		try {
			if (authentication == null || !authentication.isAuthenticated()) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(Map.of("error", "Usuário não autenticado"));
			}

			String email = authentication.getName();
			Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

			if (usuarioOpt.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(Map.of("error", "Usuário não encontrado"));
			}

			Usuario usuario = usuarioOpt.get();

			return ResponseEntity.ok(Map.of(
					"id", usuario.getId(),
					"nome", usuario.getNome(),
					"email", usuario.getEmail(),
					"role", usuario.getRole().name()
			));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Erro interno do servidor"));
		}
	}
}