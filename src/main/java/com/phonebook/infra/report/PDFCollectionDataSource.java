package com.phonebook.infra.report;

import java.util.Collection;

public class PDFCollectionDataSource extends PDF {

	private Collection<?> lista;
	
	public PDFCollectionDataSource(Collection<?> lista, String nomeArquivoJasper, String nomeArquivoSaida) {
		super(nomeArquivoJasper, nomeArquivoSaida);
		this.lista = lista;
	}
	
	@Override
	protected Report criarReport() {
		return new CollectionDataSourceReport(lista);
	}

}