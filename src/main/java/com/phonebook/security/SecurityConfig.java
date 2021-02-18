package com.phonebook.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String PRIVATE = "/private/**";
	private static final String PUBLIC = "/public/**";
	private static final String RESOURCES_IMG = "/resources/img/**";
	private static final String RESOURCES_FONTS = "/resources/fonts/**";
	private static final String JAVAX_FACES_RESOURCE = "/javax.faces.resource/**";
	private static final String URL_LOGIN = "/login.xhtml";

	@Bean
	public Md5PasswordEncoder passwordEncoder() {
		return new Md5PasswordEncoder();
	}

	@Override
	@Bean
	public AppUserDetailsService userDetailsService() {
		return new AppUserDetailsService();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().headers().frameOptions().sameOrigin().and()

				.authorizeRequests()
				.antMatchers(URL_LOGIN, JAVAX_FACES_RESOURCE, RESOURCES_FONTS, RESOURCES_IMG)
				.permitAll()
				.antMatchers(PUBLIC)
				.hasAnyRole("USER", "ADMIN")
				.antMatchers(PRIVATE)
				.hasRole("ADMIN").and()
				.formLogin().loginPage(URL_LOGIN).failureUrl("/login.xhtml?invalid=true")
				.successHandler(new CustomAuthenticationHandler()).and()

				.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).invalidateHttpSession(true)
				.logoutSuccessUrl(URL_LOGIN).and()

				.exceptionHandling().accessDeniedPage("/403.xhtml");
	}
}