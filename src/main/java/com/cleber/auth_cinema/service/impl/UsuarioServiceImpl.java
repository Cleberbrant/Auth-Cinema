package com.cleber.auth_cinema.service.impl;

import com.cleber.auth_cinema.dto.LocalidadeDTO;
import com.cleber.auth_cinema.dto.RegisterDTO;
import com.cleber.auth_cinema.dto.UsuarioDTO;
import com.cleber.auth_cinema.enums.Role;
import com.cleber.auth_cinema.model.Localidade;
import com.cleber.auth_cinema.model.Usuario;
import com.cleber.auth_cinema.repository.UsuarioRepository;
import com.cleber.auth_cinema.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public List<UsuarioDTO> listarTodos() {
		return usuarioRepository.findAll().stream()
				.map(this::toDTO)
				.collect(Collectors.toList());
	}

	@Override
	public Optional<UsuarioDTO> buscarPorId(Long id) {
		return usuarioRepository.findById(id)
				.map(this::toDTO);
	}

	@Override
	public UsuarioDTO atualizar(Long id, UsuarioDTO usuarioDTO) {
		Usuario usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
		usuario.setNome(usuarioDTO.getNome());
		usuario.setEmail(usuarioDTO.getEmail());
		usuario.setRole(usuarioDTO.getRole());
		if (usuarioDTO.getLocalidade() != null) {
			usuario.setLocalidade(toLocalidadeEntity(usuarioDTO.getLocalidade()));
		}
		usuarioRepository.save(usuario);
		return toDTO(usuario);
	}

	@Override
	public void deletar(Long id) {
		if (!usuarioRepository.existsById(id)) {
			throw new RuntimeException("Usuário não encontrado");
		}
		usuarioRepository.deleteById(id);
	}

	@Override
	public UsuarioDTO cadastrar(RegisterDTO dto) {
		Usuario usuario = Usuario.builder()
				.nome(dto.getNome())
				.dataNascimento(dto.getDataNascimento())
				.cpf(dto.getCpf())
				.email(dto.getEmail())
				.password(passwordEncoder.encode(dto.getPassword()))
				.role(dto.getRole() != null ? Role.valueOf(dto.getRole()) : Role.ROLE_USER)
				.localidade(toLocalidadeEntity(dto.getLocalidade()))
				.build();
		usuarioRepository.save(usuario);
		return toDTO(usuario);
	}

	@Override
	public UsuarioDTO promoverParaAdmin(Long id) {
		Usuario usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
		usuario.setRole(Role.ROLE_ADMIN);
		usuarioRepository.save(usuario);
		return toDTO(usuario);
	}

	private UsuarioDTO toDTO(Usuario usuario) {
		UsuarioDTO dto = new UsuarioDTO();
		dto.setId(usuario.getId());
		dto.setNome(usuario.getNome());
		dto.setEmail(usuario.getEmail());
		dto.setRole(usuario.getRole());
		dto.setLocalidade(toLocalidadeDTO(usuario.getLocalidade()));
		return dto;
	}

	private Localidade toLocalidadeEntity(LocalidadeDTO dto) {
		if (dto == null) return null;
		return Localidade.builder()
				.endereco(dto.getEndereco())
				.cep(dto.getCep())
				.referencia(dto.getReferencia())
				.build();
	}

	private LocalidadeDTO toLocalidadeDTO(Localidade localidade) {
		if (localidade == null) return null;
		LocalidadeDTO dto = new LocalidadeDTO();
		dto.setId(localidade.getId());
		dto.setEndereco(localidade.getEndereco());
		dto.setCep(localidade.getCep());
		dto.setReferencia(localidade.getReferencia());
		return dto;
	}
}