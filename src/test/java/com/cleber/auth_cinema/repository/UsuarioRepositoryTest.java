// src/test/java/com/cleber/auth_cinema/repository/UsuarioRepository.java
package com.cleber.auth_cinema.repository;

import com.cleber.auth_cinema.model.Usuario;
import com.cleber.auth_cinema.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UsuarioRepositoryTest {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Test
	void findByEmail_deveRetornarUsuario() {
		Usuario usuario = Usuario.builder()
				.email("email@teste.com")
				.password("senha")
				.nome("Teste")
				.role(Role.ROLE_USER)
				.build();
		usuarioRepository.save(usuario);

		Optional<Usuario> resultado = usuarioRepository.findByEmail("email@teste.com");

		assertTrue(resultado.isPresent());
		assertEquals("email@teste.com", resultado.get().getEmail());
	}

	@Test
	void findByEmail_deveRetornarVazio() {
		Optional<Usuario> resultado = usuarioRepository.findByEmail("naoexiste@teste.com");

		assertFalse(resultado.isPresent());
	}
}