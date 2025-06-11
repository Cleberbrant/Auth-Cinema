package com.cleber.auth_cinema.model;
import com.cleber.auth_cinema.enums.Role;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

	@Test
	void testBuilderAndGetters() {
		Localidade localidade = Localidade.builder()
				.id(1)
				.endereco("Rua X")
				.cep("12345678")
				.referencia("Perto do mercado")
				.build();

		Usuario usuario = Usuario.builder()
				.id(1L)
				.nome("João")
				.dataNascimento(LocalDate.of(2000, 1, 1))
				.cpf("12345678901")
				.email("joao@email.com")
				.password("senha123")
				.role(Role.ROLE_USER)
				.localidade(localidade)
				.build();

		assertEquals(1L, usuario.getId());
		assertEquals("João", usuario.getNome());
		assertEquals(LocalDate.of(2000, 1, 1), usuario.getDataNascimento());
		assertEquals("12345678901", usuario.getCpf());
		assertEquals("joao@email.com", usuario.getEmail());
		assertEquals("senha123", usuario.getPassword());
		assertEquals(Role.ROLE_USER, usuario.getRole());
		assertEquals(localidade, usuario.getLocalidade());
	}

	@Test
	void testSetters() {
		Usuario usuario = new Usuario();
		usuario.setNome("Maria");
		assertEquals("Maria", usuario.getNome());
	}

	@Test
	void testEqualsAndHashCode() {
		Localidade loc = Localidade.builder().id(1).build();
		Usuario u1 = Usuario.builder().id(1L).localidade(loc).build();
		Usuario u2 = Usuario.builder().id(1L).localidade(loc).build();
		assertEquals(u1, u2);
		assertEquals(u1.hashCode(), u2.hashCode());
	}

	@Test
	void testToString() {
		Usuario usuario = new Usuario();
		assertTrue(usuario.toString().contains("Usuario"));
	}
}