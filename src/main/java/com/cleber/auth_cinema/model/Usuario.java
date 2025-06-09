package com.cleber.auth_cinema.model;

import com.cleber.auth_cinema.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "O nome é obrigatório")
	@Size(max = 255, message = "O nome deve ter no máximo 255 caracteres")
	private String nome;

	@NotNull(message = "A data de nascimento é obrigatória")
	private LocalDate dataNascimento;

	@NotBlank(message = "O CPF é obrigatório")
	@Size(min = 11, max = 14, message = "O CPF deve ter entre 11 e 14 caracteres")
	@Column(unique = true)
	private String cpf;

	@NotBlank(message = "O email é obrigatório")
	@Email(message = "O email deve ser válido")
	@Column(unique = true)
	private String email;

	@NotBlank(message = "A senha é obrigatória")
	@Size(min = 6, max = 255, message = "A senha deve ter entre 6 e 255 caracteres")
	private String password;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "O tipo de conta é obrigatório")
	private Role role;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "localidade_id")
	@NotNull
	private Localidade localidade;
}