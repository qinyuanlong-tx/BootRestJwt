package com.example.web.login;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import com.example.web.domain.user.UserVo;

public class AuthenticationTokenFilter extends UsernamePasswordAuthenticationFilter {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationTokenFilter.class);

	@Value("${jwt.token.header}")
	private String tokenHeader;

	@Autowired
	private TokenUtils tokenUtils;

//	@Override
//	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
//			throws IOException, ServletException {
//
//		HttpServletRequest httpRequest = (HttpServletRequest) req;
//		HttpServletResponse httpResponse = (HttpServletResponse) res;
//		String authToken = httpRequest.getHeader(tokenHeader);
//		String username = tokenUtils.getUsernameFromToken(authToken);
//
//		logger.debug("username " + username);
//		logger.debug("SecurityContextHolder.getContext().getAuthentication() is NULL " + 
//				(SecurityContextHolder.getContext().getAuthentication() == null));
//		
//		if (username != null) {
//			String commaSprAuthorities = tokenUtils.getAuthoritiesFromToken(authToken);
//			logger.debug("commaSprAuthorities " + commaSprAuthorities);
//
//			UserVo currentUser = new UserVo(username,commaSprAuthorities);
//			if (tokenUtils.validateToken(authToken, currentUser)) {
//				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//						username, null, AuthorityUtils.commaSeparatedStringToAuthorityList(commaSprAuthorities));
//				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
//				SecurityContextHolder.getContext().setAuthentication(authentication);
//			}
//		}
//		chain.doFilter(httpRequest, httpResponse);
//	}


	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String authToken = httpRequest.getHeader(tokenHeader);
		String username = tokenUtils.getUsernameFromToken(authToken);

		logger.debug("SecurityContextHolder.getContext().getAuthentication() is NULL " + 
				(SecurityContextHolder.getContext().getAuthentication() == null));
		
		if (username != null) {
			String commaSprAuthorities = tokenUtils.getAuthoritiesFromToken(authToken);
			UserVo currentUser = new UserVo(username,commaSprAuthorities);
			if (tokenUtils.validateToken(authToken, currentUser)) {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						username, null, AuthorityUtils.commaSeparatedStringToAuthorityList(commaSprAuthorities));
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		chain.doFilter(httpRequest, httpResponse);
	}
}
