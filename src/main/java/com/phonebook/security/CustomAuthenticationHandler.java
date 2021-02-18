package com.phonebook.security;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.phonebook.infra.cdi.CDIServiceLocator;
import com.phonebook.model.Usuario;

public class CustomAuthenticationHandler extends SimpleUrlAuthenticationSuccessHandler {
		
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
	
		UsuarioSistema usuarioSistema = (UsuarioSistema) authentication.getPrincipal();
		Usuario usuario = usuarioSistema.getUsuario();
		
		try {
			getRedirectStrategy().sendRedirect(request, response, usuario.getPaginaInicial());
		} catch (NullPointerException e) {
			Logger logger = CDIServiceLocator.getBean(Logger.class);
			logger.severe("Erro ao redirecionar usuário " + usuario.getNome());
		}
		
	}

}