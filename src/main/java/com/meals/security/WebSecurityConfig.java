package com.meals.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String ADMIN_ROLE = "ADMIN";

	private static final String LOGIN_PAGE_URL = "/login.html";

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();

		http.authorizeRequests()
				.antMatchers(LOGIN_PAGE_URL).permitAll()
				.antMatchers("/**").hasRole(ADMIN_ROLE)
				.and()
				.formLogin()
				.loginPage(LOGIN_PAGE_URL)
				.permitAll()
				.and()
				.logout()
				.logoutSuccessUrl(LOGIN_PAGE_URL)
				.permitAll();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("meggers102").password("Kangas102").roles(ADMIN_ROLE);
	}


}