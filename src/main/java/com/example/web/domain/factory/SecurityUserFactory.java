package com.example.web.domain.factory;

import org.springframework.security.core.authority.AuthorityUtils;

import com.example.web.domain.user.SecurityUserVo;
import com.example.web.domain.user.UserVo;

public class SecurityUserFactory {

	public static SecurityUserVo create(UserVo userVo) {
		return new SecurityUserVo(
				userVo.getUserId(),
				userVo.getPassWd(),
				AuthorityUtils.commaSeparatedStringToAuthorityList(userVo.getAuthorities()));
	}
}
