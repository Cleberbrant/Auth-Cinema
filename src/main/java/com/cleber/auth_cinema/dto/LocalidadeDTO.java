package com.cleber.auth_cinema.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalidadeDTO {
	private Integer id;
	private String endereco;
	private String cep;
	private String referencia;
}