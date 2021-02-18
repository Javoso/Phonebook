package com.phonebook.model;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "CONTATO")
public class Contato implements Serializable {

	private static final long serialVersionUID = 4538756384223968943L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CONTATO_ID")
	private Long id;

	@Column(name = "CONTATO_NOME")
	private String nome;

	@Column(name = "CONTATO_SOBRENOME")
	private String sobrenome;

	@ElementCollection(fetch = FetchType.EAGER)
	@Cascade(value = CascadeType.DELETE)
	@Column(name = "CONTATO_TELEFONES")
	private List<String> telefones = new ArrayList<>();

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "USUARIO_ID")
	private Usuario criador = new Usuario();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public List<String> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<String> telefones) {
		this.telefones = telefones;
	}

	public Usuario getCriador() {
		return criador;
	}

	public void setCriador(Usuario criador) {
		this.criador = criador;
	}

	public Contato(String nome, String sobrenome, List<String> telefones, Usuario criador) {
		super();
		this.nome = nome;
		this.sobrenome = sobrenome;
		this.telefones = telefones;
		this.criador = criador;
	}

	public Contato() {
	}

	public void adicionar(String telefone) {
		if (isNull(telefone))
			throw new RuntimeException("A referência do telefone não pode ser nula!");
		this.getTelefones().add(telefone);
	}

	public void atualizar(String telefone) {
		if (isNull(telefone))
			throw new RuntimeException("A referência do telefone não pode ser nula!");
		int index = this.getTelefones().indexOf(telefone);
		this.getTelefones().set(index, telefone);
	}

	public void remover(String telefone) {
		if (isNull(telefone))
			throw new RuntimeException("A referência do telefone não pode ser nula!");
		this.getTelefones().remove(telefone);
	}

	@Transient
	public boolean sobrenomeNaoEstaEmBranco() {
		return isNotBlank(getSobrenome());
	}

	@Transient
	public boolean nomeNaoEstaEmBranco() {
		return isNotBlank(getNome());
	}

	@Transient
	public boolean telefonesNaoEstaEmBranco() {
		return !getTelefones().isEmpty();
	}

	@Transient
	public boolean criadorNaoEstaEmBranco() {
		return nonNull(criador);
	}

	@Transient
	public boolean contatoProntoParaSalvar() {
		return nomeNaoEstaEmBranco() && sobrenomeNaoEstaEmBranco()
				&& telefonesNaoEstaEmBranco() & criadorNaoEstaEmBranco();
	}

	@Transient
	public boolean isCadastrado() {
		return nonNull(getId());
	}

	@Transient
	public boolean isNovo() {
		return !isCadastrado();
	}

	@Override
	public String toString() {
		return "Nome: " + nome + ", Sobrenome: " + sobrenome + ", telefone(s): " + this.telefones;
	}

}
