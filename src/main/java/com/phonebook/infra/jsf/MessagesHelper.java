package com.phonebook.infra.jsf;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

public class MessagesHelper implements Serializable {

	private static final long serialVersionUID = -7694306678404358097L;
	
	@Inject
	private FacesContext context;
	

	public void addInfoMessage(String message) {
		addMessage(FacesMessage.SEVERITY_INFO, message);
	}

	public void addErrorMessage(String message) {
		addMessage(FacesMessage.SEVERITY_ERROR, message);
	}

	public void addFlash(String message) {
		context.getExternalContext().getFlash().setKeepMessages(true);
		addInfoMessage(message);
	}

	public void validationFailed(String message) {
		addErrorMessage(message);
		context.validationFailed();
	}

	private void addMessage(Severity severity, String msg) {
		FacesMessage facesMessage = new FacesMessage(severity, msg, msg);
		context.addMessage(null, facesMessage);
	}

}