package com.example.web.login;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.domain.jason.AuthenticationRequest;
import com.example.web.domain.jason.AuthenticationResponse;
import com.example.web.domain.user.SecurityUserVo;
import com.example.web.domain.user.UserVo;

@RestController
public class AuthenticationController {

	@Value("${jwt.token.header}")
	private String tokenHeader;

	@Autowired
	private AuthenticationManager authenticationManagerBean;

	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	private LoginService loginService;

	@RequestMapping(value = "/auth", method = RequestMethod.POST)
	public ResponseEntity<?> authenticationRequest(@RequestBody AuthenticationRequest authenticationRequest,
			Device device) throws AuthenticationException {
		
		// Perform the authentication
		Authentication requestAuth = new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		Authentication resultAuth = authenticationManagerBean.authenticate(requestAuth);
		SecurityContextHolder.getContext().setAuthentication(resultAuth);

		// generate Token (User ID, Authorities, Device Type, Created Date)
		@SuppressWarnings("unchecked")
		Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>)resultAuth.getAuthorities();
		String commaSprAuthorities = StringUtils.join(authorities, ',');
		UserVo loginUser = new UserVo(resultAuth.getName(),commaSprAuthorities);
		String token = this.tokenUtils.generateToken(loginUser, device);

		return ResponseEntity.ok(new AuthenticationResponse(token));
	}

	@RequestMapping(value = "/auth/refresh", method = RequestMethod.GET)
	public ResponseEntity<?> authenticationRequest(HttpServletRequest request) {
		String token = request.getHeader(this.tokenHeader);
		String username = this.tokenUtils.getUsernameFromToken(token);
		SecurityUserVo user = (SecurityUserVo) this.loginService.loadUserByUsername(username);
		if (this.tokenUtils.canTokenBeRefreshed(token, user.getLastPasswordReset())) {
			String refreshedToken = this.tokenUtils.refreshToken(token);
			return ResponseEntity.ok(new AuthenticationResponse(refreshedToken));
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}
}
