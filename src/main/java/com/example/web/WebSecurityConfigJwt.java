package com.example.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.web.login.AuthenticationTokenFilter;
import com.example.web.login.LoginService;
import com.example.web.login.RestAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class WebSecurityConfigJwt extends WebSecurityConfigurerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfigJwt.class);
	
	@Autowired
    private LoginService loginService;

    @Autowired
    private RestAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    @Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public AuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
		AuthenticationTokenFilter authenticationTokenFilter = new AuthenticationTokenFilter();
		authenticationTokenFilter.setAuthenticationManager(this.authenticationManagerBean());
		return authenticationTokenFilter;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
	        .authorizeRequests()
		        .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
		        .antMatchers(HttpMethod.POST,"/auth").permitAll()
		        .antMatchers(HttpMethod.GET,"/auth/refresh").permitAll()
				.antMatchers("/api/**").hasRole("USER")
				.anyRequest().authenticated().and()
			.requiresChannel().anyRequest().requiresSecure().and()
			.formLogin().disable();

		http.portMapper().http(8080).mapsTo(8443);
		
		http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);	
	}

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(loginService);
    }   
}
