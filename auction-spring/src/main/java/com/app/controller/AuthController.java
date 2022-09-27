package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.UserDto;
import com.app.entity.User;
import com.app.repository.UserRepository;
import com.app.service.UserService;
import com.app.shared.CurrentUser;

@RestController
public class AuthController {
	
	@Autowired
	UserService userService;

	@PostMapping("/auth")
	UserDto handleAuthentication(Authentication authentication) {//@CurrentUser User user
		User user=userService.getByUsername(authentication.getName());
		return new UserDto(user);
	}
}
