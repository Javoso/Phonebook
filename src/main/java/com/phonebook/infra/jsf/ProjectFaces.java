package com.phonebook.infra.jsf;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;

import com.phonebook.util.AES;

@ApplicationScoped
public class ProjectFaces implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FacesContext context;

	@Inject
	private ExternalContext external;

	@Inject
	private HttpServletRequest request;

	public boolean isPostBack() {
		return context.isPostback();
	}

	public boolean isNotPostBack() {
		return !isPostBack();
	}
	
	public void foward(String page) {
		try {
			String contextPath = external.getRequestContextPath();
			
			external.dispatch(contextPath + page);
			context.responseComplete();
		} catch (IOException e) {
			throw new FacesException(e);
		}
	}

	public void redirect(String page) {
		try {
			String contextPath = external.getRequestContextPath();

			external.redirect(contextPath + page);
			context.responseComplete();
		} catch (IOException e) {
			throw new FacesException(e);
		}
	}

	public String getParamName(String paramName) {
		return request.getParameter(paramName);
	}

	/**
	 * Retorna o id descriptografado de um registro parametrizado via http get.
	 * 
	 * @param paramName Referente ao identificador do registro.
	 * @return Long
	 */
	public Long getParamIdLongDescriptografado(String paramName) {
		String param = request.getParameter(paramName);

		if (param == null)
			return null;

		if (param.length() != 22)
			return -1l;

		try {
			return Long.valueOf(new AES().decodificar(param));
		} catch (Exception e) {
			return -1l;
		}
	}

	public String getParametroDescriptografado(String parametro) {
		String param = request.getParameter(parametro);
		if (StringUtils.isBlank(param)) {
			return null;
		}
		return new AES().decodificar(param);
	}

	public String getParamMatricula(String paramName) {
		String param = request.getParameter(paramName);

		if (param == null || param.length() != 22)
			return null;

		try {
			return new AES().decodificar(param);
		} catch (Exception e) {
			return null;
		}
	}

	public String busfinArquivo(String path) {
		return external.getRealPath(path);
	}

	public String recuperaLogo() {
		return busfinArquivo("/resources/images/logo-unicatolica.png");
	}

	public String getUrl() {
		return context.getViewRoot().getViewId();
	}

	/**
	 * @return Este método retorna uma string contendo o nome do servidor, porta e
	 *         contexto. <br/>
	 *         <b>Ex.: <i>localhost:8080/sgo</i></b>
	 */
	public String getServerPortContext() {
		return "http://" + context.getExternalContext().getRequestServerName() + ":"
				+ context.getExternalContext().getRequestServerPort()
				+ context.getExternalContext().getRequestContextPath();
	}

	public String getParameterValue(String parametro) {
		return external.getRequestParameterMap().get(parametro);
	}

	public String[] getParameterValues(String paramName) {
		return external.getRequestParameterValuesMap().get(paramName);
	}

	public boolean hasParameters() {
		return external.getRequestParameterMap().size() > 0;
	}

	public void updateComponent(String idComponent) {
		PrimeFaces.current().ajax().update(idComponent);
	}

	public void updateComponents(String... idsComponent) {
		PrimeFaces.current().ajax().update(idsComponent);
	}

	public void executeJS(String javascript) {
		PrimeFaces.current().executeScript(javascript);
	}

	public FacesContext getFacesContext() {
		return context;
	}

	public void openDialog(String dialog) {
		executeJS("PF('" + dialog + "').show();");
		updateComponents(dialog);
	}

	public void closeDialog(String dialog) {
		executeJS("PF('" + dialog + "').hide();");
	}

	public void toTop() {
		executeJS("goToTop()");
	}

	public String getContextUrl() {
		HttpServletRequest httpServletRequest = (HttpServletRequest) external.getRequest();
		return httpServletRequest.getRequestURL()
				.substring(0, httpServletRequest.getRequestURL().length() - httpServletRequest.getRequestURI().length())
				.concat(httpServletRequest.getContextPath());
	}

	public String getUrlParametrizada(String parametro) {
		return getContextUrl().concat(parametro);
	}

	public boolean isAjaxRequest() {
		return PrimeFaces.current().isAjaxRequest();
	}

	public String getContextParamValue(String key) {
		try {
			return external.getInitParameter(key);
		} catch (NullPointerException e) {
			return null;
		}
	}

}