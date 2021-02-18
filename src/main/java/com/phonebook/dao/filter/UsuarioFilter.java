package com.phonebook.dao.filter;

import java.io.Serializable;

import com.phonebook.model.Permissao;


public class UsuarioFilter implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nome;

	private String email;
	
	private Permissao permissao;
	

	public boolean hasPermissao() {
		return permissao != null;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public Permissao getPermissao() {
		return permissao;
	}
	
	public void setPermissao(Permissao permissao) {
		this.permissao = permissao;
	}
}