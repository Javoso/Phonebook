package com.phonebook.service;

import java.io.Serializable;
import java.util.List;

import com.phonebook.exception.ContatoException;
import com.phonebook.exception.RegistroExistenteException;
import com.phonebook.model.Contato;
import com.phonebook.model.Usuario;

public interface ContatoService extends Serializable {
	
	/**
	 * 
	 * @param contato
	 * @throws RegistroExistenteException 
	 */
	void merge(Contato contato) throws RegistroExistenteException;

	/**
	 * 
	 * @param contato
	 */
	void deletar(Contato contato);
	
	/**
	 * 
	 * @param usuario
	 */
	void deletar(Usuario usuario);
	
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	Contato findByID(Long id);

	/**
	 * 
	 * @param contato
	 * @throws ContatoException 
	 */
	void persist(Contato contato) throws ContatoException;

	/**
	 * 
	 * @param nome
	 * @return
	 */
	Contato findByNome(String nome);
	
	/**
	 * 
	 * @param usuario
	 * @return
	 */
	List<Contato> contatosPorUsuario(Usuario usuario) throws ContatoException;

}
