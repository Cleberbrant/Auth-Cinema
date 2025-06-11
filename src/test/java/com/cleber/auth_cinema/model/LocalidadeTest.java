package com.cleber.auth_cinema.model;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocalidadeTest {

	@Test
	void testBuilderAndGetters() {
		Localidade localidade = Localidade.builder()
				.id(1)
				.endereco("Rua Y")
				.cep("87654321")
				.referencia("Ao lado da escola")
				.build();

		assertEquals(1, localidade.getId());
		assertEquals("Rua Y", localidade.getEndereco());
		assertEquals("87654321", localidade.getCep());
		assertEquals("Ao lado da escola", localidade.getReferencia());
	}

	@Test
	void testSetters() {
		Localidade localidade = new Localidade();
		localidade.setEndereco("Rua Z");
		assertEquals("Rua Z", localidade.getEndereco());
	}

	@Test
	void testEqualsAndHashCode() {
		Localidade l1 = Localidade.builder().id(1).build();
		Localidade l2 = Localidade.builder().id(1).build();
		assertEquals(l1, l2);
		assertEquals(l1.hashCode(), l2.hashCode());
	}

	@Test
	void testToString() {
		Localidade localidade = new Localidade();
		assertTrue(localidade.toString().contains("Localidade"));
	}
}