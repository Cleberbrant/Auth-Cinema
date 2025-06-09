package com.cleber.auth_cinema.config;

import com.cleber.auth_cinema.security.CustomAccessDeniedHandler;
import com.cleber.auth_cinema.service.impl.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomUserDetailsService userDetailsService;
	private final PasswordEncoder passwordEncoder;
	private final CustomAccessDeniedHandler customAccessDeniedHandler;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.exceptionHandling(ex -> ex
						.authenticationEntryPoint((request, response, authException) -> {
							response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
							response.setContentType("application/json");
							response.getWriter().write("{\"error\": \"Unauthorized - Token ausente ou inválido\"}");
						})
						.accessDeniedHandler(customAccessDeniedHandler)
				)
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(
								"/auth/login",
								"/auth/register",
								"/swagger-ui/**",
								"/v3/api-docs/**",
								"/error"
						).permitAll()
						.anyRequest().authenticated()
				)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.authenticationProvider(authenticationProvider());

		return http.build();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder);
		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}