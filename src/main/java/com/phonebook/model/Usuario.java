package com.phonebook.model;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.NotBlank;

import com.phonebook.util.Criptografia;

@Entity
@Table(name = "USUARIO")
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1140901365395325202L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USUARIO_ID")
	private Long id;

	@Column(name = "USUARIO_NOME")
	@NotBlank(message = "O campo 'Nome' não pode fica em branco")
	private String nome;

	@Column(name = "USUARIO_EMAIL")
	@NotBlank(message = "O campo 'E-Mail' não pode fica em branco")
	private String email;

	@Column(name = "USUARIO_SENHA")
	private String senha;

	@ManyToMany(cascade=CascadeType.MERGE)
	@JoinTable(name = "USUARIO_PERMISSAO", joinColumns = { @JoinColumn(name = "USUARIO_ID") }, inverseJoinColumns = {
	@JoinColumn(name = "PERMISSAO_ID") })
	private List<Permissao> permissoes = new ArrayList<>();

	/**
	 * Adiciona uma permissão ao perfil
	 * 
	 * @param permissao
	 *            Permissão que deve adicionada
	 */
	public void adicionar(Permissao permissao) {
		if (isNull(permissao))
			throw new RuntimeException("A referência da permissão não pode ser nula!");
		this.getPermissoes().add(permissao);
	}

	/**
	 * Atualiza os dados de uma dada permissão do perfil
	 * 
	 * @param permissao
	 *            Permissão que deve ter os dados atualizados
	 */
	public void atualizar(Permissao permissao) {
		if (isNull(permissao))
			throw new RuntimeException("A referência da permissão não pode ser nula!");
		int index = this.getPermissoes().indexOf(permissao);
		this.getPermissoes().set(index, permissao);
	}

	/**
	 * Remove uma permissão do perfil
	 * 
	 * @param permissao
	 *            Permissão que deve ser removida
	 */
	public void remover(Permissao permissao) {
		if (isNull(permissao))
			throw new RuntimeException("A referência da permissão não pode ser nula!");
		this.getPermissoes().remove(permissao);
	}

	public Usuario(String nome, String email, String senha, List<Permissao> permissoes) {
		super();
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.permissoes = permissoes;
	}

	public Usuario() {
	}

	public boolean temPermissao(String permissao) {
		return this.getPermissoes().stream().anyMatch(p -> p.getNome().equals(permissao));
	}

	public boolean temPermissao(Permissao permissao) {
		return this.getPermissoes().stream().anyMatch(p -> p.equals(permissao));
	}

	public boolean senhaNaoEstaEmBranco() {
		return isNotBlank(getSenha());
	}

	public boolean nomeNaoEstaEmBranco() {
		return isNotBlank(getNome());
	}

	public boolean emailNaoEstaEmBranco() {
		return isNotBlank(getEmail());
	}

	public boolean usuarioProntoParaSalvar() {
		return nomeNaoEstaEmBranco() && emailNaoEstaEmBranco();
	}

	public void resetarSenha() {
		senha = new Criptografia().criptografar("123");
	}

	@Transient
	public boolean isCadastrado() {
		return nonNull(getId());
	}

	@Transient
	public boolean isNovo() {
		return !isCadastrado();
	}

	@Transient
	public String getPaginaInicial() {
		if(temPermissao("ADMIN"))
			return "/private/dashboard.xhtml";
		if(temPermissao("USER"))
			return "/public/contatos.xhtml";
		return "";
	}

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

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public List<Permissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<Permissao> permissoes) {
		this.permissoes = permissoes;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

}
