package com.phonebook.util;

import javax.faces.context.FacesContext;

public class BuscaNoWebContent {
	
	private BuscaNoWebContent() { }

	public static String busfinArquivo(String path) {
		return FacesContext.getCurrentInstance().getExternalContext().getRealPath(path);
	}
}