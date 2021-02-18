package com.phonebook.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.phonebook.dao.filter.ContatoFilter;
import com.phonebook.exception.ContatoException;
import com.phonebook.model.Contato;
import com.phonebook.model.Contato_;
import com.phonebook.model.Usuario;

public class ContatoDAO implements Serializable {

	private static final long serialVersionUID = 2374601448371816687L;

	@Inject
	private transient EntityManager manager;

	@Inject
	private transient Logger logger;

	private transient CriteriaBuilder builder;

	@PostConstruct
	public void init() {
		this.builder = manager.getCriteriaBuilder();
	}

	/**
	 * 
	 * @param contato
	 * @return
	 */
	public Contato merge(Contato contato) {
		return manager.merge(contato);
	}

	/**
	 * 
	 * @param nome
	 * @return
	 */
	public Contato findByNome(String nome) {
		CriteriaQuery<Contato> criteria = builder.createQuery(Contato.class);
		Root<Contato> u = criteria.from(Contato.class);
		criteria.select(u).where(builder.equal(u.get(Contato_.nome), nome));
		try {
			return manager.createQuery(criteria).getSingleResult();
		} catch (NoResultException e) {
			logger.severe("Contato não encontrado!");
		}
		return null;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public Contato findByID(Long id) {
		CriteriaQuery<Contato> criteria = builder.createQuery(Contato.class);
		Root<Contato> u = criteria.from(Contato.class);
		criteria.select(u).where(builder.equal(u.get(Contato_.id), id));
		try {
			return manager.createQuery(criteria).getSingleResult();
		} catch (NoResultException e) {
			logger.severe("Contato não encontrado!");
		}
		return null;
	}

	/**
	 * 
	 * @param first
	 * @param pageSize
	 * @param filter
	 * @return
	 */
	public List<Contato> pesquisar(int first, int pageSize, ContatoFilter filter) {
		CriteriaQuery<Contato> criteria = builder.createQuery(Contato.class);
		Root<Contato> root = criteria.from(Contato.class);
		Predicate[] predicates = criarRestricoes(filter, root);
		criteria.where(predicates);
		criteria.orderBy(builder.asc(root.get(Contato_.nome)));

		return manager.createQuery(criteria).setFirstResult(first).setMaxResults(pageSize).getResultList();
	}

	/**
	 * 
	 * @param filter
	 * @return
	 */
	public Long count(ContatoFilter filter) {
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Contato> root = criteria.from(Contato.class);
		criteria.select(builder.count(root));
		Predicate[] predicates = criarRestricoes(filter, root);
		criteria.where(predicates);
		return manager.createQuery(criteria).getSingleResult();
	}

	private Predicate[] criarRestricoes(ContatoFilter filter, Root<Contato> root) {

		List<Predicate> predicates = new ArrayList<>();

		if (filter.hasNome())
			predicates.add(
					builder.like(builder.lower(root.get(Contato_.nome)), "%" + filter.getNome().toLowerCase() + "%"));

		if (filter.hasSobreNome())
			predicates
					.add(builder.like(builder.lower(root.get(Contato_.sobrenome)), "%" + filter.getSobrenome() + "%"));

		if (filter.hasCriador())
			predicates.add(builder.equal(root.get(Contato_.criador), filter.getCriador()));

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	/**
	 * 
	 * @param usuario
	 * @return
	 * @throws ContatoException
	 */
	public List<Contato> contatosPorUsuario(Usuario usuario) throws ContatoException {
		CriteriaQuery<Contato> criteria = builder.createQuery(Contato.class);
		Root<Contato> u = criteria.from(Contato.class);
		criteria.select(u).distinct(true).where(builder.equal(u.get(Contato_.criador), usuario));
		criteria.orderBy(builder.asc(u.get(Contato_.nome)));
		try {
			return manager.createQuery(criteria).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ContatoException("Usuário " + usuario.getNome() + " não possui contatos");
		}
	}

	/**
	 * 
	 * @param contato
	 */
	public void persist(Contato contato) {
		manager.persist(contato);
	}

	/**
	 * 
	 * @param contato
	 * 
	 */
	public void deletar(Contato contato) {
		contato.setTelefones(null);
		merge(contato);
		CriteriaDelete<Contato> delete = builder.createCriteriaDelete(Contato.class);
		Root<Contato> root = delete.from(Contato.class);
		delete.where(builder.equal(root.get(Contato_.id), contato.getId()));
		manager.createQuery(delete).executeUpdate();
	}
	
	/**
	 * 
	 * @param contato
	 * 
	 */
	public void deletar(Usuario usuario) {
		CriteriaDelete<Contato> delete = builder.createCriteriaDelete(Contato.class);
		Root<Contato> root = delete.from(Contato.class);
		delete.where(builder.equal(root.get(Contato_.criador), usuario));
		manager.createQuery(delete).executeUpdate();
	}
}