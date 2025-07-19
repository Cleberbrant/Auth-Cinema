package com.cleber.auth_cinema.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
	private String token;

	// Getter alternativo para compatibilidade
	public String getJwt() {
		return this.token;
	}

	public void setJwt(String jwt) {
		this.token = jwt;
	}
}