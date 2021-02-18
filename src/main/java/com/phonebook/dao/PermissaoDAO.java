package com.phonebook.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import com.phonebook.infra.jpa.Transactional;
import com.phonebook.model.Permissao;

public class PermissaoDAO implements Serializable {


	private static final long serialVersionUID = 2622851319316151549L;
	
	@Inject
	private EntityManager manager;
	
	/**
	 * 
	 * @param permissao
	 * @return
	 */
	@Transactional
	public Permissao save(Permissao permissao) {
		return manager.merge(permissao);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Permissao> findAll() {
		return manager.createQuery("SELECT p FROM Permissao p ORDER BY p.nome", Permissao.class)
					  .getResultList();
	}
	
}