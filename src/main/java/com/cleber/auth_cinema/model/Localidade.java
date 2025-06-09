package com.cleber.auth_cinema.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Localidade {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, length = 255)
	private String endereco;

	@Column(nullable = false, length = 20)
	private String cep;

	@Column(length = 255)
	private String referencia;
}