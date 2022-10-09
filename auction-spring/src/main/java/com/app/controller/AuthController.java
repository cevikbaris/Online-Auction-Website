package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.UserResponse;
import com.app.entity.User;
import com.app.service.UserService;

@RestController
public class AuthController {
	
	@Autowired
	UserService userService;

	@PostMapping("/auth")
    UserResponse handleAuthentication(Authentication authentication) {//@CurrentUser User user
		User user=userService.getByUsername(authentication.getName());
		return new UserResponse(user);
	}
}
