package com.example.web.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.domain.jason.AuthenticationRequest;
import com.example.web.domain.jason.AuthenticationResponse;
import com.example.web.domain.user.UserVo;
import com.example.web.login.LoginService;

@RestController
public class UserController {
	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
    private LoginService loginService;

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
	public List<UserVo> getUserVoList() {
    	List<UserVo> userList = new ArrayList<UserVo>();
    	userList = loginService.getUserList();
		return userList;
	}
}
