package com.cleber.auth_cinema.dto;

import lombok.Data;

@Data
public class AuthRequestDTO {
	private String email;
	private String password;
}