package com.cleber.auth_cinema.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

	private JwtUtil jwtUtil;

	@BeforeEach
	void setUp() {
		jwtUtil = new JwtUtil();
		ReflectionTestUtils.setField(jwtUtil, "SECRET_KEY", "test-secret");
	}

	@Test
	void generateAndValidateToken() {
		User user = new User("user", "pass", Collections.singleton(() -> "ROLE_USER"));
		String token = jwtUtil.generateToken(user);

		assertNotNull(token);
		assertEquals("user", jwtUtil.extractUsername(token));
		assertTrue(jwtUtil.validateToken(token, user));
	}

	@Test
	void validateToken_invalidUser() {
		User user = new User("user", "pass", Collections.singleton(() -> "ROLE_USER"));
		String token = jwtUtil.generateToken(user);

		User otherUser = new User("other", "pass", Collections.singleton(() -> "ROLE_USER"));
		assertFalse(jwtUtil.validateToken(token, otherUser));
	}
}