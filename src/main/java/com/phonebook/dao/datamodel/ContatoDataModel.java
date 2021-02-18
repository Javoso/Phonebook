package com.phonebook.dao.datamodel;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.phonebook.dao.ContatoDAO;
import com.phonebook.dao.filter.ContatoFilter;
import com.phonebook.model.Contato;
import com.phonebook.model.Usuario;

public class ContatoDataModel extends LazyDataModel<Contato> implements Serializable {

	private static final long serialVersionUID = 1L;

	private ContatoFilter filter;

	private ContatoDAO contatoDAO;

	private int total;

	public ContatoDataModel(ContatoDAO contatoDAO, ContatoFilter filter) {
		this.filter = filter;
		this.contatoDAO = contatoDAO;
	}

	public ContatoDataModel(ContatoDAO contatoDAO, ContatoFilter filter, Usuario usuarioLogado) {
		this.filter = filter;
		this.contatoDAO = contatoDAO;
		if (!filter.hasCriador())
			filter.setCriador(usuarioLogado);
	}

	@Override
	public List<Contato> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {

		this.total = contatoDAO.count(filter).intValue();

		setRowCount(total);

		return contatoDAO.pesquisar(first, pageSize, filter);
	}

	public int getTotal() {
		return total;
	}

}