package com.cleber.auth_cinema.controller;

import com.cleber.auth_cinema.dto.RegisterDTO;
import com.cleber.auth_cinema.dto.UsuarioDTO;
import com.cleber.auth_cinema.enums.Role;
import com.cleber.auth_cinema.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioControllerTest {

	@Mock
	private UsuarioService usuarioService;

	@InjectMocks
	private UsuarioController usuarioController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void cadastrar_deveRetornarCreated() {
		RegisterDTO dto = new RegisterDTO();
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		when(usuarioService.cadastrar(dto)).thenReturn(usuarioDTO);

		ResponseEntity<UsuarioDTO> response = usuarioController.cadastrar(dto);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(usuarioDTO, response.getBody());
	}

	@Test
	void listarUsuarios_deveRetornarLista() {
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		when(usuarioService.listarTodos()).thenReturn(List.of(usuarioDTO));

		ResponseEntity<List<UsuarioDTO>> response = usuarioController.listarUsuarios();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(1, response.getBody().size());
	}

	@Test
	void buscarPorId_existente_deveRetornarUsuario() {
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		when(usuarioService.buscarPorId(1L)).thenReturn(Optional.of(usuarioDTO));

		ResponseEntity<UsuarioDTO> response = usuarioController.buscarPorId(1L);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(usuarioDTO, response.getBody());
	}

	@Test
	void buscarPorId_inexistente_deveRetornarNotFound() {
		when(usuarioService.buscarPorId(1L)).thenReturn(Optional.empty());

		ResponseEntity<UsuarioDTO> response = usuarioController.buscarPorId(1L);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	void deletar_existente_deveRetornarNoContent() {
		doNothing().when(usuarioService).deletar(1L);

		ResponseEntity<Void> response = usuarioController.deletar(1L);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	@Test
	void deletar_inexistente_deveRetornarNotFound() {
		doThrow(new RuntimeException()).when(usuarioService).deletar(1L);

		ResponseEntity<Void> response = usuarioController.deletar(1L);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	void atualizar_existente_deveRetornarAtualizado() {
		UsuarioDTO dto = new UsuarioDTO();
		when(usuarioService.atualizar(eq(1L), any())).thenReturn(dto);

		ResponseEntity<UsuarioDTO> response = usuarioController.atualizar(1L, dto);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(dto, response.getBody());
	}

	@Test
	void atualizar_inexistente_deveRetornarNotFound() {
		UsuarioDTO dto = new UsuarioDTO();
		when(usuarioService.atualizar(eq(1L), any())).thenThrow(new RuntimeException());

		ResponseEntity<UsuarioDTO> response = usuarioController.atualizar(1L, dto);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	void promoverParaAdmin_deveRetornarPromovido() {
		UsuarioDTO dto = new UsuarioDTO();
		dto.setRole(Role.ROLE_ADMIN);
		when(usuarioService.promoverParaAdmin(1L)).thenReturn(dto);

		ResponseEntity<UsuarioDTO> response = usuarioController.promoverParaAdmin(1L);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(Role.ROLE_ADMIN, response.getBody().getRole());
	}
}