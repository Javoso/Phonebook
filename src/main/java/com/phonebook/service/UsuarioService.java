package com.phonebook.service;

import java.io.Serializable;
import java.util.List;

import com.phonebook.exception.RegistroExistenteException;
import com.phonebook.exception.UsuarioException;
import com.phonebook.model.Usuario;

public interface UsuarioService extends Serializable {
	
	/**
	 * 
	 * @param usuario
	 * @throws RegistroExistenteException 
	 */
	void merge(Usuario usuario) throws RegistroExistenteException;

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
	Usuario findByID(Long id);

	/**
	 * 
	 * @param usuario
	 * @throws UsuarioException 
	 */
	void persist(Usuario usuario) throws UsuarioException;

	/**
	 * 
	 * @param email
	 * @return
	 */
	Usuario login(String email);
	/**
	 * 
	 *
	 * @return
	 */
	List<Usuario> findAll();

}
