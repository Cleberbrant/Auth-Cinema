package com.cleber.auth_cinema.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CustomAccessDeniedHandlerTest {

	@Test
	void handle_deveRetornarForbiddenJson() throws Exception {
		CustomAccessDeniedHandler handler = new CustomAccessDeniedHandler();
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		AccessDeniedException ex = new AccessDeniedException("Acesso negado");

		when(request.getRequestURI()).thenReturn("/api/teste");
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);

		handler.handle(request, response, ex);

		verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
		verify(response).setContentType("application/json");
		pw.flush();
		assertTrue(sw.toString().contains("Acesso negado"));
		assertTrue(sw.toString().contains("/api/teste"));
	}
}