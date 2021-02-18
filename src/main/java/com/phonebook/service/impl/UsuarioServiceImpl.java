package com.phonebook.service.impl;

import java.util.List;

import javax.inject.Inject;

import com.phonebook.dao.UsuarioDAO;
import com.phonebook.exception.ContatoException;
import com.phonebook.exception.UsuarioException;
import com.phonebook.infra.jpa.Transactional;
import com.phonebook.model.Contato;
import com.phonebook.model.Usuario;
import com.phonebook.service.ContatoService;
import com.phonebook.service.UsuarioService;

public class UsuarioServiceImpl implements UsuarioService {

	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioDAO usuarioDAO;

	@Inject
	private ContatoService contatoService;

	@Override
	@Transactional
	public void merge(Usuario usuario) {
		usuarioDAO.merge(usuario);
	}

	@Override
	@Transactional
	public void persist(Usuario usuario) throws UsuarioException {
		usuario.resetarSenha();
		usuarioDAO.persist(usuario);
	}

	@Override
	@Transactional
	public void deletar(Usuario usuario) {
		List<Contato> contatos;
		try {
			contatos = contatoService.contatosPorUsuario(usuario);
			contatos.forEach(contato -> {
				contato.setCriador(null);
				contatoService.deletar(contato);
			});
			usuarioDAO.deletar(usuario);
		} catch (ContatoException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Usuario findByID(Long id) {
		return usuarioDAO.findByID(id);
	}

	@Override
	public Usuario login(String matricula) {
		return usuarioDAO.login(matricula);
	}

	@Override
	public List<Usuario> findAll() {
		return usuarioDAO.findAll();
	}

}