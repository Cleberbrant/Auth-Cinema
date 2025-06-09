package com.cleber.auth_cinema.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request,
					   HttpServletResponse response,
					   AccessDeniedException accessDeniedException) throws IOException {

		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType("application/json");

		Map<String, Object> errorDetails = new HashMap<>();
		errorDetails.put("statusCode", HttpServletResponse.SC_FORBIDDEN);
		errorDetails.put("message", "Acesso negado: você não tem permissão para acessar este recurso.");
		errorDetails.put("timestamp", LocalDateTime.now().toString());
		errorDetails.put("path", request.getRequestURI());

		ObjectMapper mapper = new ObjectMapper();
		response.getWriter().write(mapper.writeValueAsString(errorDetails));
	}
}