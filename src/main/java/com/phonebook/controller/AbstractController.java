package com.phonebook.controller;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.phonebook.infra.jsf.FacesUtil;
import com.phonebook.infra.jsf.MessagesHelper;
import com.phonebook.util.AES;


@SuppressWarnings("serial")
public abstract class AbstractController implements Serializable {

	@Inject
	private MessagesHelper helper;

	@Inject
	private FacesUtil facesUtil;
	
	@Inject
	protected transient Logger logger;

	@PostConstruct
	public void postConstruct() {
		init();
	}

	public abstract void init();

	protected void onSuccess(String message) {
		helper.addInfoMessage(message);
	}

	protected void onSuccessWithFlash(String message) {
		helper.addFlash(message);
	}

	protected void onError(String message) {
		helper.addErrorMessage(message);
	}

	protected void onError(String message, Throwable e) {
		onError(message);
		logger.severe(e.getMessage());
	}

	protected void onFailed(String message) {
		helper.validationFailed(message);
	}

	protected void updateComponents(boolean goToTop, String... components) {
		facesUtil.updateComponents(goToTop, components);
	}

	protected void updateComponents(String... components) {
		updateComponents(true, components);
	}

	protected void executeJS(String javascript) {
		facesUtil.executeJS(javascript);
	}

	protected void redirect(String page) {
		facesUtil.redirect(page);
	}

	protected String getParamName(String paramName) {
		return facesUtil.getParamName(paramName);
	}

	protected String getParamNameDecodificado(String paramName) {
		try {
			return new AES().decodificar(getParamName(paramName));
		} catch (NullPointerException e) {
			logger.severe("O paramêtro é nulo");
		}
		return null;
	}

}
