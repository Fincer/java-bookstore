// Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.fjordtek.bookstore.service.session.BookStoreAccessDeniedHandler;
import com.fjordtek.bookstore.service.session.UserDetailServiceImpl;

/**
*
* @see https://docs.spring.io/spring-security/site/docs/current/reference/html5/#multiple-httpsecurity
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
	private static BookStoreAccessDeniedHandler bookStoreAccessDeniedHandler;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authManagerBuilder)
    		throws Exception {
    	authManagerBuilder.userDetailsService(userDetailService);
    }


    /*
     * Have different HTTP security policies for:
     *
     * 1) native REST API end points
     * 2) web form authentication & authorization
     *
     */

	@Configuration
	@Order(1)
	public static class ApiWebSecurityConfig extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity httpSecurity) throws Exception {
			httpSecurity
			.antMatcher("/api/**")
			.authorizeRequests(
					authorize -> authorize
					.anyRequest().hasAuthority("ADMIN")
					)
			.httpBasic()
			.and()
				.csrf().disable()
			;
		}

	}

	@Configuration
	@Order(2)
	public static class WebFormWebSecurityConfig extends WebSecurityConfigurerAdapter {
/*
		@Override
		public void configure(WebSecurity webSecurity) throws Exception {
			webSecurity.ignoring().antMatchers("/foo/**");
		}
*/
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
			// TODO do not expose /login URI end point (prevent direct access)
				.defaultSuccessUrl("/booklist")
//				.loginPage("/booklist")
//				.permitAll()
			.and()
				.logout()
					.logoutSuccessUrl("/booklist")
					.permitAll()
					.invalidateHttpSession(true)
					.clearAuthentication(true)
			.and()
				.exceptionHandling()
				.accessDeniedHandler(bookStoreAccessDeniedHandler)
			.and()
				.csrf()
			.and()
				.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
			;

		}
	}

}