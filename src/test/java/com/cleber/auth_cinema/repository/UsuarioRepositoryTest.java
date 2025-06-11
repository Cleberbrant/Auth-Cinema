package com.cleber.auth_cinema.repository;

import com.cleber.auth_cinema.model.Usuario;
import com.cleber.auth_cinema.model.Localidade;
import com.cleber.auth_cinema.enums.Role;
import com.cleber.auth_cinema.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UsuarioRepositoryTest {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Test
	void findByEmail_deveRetornarUsuario() {
		Localidade localidade = Localidade.builder()
				.endereco("Rua Teste, 123")
				.cep("12345678")
				.referencia("Perto da praça")
				.build();

		Usuario usuario = Usuario.builder()
				.email("email@teste.com")
				.password("senha123")
				.nome("Teste")
				.role(Role.ROLE_USER)
				.cpf("12345678901")
				.dataNascimento(LocalDate.of(2000, 1, 1))
				.localidade(localidade)
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