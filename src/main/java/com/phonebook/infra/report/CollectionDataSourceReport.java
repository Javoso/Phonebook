package com.phonebook.infra.report;

import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class CollectionDataSourceReport implements Report {

	private Collection<?> lista;
	
	public CollectionDataSourceReport(Collection<?> lista) {
		this.lista = lista;
	}
	
	@Override
	public JasperPrint getJasperPrint(InputStream jasper, Map<String, Object> parametros) throws JRException {
		return JasperFillManager.fillReport(jasper, parametros, new JRBeanCollectionDataSource(lista));
	}
	
}