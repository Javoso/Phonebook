package com.phonebook.dao;

import static javax.persistence.criteria.JoinType.LEFT;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.phonebook.dao.filter.UsuarioFilter;
import com.phonebook.exception.PermissaoException;
import com.phonebook.model.Permissao;
import com.phonebook.model.Permissao_;
import com.phonebook.model.Usuario;
import com.phonebook.model.Usuario_;

public class UsuarioDAO implements Serializable {

	private static final long serialVersionUID = 9063942926793212775L;

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
	 * @param usuario
	 * @return
	 */
	public Usuario merge(Usuario usuario) {
		return manager.merge(usuario);
	}

	/**
	 * 
	 * @param matricula
	 * @return
	 */
	public Usuario findByEmail(String email) {
		CriteriaQuery<Usuario> criteria = builder.createQuery(Usuario.class);
		Root<Usuario> u = criteria.from(Usuario.class);
		criteria.select(u).where(builder.equal(u.get(Usuario_.email), email));
		try {
			return manager.createQuery(criteria).getSingleResult();
		} catch (NoResultException e) {
			logger.severe("Usuário não encontrado!");
		}
		return null;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public Usuario findByID(Long id) {
		CriteriaQuery<Usuario> criteria = builder.createQuery(Usuario.class);
		Root<Usuario> u = criteria.from(Usuario.class);
		criteria.select(u).where(builder.equal(u.get(Usuario_.id), id));
		try {
			return manager.createQuery(criteria).getSingleResult();
		} catch (NoResultException e) {
			logger.severe("Usuário não encontrado!");
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
	public List<Usuario> pesquisar(int first, int pageSize, UsuarioFilter filter) {
		CriteriaQuery<Usuario> criteria = builder.createQuery(Usuario.class);
		Root<Usuario> root = criteria.from(Usuario.class);
		Predicate[] predicates = criarRestricoes(filter, root);
		criteria.where(predicates);
		criteria.orderBy(builder.asc(root.get(Usuario_.nome)));

		return manager.createQuery(criteria).setFirstResult(first).setMaxResults(pageSize).getResultList();
	}

	/**
	 * 
	 * @param filter
	 * @return
	 */
	public Long count(UsuarioFilter filter) {
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Usuario> root = criteria.from(Usuario.class);
		criteria.select(builder.count(root));
		Predicate[] predicates = criarRestricoes(filter, root);
		criteria.where(predicates);
		return manager.createQuery(criteria).getSingleResult();
	}

	private Predicate[] criarRestricoes(UsuarioFilter filter, Root<Usuario> root) {

		List<Predicate> predicates = new ArrayList<>();

		if (isNotEmpty(filter.getNome()))
			predicates.add(
					builder.like(builder.lower(root.get(Usuario_.nome)), "%" + filter.getNome().toLowerCase() + "%"));

		if (isNotEmpty(filter.getEmail()))
			predicates.add(builder.like(builder.lower(root.get(Usuario_.email)), "%" + filter.getEmail() + "%"));

		if (filter.hasPermissao()) {
			Join<Usuario, Permissao> g = root.join(Usuario_.permissoes);
			predicates.add(builder.equal(g.get(Permissao_.id), filter.getPermissao().getId()));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	/**
	 * 
	 * @param usuario
	 * @return
	 * @throws PermissaoException
	 */
	public List<Permissao> permissoes(Usuario usuario) throws PermissaoException {
		CriteriaQuery<Permissao> criteria = builder.createQuery(Permissao.class);
		Root<Usuario> u = criteria.from(Usuario.class);
		Join<Usuario, Permissao> p = u.join(Usuario_.permissoes);
		criteria.select(p).distinct(true).where(builder.equal(u, usuario));
		try {
			return manager.createQuery(criteria).getResultList();
		} catch (NoResultException e) {
			throw new PermissaoException("Usuário " + usuario + " não possui permissões");
		}
	}

	/**
	 * 
	 * @param usuario
	 */
	public void persist(Usuario usuario) {
		manager.persist(usuario);
	}

	/**
	 * 
	 * @param matricula
	 * @return
	 */
	public Usuario login(String email) {
		CriteriaQuery<Usuario> criteria = builder.createQuery(Usuario.class);
		Root<Usuario> u = criteria.from(Usuario.class);
		u.fetch(Usuario_.permissoes, LEFT);

		criteria.select(u).where(builder.equal(u.get(Usuario_.email), email));

		return manager.createQuery(criteria).getSingleResult();
	}

	/**
	 * 
	 * @param permissoes
	 * @return
	 */
	public List<Usuario> getUsuariosPorPermissoes(String... permissoes) {
		CriteriaQuery<Usuario> criteria = builder.createQuery(Usuario.class);
		Root<Usuario> u = criteria.from(Usuario.class);
		Join<Usuario, Permissao> g = u.join(Usuario_.permissoes);
		u.fetch(Usuario_.permissoes);
		criteria.select(u).distinct(true).where(g.get(Permissao_.nome).in(Arrays.asList(permissoes)))
				.orderBy(builder.asc(u.get(Usuario_.nome)));
		return manager.createQuery(criteria).getResultList();
	}

	/**
	 * 
	 * @param usuario
	 * 
	 */
	public void deletar(Usuario usuario) {
		usuario.setPermissoes(null);
		merge(usuario);
		CriteriaDelete<Usuario> delete = builder.createCriteriaDelete(Usuario.class);
		Root<Usuario> root = delete.from(Usuario.class);
		delete.where(builder.equal(root.get(Usuario_.id), usuario.getId()));
		manager.createQuery(delete).executeUpdate();
	}

	public List<Usuario> findAll() {
		return manager.createQuery("SELECT u FROM Usuario u ORDER BY u.nome", Usuario.class).getResultList();

	}
}