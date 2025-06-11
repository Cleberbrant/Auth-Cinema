package com.cleber.auth_cinema.repository;

import com.cleber.auth_cinema.model.Localidade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class LocalidadeRepositoryTest {

	@Autowired
	private LocalidadeRepository localidadeRepository;

	@Test
	void save_devePersistirLocalidade() {
		Localidade localidade = Localidade.builder()
				.endereco("Rua Teste")
				.cep("12345-678")
				.referencia("Perto do mercado")
				.build();
		Localidade resultado = localidadeRepository.save(localidade);

		assertNotNull(resultado.getId());
		assertEquals("Rua Teste", resultado.getEndereco());
	}

	@Test
	void findById_deveRetornarLocalidade() {
		Localidade localidade = Localidade.builder()
				.endereco("Rua Teste")
				.cep("12345-678")
				.referencia("Perto do mercado")
				.build();
		Localidade salvo = localidadeRepository.save(localidade);

		Localidade resultado = localidadeRepository.findById(salvo.getId()).orElse(null);

		assertNotNull(resultado);
		assertEquals("Rua Teste", resultado.getEndereco());
	}

	@Test
	void findById_deveRetornarVazio() {
		assertTrue(localidadeRepository.findById(999).isEmpty());
	}
}