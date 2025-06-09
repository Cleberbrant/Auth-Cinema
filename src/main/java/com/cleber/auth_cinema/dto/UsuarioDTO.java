package com.cleber.auth_cinema.dto;

import com.cleber.auth_cinema.enums.Role;
import lombok.Data;

@Data
public class UsuarioDTO {
	private Long id;
	private String nome;
	private String email;
	private Role role;
	private LocalidadeDTO localidade;
}