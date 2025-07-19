package com.cleber.auth_cinema.config;

import com.cleber.auth_cinema.security.CustomAccessDeniedHandler;
import com.cleber.auth_cinema.security.JwtAuthenticationEntryPoint;
import com.cleber.auth_cinema.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final CustomAccessDeniedHandler customAccessDeniedHandler;
	private final CorsConfigurationSource corsConfigurationSource;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
				// CORS
				.cors(cors -> cors.configurationSource(corsConfigurationSource))

				// CSRF
				.csrf(csrf -> csrf.disable())

				// Session Management
				.sessionManagement(session ->
						session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				// Authorization Rules (ORDEM IMPORTA!)
				.authorizeHttpRequests(auth -> auth
						// ===== ROTAS COMPLETAMENTE PÚBLICAS =====
						.requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
						.requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // CORS preflight

						// Rotas de desenvolvimento e monitoramento
						.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
						.requestMatchers("/h2-console/**").permitAll()
						.requestMatchers("/actuator/**").permitAll()

						// ===== ROTAS PROTEGIDAS POR ROLE =====
						.requestMatchers("/auth/admin/**").hasRole("ADMIN")
						.requestMatchers("/auth/usuarios/**").hasRole("ADMIN")  // ← ADICIONADO

						// ===== ROTAS QUE PRECISAM DE AUTENTICAÇÃO =====
						.requestMatchers("/auth/me", "/auth/profile").authenticated()

						// ===== QUALQUER OUTRA ROTA PRECISA DE AUTENTICAÇÃO =====
						.anyRequest().authenticated()
				)

				// Exception Handling
				.exceptionHandling(ex -> ex
						.authenticationEntryPoint(jwtAuthenticationEntryPoint)
						.accessDeniedHandler(customAccessDeniedHandler)
				)

				// Headers para H2 Console
				.headers(headers -> headers
						.frameOptions(frameOptions -> frameOptions.sameOrigin())
				)

				// JWT Filter
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

				.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}