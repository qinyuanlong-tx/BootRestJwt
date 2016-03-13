package com.example.web.domain.user;

import java.util.Date;

import com.example.web.domain.common.CommonVo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

//@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties({ "passWd" })
public class UserVo extends CommonVo {
	
	private static final long serialVersionUID = -5511115546763673243L;

	private String userId;
	private String passWd;
	private String authorities;

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassWd() {
		return passWd;
	}
	public void setPassWd(String passWd) {
		this.passWd = passWd;
	}
	public String getAuthorities() {
		return authorities;
	}
	public void setAuthorities(String authorities) {
		this.authorities = authorities;
	}
}
