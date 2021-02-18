package com.phonebook.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.phonebook.infra.cdi.CDIServiceLocator;
import com.phonebook.model.Usuario;
import com.phonebook.service.UsuarioService;

public class AppUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String matricula) {
		UsuarioService usuarioService = CDIServiceLocator.getBean(UsuarioService.class);
		Usuario usuario = usuarioService.login(matricula);
		return new UsuarioSistema(usuario, getPermissoes(usuario));

	}

	private Collection<? extends GrantedAuthority> getPermissoes(Usuario usuario) {
		List<SimpleGrantedAuthority> permissoes = new ArrayList<>();
		usuario.getPermissoes()
				.forEach(a -> permissoes.add(new SimpleGrantedAuthority("ROLE_" + a.getNome().toUpperCase())));
		return permissoes;
	}
}