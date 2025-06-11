package com.cleber.auth_cinema.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.AuthenticationException;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthenticationEntryPointTest {

	@Test
	void commence_deveRetornarUnauthorizedJson() throws Exception {
		JwtAuthenticationEntryPoint entryPoint = new JwtAuthenticationEntryPoint();
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		AuthenticationException authException = mock(AuthenticationException.class);

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);

		entryPoint.commence(request, response, authException);

		verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		verify(response).setContentType("application/json");
		pw.flush();
		assertTrue(sw.toString().contains("Unauthorized"));
	}
}