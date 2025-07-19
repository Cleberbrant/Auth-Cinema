package com.cleber.auth_cinema.security;

import com.cleber.auth_cinema.service.impl.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final CustomUserDetailsService userDetailsService;

	// Lista de rotas que NÃO precisam de autenticação (incluindo /api/)
	private static final List<String> PUBLIC_PATHS = Arrays.asList(
			"/auth/login",
			"/auth/register",
			"/api/auth/login",          // ← ADICIONADO
			"/api/auth/register",       // ← ADICIONADO
			"/api/usuarios/register",
			"/api/usuarios/login",
			"/usuarios/register",
			"/usuarios/login",
			"/swagger-ui",
			"/v3/api-docs",
			"/h2-console",
			"/actuator/health"
	);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {

		String path = request.getRequestURI();
		String method = request.getMethod();

		// ===== PULAR FILTRO PARA ROTAS PÚBLICAS =====
		if (isPublicPath(path, method)) {
			System.out.println("=== ROTA PÚBLICA DETECTADA: " + method + " " + path + " ===");
			filterChain.doFilter(request, response);
			return;
		}

		final String authHeader = request.getHeader("Authorization");

		// Se não tem header Authorization, deixa passar
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			System.out.println("=== SEM TOKEN (ROTA PROTEGIDA): " + method + " " + path + " ===");
			filterChain.doFilter(request, response);
			return;
		}

		try {
			final String jwt = authHeader.substring(7);
			final String userEmail = jwtUtil.extractUsername(jwt);

			System.out.println("=== PROCESSANDO TOKEN PARA: " + userEmail + " ===");

			if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

				if (jwtUtil.validateToken(jwt, userDetails)) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);

					System.out.println("=== TOKEN VÁLIDO PARA: " + userEmail + " ===");
				} else {
					System.out.println("=== TOKEN INVÁLIDO PARA: " + userEmail + " ===");
				}
			}
		} catch (Exception e) {
			System.out.println("=== ERRO NO JWT FILTER: " + e.getMessage() + " ===");
			logger.error("Erro ao processar token JWT: {}");
		}

		filterChain.doFilter(request, response);
	}

	/**
	 * Verifica se o path é público e não precisa de autenticação
	 */
	private boolean isPublicPath(String path, String method) {
		// Verificar rotas exatas para POST
		if ("POST".equals(method)) {
			for (String publicPath : PUBLIC_PATHS) {
				if (path.equals(publicPath)) {
					return true;
				}
			}
		}

		// Verificar prefixos para qualquer método
		if (path.startsWith("/swagger-ui/") ||
				path.startsWith("/v3/api-docs/") ||
				path.startsWith("/h2-console/") ||
				path.equals("/actuator/health")) {
			return true;
		}

		// CORS preflight requests
		if ("OPTIONS".equals(method)) {
			return true;
		}

		return false;
	}
}