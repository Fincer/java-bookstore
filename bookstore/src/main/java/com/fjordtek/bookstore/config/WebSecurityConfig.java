// Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.fjordtek.bookstore.service.session.BookStoreAccessDeniedHandler;
import com.fjordtek.bookstore.service.session.UserDetailServiceImpl;

/**
*
*
* @author Pekka Helenius
*/

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		prePostEnabled = true
		)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailServiceImpl userDetailService;

	@Autowired
	private BookStoreAccessDeniedHandler bookStoreAccessDeniedHandler;

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
		.authorizeRequests()
			.antMatchers(
					"/",
					"/booklist",
					"/error",
					"/favicon.ico",
					"/css/**",
					"/js/**",
					"/images/**"
					).permitAll()
			.antMatchers("/apiref/**").hasAuthority("ADMIN")
			.anyRequest()
			.authenticated()
		.and()
			.formLogin()
				.defaultSuccessUrl("/booklist")
//				.loginPage("/login")
				.permitAll()
		.and()
			.logout()
				.logoutSuccessUrl("/booklist")
				.permitAll()
				.invalidateHttpSession(true)
		.and()
			.exceptionHandling()
			.accessDeniedHandler(bookStoreAccessDeniedHandler)
		.and()
			.csrf()
		;

	}

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authManagerBuilder)
    		throws Exception {
    	authManagerBuilder.userDetailsService(userDetailService);
    }
}