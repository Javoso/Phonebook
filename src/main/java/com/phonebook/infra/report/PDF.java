package com.phonebook.infra.report;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

public abstract class PDF {

	private static final String DIRETORIO = "/report/";
	
	private static final String EXTENSAO = ".jasper";
	
	public static final String LOGO = "/resources/image/phonebook-black.png";
	
	private FacesContext context = FacesContext.getCurrentInstance();
	
	private String nomeArquivo;
	
	private String nomeArquivoSaida;
	
	private Map<String, Object> parametros = new HashMap<>();
	
	private boolean download = true;
	
	/**
	 * Recebe o nome do arquivo .jasper, assim como o
	 * nome do arquivo de saída do PDF.
	 * 
	 * @param nomeArquivoJasper
	 * @param nomeArquivoSaida
	 */
	public PDF(String nomeArquivoJasper, String nomeArquivoSaida) {
		this.nomeArquivo = nomeArquivoJasper;
		this.nomeArquivoSaida = nomeArquivoSaida;
		
		addParametro("DATA", new Date());
		addParametro("LOGO", LOGO);
		addParametro("SUBREPORT_DIR", DIRETORIO);
	}
	
	/**
	 * Método principal. 
	 * Responsável por configurar e gerar o arquivo PDF.
	 */
	public void gerarPDF() {
		HttpServletResponse response = configurationResponse();
		
		try (InputStream jasper = getArquivoJasper();
			 ServletOutputStream out = response.getOutputStream()) {
			
			JasperPrint print = getJasperPrint(jasper);
		
			JasperExportManager.exportReportToPdfStream(print, out);
			context.responseComplete();
			out.flush();
		
		} catch (JRException | IOException e) {
			throw new RuntimeException(e);
		} 
		
	}

	/**
	 * A subclasse será a reponsável por fornecer a implementação.
	 * 
	 * @return
	 */
	protected abstract Report criarReport();
	
	/**
	 * Configura se será feito o download do PDF ou se será
	 * exibido no navegador. Download é o padrão.
	 * 
	 * @param download
	 */
	public void setDownload(boolean download) {
		this.download = download;
	}
	
	/**
	 * Método para adicionar parametro para ser utilizado
	 * na geração do PDF.
	 * 
	 * @param nome do parametro
	 * @param valor do parametro
	 */
	public void addParametro(String nome, Object valor) {
		this.parametros.put(nome, valor);
	}
	
	/**
	 * Adiciona um mapa de parametros.
	 * 
	 * @param parametros
	 */
	public void addParametros(Map<String, Object> parametros) {
		this.parametros.putAll(parametros);
	}

	/**
	 * Retorna o JasperPrint de acordo com a implementação escolhida.
	 * 
	 * @param jasper
	 * @return
	 * @throws JRException
	 */
	private JasperPrint getJasperPrint(InputStream jasper) throws JRException {
		return criarReport().getJasperPrint(jasper, this.parametros);
	}
	
	/**
	 * Método responsável por recuperar o arquivo jasper.
	 * 
	 * @return InputStream do arquivo.
	 */
	private InputStream getArquivoJasper() {
		return this.getClass().getResourceAsStream(DIRETORIO + nomeArquivo + EXTENSAO);
	}
	
	/**
	 * Método responsável por configurar o content-type
	 * e o header do HttpServletResponse.
	 * 
	 * @return o HttpServletResponse configurado.
	 */
	private HttpServletResponse configurationResponse() {
		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		response.setContentType("application/pdf");
		
		if (download)
			response.addHeader("content-disposition", "attachment; filename=" + nomeArquivoSaida + ".pdf");
		
		return response;
	}
	
}