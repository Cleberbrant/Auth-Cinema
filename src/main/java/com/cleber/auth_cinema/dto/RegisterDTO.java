package com.cleber.auth_cinema.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
	private String email;
	private String password;
	private String nome;
	private LocalDate dataNascimento;
	private String cpf;
	private String role;
	private LocalidadeDTO localidade;
}