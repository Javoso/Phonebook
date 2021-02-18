package com.phonebook.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.PersistenceException;

import org.primefaces.component.export.ExcelOptions;

import com.phonebook.dao.ContatoDAO;
import com.phonebook.dao.datamodel.ContatoDataModel;
import com.phonebook.dao.filter.ContatoFilter;
import com.phonebook.exception.ContatoException;
import com.phonebook.exception.RegistroExistenteException;
import com.phonebook.infra.report.PDFCollectionDataSource;
import com.phonebook.model.Contato;
import com.phonebook.model.Usuario;
import com.phonebook.security.Logado;
import com.phonebook.service.ContatoService;
import com.phonebook.service.UsuarioService;
import com.phonebook.util.BuscaNoWebContent;

@Named
@ViewScoped
public class ContatoController extends AbstractController {

	private static final long serialVersionUID = -8914390906649246369L;

	private static final String LOGO_RODAPE = "logorodape";
	private static final String LOGO = "logo";
	private static final String USUARIO_NOME = "usuarioNome";
	private static final String QUANTIDADE = "quantidade";

	@Inject
	private ContatoDAO dao;

	@Inject
	private ContatoService contatoService;

	@Inject
	private UsuarioService usuarioService;

	@Inject
	@Logado
	private Usuario usuarioLogado;

	private Contato selecionado = new Contato();
	private ContatoDataModel contatos;
	private ContatoFilter filter = new ContatoFilter();

	private List<Usuario> usuarios = new ArrayList<>();

	@Override
	public void init() {
		pesquisar();
		filter = new ContatoFilter();
		usuarios = usuarioService.findAll();
	}

	public ExcelOptions getCustomizationOptions() {
		ExcelOptions excelOpt = new ExcelOptions();
		excelOpt.setFacetBgColor("#807DA8");
		excelOpt.setFacetFontColor("#FFFFFF");
		excelOpt.setFacetFontStyle("BOLD");

		return excelOpt;
	}

	public void gerarPDF() {

		try {
			Map<String, Object> mapParam = new HashMap<>();
			List<Contato> contatos = new ArrayList<>();
			if (filter.hasCriador()) {
				contatos = contatoService.contatosPorUsuario(filter.getCriador());
				mapParam.put(USUARIO_NOME, filter.getCriador().getNome());
			} else {
				contatos = contatoService.contatosPorUsuario(usuarioLogado);
				mapParam.put(USUARIO_NOME, usuarioLogado.getNome());
			}

			mapParam.put(LOGO, BuscaNoWebContent.busfinArquivo("/resources/image/phonebook-black.png"));
			mapParam.put(LOGO_RODAPE, BuscaNoWebContent.busfinArquivo("/resources/image/phonebook-white.png"));
			mapParam.put(QUANTIDADE, contatos.size());

			PDFCollectionDataSource pdf = new PDFCollectionDataSource(contatos, "meus_contatos", "Meus contatos");
			pdf.addParametros(mapParam);
			pdf.gerarPDF();

		} catch (ContatoException e) {
			e.printStackTrace();
		}

	}

	public void limpar() {
		selecionado = new Contato();
	}

	public void pesquisar() {
		this.contatos = new ContatoDataModel(dao, filter, usuarioLogado);
		onSuccess("Lista de contatos");
	}

	public void deletar() {
		try {
			contatoService.deletar(selecionado);
			onSuccess("Deletado com sucesso!");
		} catch (Exception e) {
			onError("Erro ao deletar contato!");
		}
	}

	public void salvar() {
		try {
			selecionado.setCriador(usuarioLogado);
			if (selecionado.contatoProntoParaSalvar()) {
				contatoService.persist(selecionado);
				onSuccessWithFlash("Contato salvo com sucesso!");
			} else {
				onError("Por favor, preencher todos os campos obrgatórios");
			}
		} catch (PersistenceException e) {
			onError("Já existe um contato cadastrado");
			e.printStackTrace();
		} catch (Exception e) {
			onError("Erro ao salvar o contato!");
			logger.severe(e.getMessage());
		}
	}

	public void atualizar() {
		try {
			merge();
			onSuccessWithFlash("Contato atualizado com sucesso!");
		} catch (Exception e) {
			onError("Erro ao atualizar o contato!");
		}
	}

	private void merge() throws RegistroExistenteException {
		contatoService.merge(selecionado);
	}

	public ContatoDataModel getContatos() {
		return contatos;
	}

	public ContatoFilter getFilter() {
		return filter;
	}

	public Contato getSelecionado() {
		return selecionado;
	}

	public void setSelecionado(Contato selecionado) {
		this.selecionado = selecionado;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

}