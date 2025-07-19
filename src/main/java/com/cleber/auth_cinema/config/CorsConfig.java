package com.cleber.auth_cinema.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		// Configuração para Angular (portas 4200-4301)
		configuration.setAllowedOriginPatterns(Arrays.asList(
				"http://localhost:4200",           // Angular padrão
				"http://localhost:4201",           // Angular alternativa
				"http://localhost:4300",           // Sua porta atual
				"http://localhost:4301",           // Sua porta com proxy
				"http://127.0.0.1:4200",          // Angular local alternativo
				"http://127.0.0.1:4201",          // Angular local alternativo
				"http://127.0.0.1:4300",          // Angular local alternativo
				"http://127.0.0.1:4301",          // Angular local alternativo
				"http://0.0.0.0:4200",            // Para Docker
				"http://0.0.0.0:4201",            // Para Docker
				"http://0.0.0.0:4300",            // Para Docker
				"http://0.0.0.0:4301",            // Para Docker
				"http://host.docker.internal:4200", // Docker Desktop
				"http://host.docker.internal:4201", // Docker Desktop
				"http://host.docker.internal:4300", // Docker Desktop
				"http://host.docker.internal:4301"  // Docker Desktop
		));

		// Métodos HTTP permitidos
		configuration.setAllowedMethods(Arrays.asList(
				"GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"
		));

		// Headers permitidos (mais específico que "*" para segurança)
		configuration.setAllowedHeaders(Arrays.asList(
				"Authorization",
				"Content-Type",
				"X-Requested-With",
				"Accept",
				"Origin",
				"Access-Control-Request-Method",
				"Access-Control-Request-Headers",
				"X-Forwarded-For",
				"X-Forwarded-Proto",
				"X-Forwarded-Port"
		));

		// Headers expostos para o frontend
		configuration.setExposedHeaders(Arrays.asList(
				"Authorization",
				"Content-Type",
				"X-Total-Count"
		));

		// Permitir envio de credenciais (cookies, authorization headers)
		configuration.setAllowCredentials(true);

		// Cache do preflight request (em segundos)
		configuration.setMaxAge(3600L);

		// Aplicar configuração para todas as rotas
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}