package com.phonebook.infra.report;

import java.io.InputStream;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

public interface Report {

	public JasperPrint getJasperPrint(InputStream jasper, Map<String, Object> parametros) throws JRException;
	
}