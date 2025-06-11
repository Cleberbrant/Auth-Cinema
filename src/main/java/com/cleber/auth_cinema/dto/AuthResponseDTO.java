package com.cleber.auth_cinema.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class AuthResponseDTO {
	private String jwt;
}