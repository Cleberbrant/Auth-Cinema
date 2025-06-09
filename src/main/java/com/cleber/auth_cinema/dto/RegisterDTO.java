package com.cleber.auth_cinema.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class RegisterDTO {
	private String nome;
	private LocalDate dataNascimento;
	private String cpf;
	private String email;
	private String password;
	private String role;
	private LocalidadeDTO localidade;
}