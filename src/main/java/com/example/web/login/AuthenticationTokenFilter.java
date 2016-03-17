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

import com.example.web.domain.user.SecurityUserVo;

public class AuthenticationTokenFilter extends UsernamePasswordAuthenticationFilter {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationTokenFilter.class);

	private final String ORIGIN_LOCAL = "local";
	
	@Value("${jwt.token.header}")
	private String tokenHeader;

	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
    private LoginService loginService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String authToken = httpRequest.getHeader(tokenHeader);
		String username = tokenUtils.getUsernameFromToken(authToken);
		
		logger.debug("Created: " + tokenUtils.getCreatedDateFromToken(authToken) +
				"    Expiration: " + tokenUtils.getExpirationDateFromToken(authToken));
		if (username != null) {
			String orgin = tokenUtils.getOriginFromToken(authToken);
			SecurityUserVo currentUser = null;
			if (ORIGIN_LOCAL.equals(orgin)) currentUser = (SecurityUserVo)loginService.loadUserByUsername(username);
			if (tokenUtils.validateToken(authToken, currentUser)) {
				String commaSprAuthorities = tokenUtils.getAuthoritiesFromToken(authToken);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						username, null, AuthorityUtils.commaSeparatedStringToAuthorityList(commaSprAuthorities));
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		chain.doFilter(httpRequest, httpResponse);
	}
}
