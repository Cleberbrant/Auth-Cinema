package com.cleber.auth_cinema.service;

import com.cleber.auth_cinema.dto.UsuarioDTO;
import com.cleber.auth_cinema.dto.RegisterDTO;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
	List<UsuarioDTO> listarTodos();
	Optional<UsuarioDTO> buscarPorId(Long id);
	UsuarioDTO atualizar(Long id, UsuarioDTO usuarioDTO);
	void deletar(Long id);
	UsuarioDTO cadastrar(RegisterDTO dto);
	UsuarioDTO promoverParaAdmin(Long id);
}
