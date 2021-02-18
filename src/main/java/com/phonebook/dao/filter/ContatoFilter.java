package com.phonebook.dao.filter;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.Serializable;

import com.phonebook.model.Usuario;

public class ContatoFilter implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nome;
	
	private String sobrenome;
	
	private Usuario criador;

	public boolean hasNome() {
		return isNotBlank(nome);
	}
	
	public boolean hasSobreNome() {
		return isNotBlank(sobrenome);
	}
	
	public boolean hasCriador() {
		return nonNull(criador);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getSobrenome() {
		return sobrenome;
	}
	
	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public Usuario getCriador() {
		return criador;
	}

	public void setCriador(Usuario criador) {
		this.criador = criador;
	}

}