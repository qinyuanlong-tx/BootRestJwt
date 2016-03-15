package com.example.web.login;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.web.domain.user.SecurityUserVo;
import com.example.web.domain.user.UserVo;
import com.example.web.sqlmappers.login.LoginMapper;

@Service
public class LoginService implements UserDetailsService {

	@Resource
	private LoginMapper loginMapper;
	
	public UserVo getUser(String userId) {
		UserVo userVo = null;
		if (userId != null && !"".equals(userId)) userVo = loginMapper.getUser(userId);
		return userVo;
	}

	public List<UserVo> getUserList() {
		List<UserVo> userList = new ArrayList<UserVo>();
		userList = loginMapper.getUserList();
		return userList;
	}
	
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		try {
			UserVo domainUser = getUser(userId);

			if (domainUser == null) throw new UsernameNotFoundException("Not Found ID : " + userId);
			
			SecurityUserVo securityUser = new SecurityUserVo(
					domainUser.getUserId(),
					domainUser.getPassWd(),
					getAuthorities(1));
			
			return securityUser;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Retrieves a collection of {@link GrantedAuthority} based on a numerical role
	 * @param role the numerical role
	 * @return a collection of {@link GrantedAuthority
	 */
	public Collection<? extends GrantedAuthority> getAuthorities(Integer role) {
		List<GrantedAuthority> authList = getGrantedAuthorities(getRoles(role));
		return authList;
	}

	/**
	 * Converts a numerical role to an equivalent list of roles
	 * @param role the numerical role
	 * @return list of roles as as a list of {@link String}
	 */
	public List<String> getRoles(Integer role) {
		List<String> roles = new ArrayList<String>();

		if (role.intValue() == 1) {
			roles.add("ROLE_USER");
			roles.add("ROLE_ADMIN");

		} else if (role.intValue() == 2) {
			roles.add("ROLE_USER");
		}

		return roles;
	}

	/**
	 * Wraps {@link String} roles to {@link SimpleGrantedAuthority} objects
	 * @param roles {@link String} of roles
	 * @return list of granted authorities
	 */
	public static List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return authorities;
	}
}
