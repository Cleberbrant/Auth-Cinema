package com.cleber.auth_cinema.service.impl;

import com.cleber.auth_cinema.dto.RegisterDTO;
import com.cleber.auth_cinema.dto.UsuarioDTO;
import com.cleber.auth_cinema.enums.Role;
import com.cleber.auth_cinema.model.Usuario;
import com.cleber.auth_cinema.repository.UsuarioRepository;
import com.cleber.auth_cinema.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceImplTest {

	@Mock
	private UsuarioRepository usuarioRepository;
	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UsuarioServiceImpl usuarioService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void listarTodos_deveRetornarListaDeUsuariosDTO() {
		Usuario usuario = Usuario.builder().id(1L).nome("Teste").email("t@e.com").role(Role.ROLE_USER).build();
		when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

		List<UsuarioDTO> lista = usuarioService.listarTodos();

		assertEquals(1, lista.size());
		assertEquals("Teste", lista.get(0).getNome());
	}

	@Test
	void buscarPorId_existente_deveRetornarUsuarioDTO() {
		Usuario usuario = Usuario.builder().id(2L).nome("Maria").email("m@e.com").role(Role.ROLE_USER).build();
		when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usuario));

		Optional<UsuarioDTO> dto = usuarioService.buscarPorId(2L);

		assertTrue(dto.isPresent());
		assertEquals("Maria", dto.get().getNome());
	}

	@Test
	void atualizar_deveAtualizarUsuario() {
		Usuario usuario = Usuario.builder().id(3L).nome("Antigo").email("a@a.com").role(Role.ROLE_USER).build();
		UsuarioDTO dto = new UsuarioDTO();
		dto.setNome("Novo");
		dto.setEmail("novo@a.com");
		dto.setRole(Role.ROLE_ADMIN);

		when(usuarioRepository.findById(3L)).thenReturn(Optional.of(usuario));
		when(usuarioRepository.save(any())).thenReturn(usuario);

		UsuarioDTO atualizado = usuarioService.atualizar(3L, dto);

		assertEquals("Novo", atualizado.getNome());
		assertEquals(Role.ROLE_ADMIN, atualizado.getRole());
	}

	@Test
	void deletar_existente_naoLancaExcecao() {
		when(usuarioRepository.existsById(4L)).thenReturn(true);

		assertDoesNotThrow(() -> usuarioService.deletar(4L));
		verify(usuarioRepository).deleteById(4L);
	}

	@Test
	void cadastrar_deveSalvarEConverterParaDTO() {
		RegisterDTO dto = new RegisterDTO();
		dto.setNome("Novo");
		dto.setEmail("novo@e.com");
		dto.setPassword("senha");
		dto.setRole("ROLE_USER");

		when(passwordEncoder.encode(anyString())).thenReturn("senhaCriptografada");
		when(usuarioRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

		UsuarioDTO usuarioDTO = usuarioService.cadastrar(dto);

		assertEquals("Novo", usuarioDTO.getNome());
		assertEquals(Role.ROLE_USER, usuarioDTO.getRole());
	}

	@Test
	void promoverParaAdmin_deveAlterarRole() {
		Usuario usuario = Usuario.builder().id(5L).nome("User").role(Role.ROLE_USER).build();
		when(usuarioRepository.findById(5L)).thenReturn(Optional.of(usuario));
		when(usuarioRepository.save(any())).thenReturn(usuario);

		UsuarioDTO dto = usuarioService.promoverParaAdmin(5L);

		assertEquals(Role.ROLE_ADMIN, dto.getRole());
	}
}