package com.cleber.auth_cinema.controller;

import com.cleber.auth_cinema.dto.UsuarioDTO;
import com.cleber.auth_cinema.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

	private final UsuarioService usuarioService;

	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
		List<UsuarioDTO> usuarios = usuarioService.listarTodos();
		return ResponseEntity.ok(usuarios);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Long id) {
		return usuarioService.buscarPorId(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		try {
			usuarioService.deletar(id);
			return ResponseEntity.noContent().build();
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<UsuarioDTO> atualizar(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
		try {
			UsuarioDTO atualizado = usuarioService.atualizar(id, usuarioDTO);
			return ResponseEntity.ok(atualizado);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/{id}/promover")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<UsuarioDTO> promoverParaAdmin(@PathVariable Long id) {
		UsuarioDTO promovido = usuarioService.promoverParaAdmin(id);
		return ResponseEntity.ok(promovido);
	}
}
