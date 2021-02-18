package com.phonebook.service.impl;

import java.util.List;

import javax.inject.Inject;

import com.phonebook.dao.ContatoDAO;
import com.phonebook.exception.ContatoException;
import com.phonebook.infra.jpa.Transactional;
import com.phonebook.model.Contato;
import com.phonebook.model.Usuario;
import com.phonebook.service.ContatoService;

public class ContatoServiceImpl implements ContatoService {

	private static final long serialVersionUID = 1L;

	@Inject
	private ContatoDAO contatoDAO;

	@Override
	@Transactional
	public void merge(Contato contato) {
		contatoDAO.merge(contato);
	}

	@Override
	@Transactional
	public void persist(Contato contato) throws ContatoException {
		contatoDAO.persist(contato);
	}

	@Override
	@Transactional
	public void deletar(Contato contato) {
		contatoDAO.deletar(contato);
	}

	@Override
	@Transactional
	public void deletar(Usuario usuario) {
		contatoDAO.deletar(usuario);
	}

	@Override
	public Contato findByID(Long id) {
		return contatoDAO.findByID(id);
	}

	@Override
	public Contato findByNome(String nome) {
		return contatoDAO.findByNome(nome);
	}

	@Override
	public List<Contato> contatosPorUsuario(Usuario usuario) throws ContatoException {
		return contatoDAO.contatosPorUsuario(usuario);
	}

}