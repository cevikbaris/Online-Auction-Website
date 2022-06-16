package com.app.auction.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.auction.shared.CurrentUser;
import com.app.auction.user.User;
import com.app.auction.user.UserRepository;
import com.app.auction.user.UserService;
import com.app.auction.user.vm.UserVM;

@RestController
public class AuthController {
	
	@Autowired
	UserService userService;

	@PostMapping("/auth")
	UserVM handleAuthentication(Authentication authentication) {//@CurrentUser User user
		User user=userService.getByUsername(authentication.getName());
		return new UserVM(user);
	}
}
