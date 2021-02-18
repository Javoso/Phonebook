package com.phonebook.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.PersistenceException;

import com.phonebook.dao.PermissaoDAO;
import com.phonebook.dao.UsuarioDAO;
import com.phonebook.dao.datamodel.UsuarioDataModel;
import com.phonebook.dao.filter.UsuarioFilter;
import com.phonebook.exception.RegistroExistenteException;
import com.phonebook.model.Permissao;
import com.phonebook.model.Usuario;
import com.phonebook.service.UsuarioService;

@Named
@ViewScoped
public class UsuarioController extends AbstractController {

	private static final long serialVersionUID = -1013617852941176553L;

	@Inject
	private UsuarioDAO dao;

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private PermissaoDAO permissaoDAO;

	private Usuario selecionado = new Usuario();
	private UsuarioDataModel usuarios;
	private UsuarioFilter filter = new UsuarioFilter();

	private List<Permissao> permissoes = new ArrayList<>();

	@Override
	public void init() {
		pesquisar();
		permissoes = permissaoDAO.findAll();
		filter = new UsuarioFilter();
	}

	public void limpar() {
		selecionado = new Usuario();
	}

	public void pesquisar() {
		this.usuarios = new UsuarioDataModel(dao, filter);
		onSuccess("Lista de usuários");
	}

	public void deletar() {
		try {
			usuarioService.deletar(selecionado);
			onSuccess("Deletado com sucesso!");
		} catch (Exception e) {
			onError("Erro ao deletar usuário!");
		}
	}

	public void salvar() {
		try {
			if (selecionado.usuarioProntoParaSalvar()) {
				usuarioService.persist(selecionado);
				onSuccessWithFlash("Usuário salvo com sucesso!");
			} else {
				onError("Por favor, preencher todos oa campos obrgatórios");
			}
		} catch (PersistenceException e) {
			onError("Já existe um usuário cadastrado");
			e.printStackTrace();
		} catch (Exception e) {
			onError("Erro ao salvar o usuário!");
			logger.severe(e.getMessage());
		}

	}

	public void atualizar() {
		try {
			merge();
			onSuccessWithFlash("Usuário atualizado com sucesso!");
		} catch (Exception e) {
			onError("Erro ao atualizar o usuário!");
		}
	}

	private void merge() throws RegistroExistenteException {
		usuarioService.merge(selecionado);
	}

	public void resetarSenha() {
		try {
			selecionado.resetarSenha();
			usuarioService.merge(selecionado);
			onSuccess("Senha resetada com sucesso!");
		} catch (RegistroExistenteException e) {
			onError(e.getMessage());
			logger.severe(e.getMessage());
		}
	}

	public UsuarioDataModel getUsuarios() {
		return usuarios;
	}

	public UsuarioFilter getFilter() {
		return filter;
	}

	public Usuario getSelecionado() {
		return selecionado;
	}

	public void setSelecionado(Usuario selecionado) {
		this.selecionado = selecionado;
	}

	public List<Permissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<Permissao> permissoes) {
		this.permissoes = permissoes;
	}

}