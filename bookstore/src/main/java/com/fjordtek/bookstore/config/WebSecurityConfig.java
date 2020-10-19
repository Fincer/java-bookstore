// Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.config;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fjordtek.bookstore.service.session.BookStoreAccessDeniedHandler;
import com.fjordtek.bookstore.service.session.BookStoreAuthenticationFailureHandler;
import com.fjordtek.bookstore.service.session.BookStoreAuthenticationSuccessHandler;
import com.fjordtek.bookstore.service.session.UserDetailServiceImpl;

/**
 *
 * This class and its subclasses extend Spring Framework security
 * WebSecurityConfigurerAdapter configuration for HTTP end points and sessions.
 * <p>
 * The class uses a custom UserDetailService interface class implementation
 * to determine proper user authorities.
 * <p>
 * This class has multiple sub-configurations for different end points.
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
	private Environment env;

	@Autowired
	private MessageSource msg;

	@Autowired
	private UserDetailServiceImpl userDetailService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authManagerBuilder)
    		throws Exception {
    	authManagerBuilder.userDetailsService(userDetailService)
    	.passwordEncoder(bCryptPasswordEncoder())
    	;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
		/*
		 * Set hash strength to 14 (2^14) + use RNG to randomize generated hash.
		 * Default strength value is 10.
		 */
		return new BCryptPasswordEncoder(14, new SecureRandom());
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
	public class ApiWebSecurityConfig extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity httpSecurity) throws Exception {
			httpSecurity
			.requestMatchers()
				.antMatchers(
						env.getProperty("spring.data.rest.base-path") + "/**",
						env.getProperty("page.url.actuator")          + "/**"
						)
			.and()
			.authorizeRequests(
					authorize -> authorize
					.anyRequest().hasAuthority(env.getProperty("auth.authority.admin"))
					)
				.httpBasic()
			.and()
				.csrf()
					.disable()
			;
		}

	}

	@Configuration
	@Order(2)
	public class WebFormWebSecurityConfig extends WebSecurityConfigurerAdapter {
/*
		@Override
		public void configure(WebSecurity webSecurity) throws Exception {
			webSecurity.ignoring().antMatchers("/foo/**");
		}
*/
		@Override
		protected void configure(HttpSecurity httpSecurity) throws Exception {

			/*
			 *  Explicitly Permit access to specific end points.
			 *  Basic norm is: if the end point access is not permitted here,
			 *  public access to it is denied by default.
			 */
			httpSecurity
//			.addFilterAfter(new BookSameSiteCookieFilter(), BasicAuthenticationFilter.class)
			.authorizeRequests()
				.antMatchers(
					env.getProperty("spring.h2.console.path")    + "/**",
					env.getProperty("page.url.dev")              + "/**",
					env.getProperty("page.url.index"),
					env.getProperty("page.url.list"),
					env.getProperty("page.url.error"),
					env.getProperty("page.url.resources.css")    + "/**",
					env.getProperty("page.url.resources.js")     + "/**",
					env.getProperty("page.url.resources.images") + "/**"
//					"/favicon.ico",
					).permitAll()
				.antMatchers(
						env.getProperty("page.url.apiref")       + "/**"
						)
					.hasAuthority(env.getProperty("auth.authority.admin"))
				.anyRequest()
				.authenticated()
			.and()
				.formLogin()
					.usernameParameter(env.getProperty("auth.field.username"))
					.passwordParameter(env.getProperty("auth.field.password"))
					.successHandler(new BookStoreAuthenticationSuccessHandler())
					.failureHandler(new BookStoreAuthenticationFailureHandler(env, msg))
					.loginProcessingUrl(env.getProperty("page.url.login"))
					.loginPage(env.getProperty("page.url.list"))
					.defaultSuccessUrl(env.getProperty("page.url.list"))
					.permitAll()
			.and()
				.logout()
					.logoutSuccessUrl(env.getProperty("page.url.list"))
					.permitAll()
					.invalidateHttpSession(true)
					.clearAuthentication(true)
					.deleteCookies("JSESSIONID")
			.and()
				.exceptionHandling()
					.accessDeniedHandler(new BookStoreAccessDeniedHandler())
			.and()
				.csrf()
					.ignoringAntMatchers(env.getProperty("spring.h2.console.path") + "/**")
			.and()
				.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
/*			.and()
				.headers()
					.frameOptions().sameOrigin()
//					.contentTypeOptions().disable()
					.contentSecurityPolicy("frame-ancestors 'self'")
*/
			;

		}
	}

}