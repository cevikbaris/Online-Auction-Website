package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.app.dto.UserResponse;
import com.app.dto.UserUpdateRequest;
import com.app.dto.UserRequest;
import com.app.entity.User;
import com.app.service.UserService;
import com.app.shared.GenericResponse;

import javax.validation.Valid;


@RestController
public class UserController {

	@Autowired
	private UserService userService;

	
	@PostMapping("/users") 
	GenericResponse createUser(@Valid @RequestBody UserRequest userWithoutRole) {
		userService.save(userWithoutRole);
		return new GenericResponse("User created");
	}
	
	@GetMapping("/users") //without logged in user
	Page<UserResponse> getUsers(Pageable page, Authentication authentication){
		User user=userService.getByUsername(authentication.getName());
		return userService.getUsers(page,user).map(UserResponse::new);
	}
	
	@GetMapping("/users/{username}")
	UserResponse getUser(@PathVariable String username) {
		return new UserResponse(userService.getByUsername(username));
	}

	@PutMapping("/users/{username}")
	@PreAuthorize("#username == principal.username")
	UserResponse updateUser(@Valid @RequestBody UserUpdateRequest updatedUser, @PathVariable String username) {
		return new UserResponse(userService.updateUser(username, updatedUser));

	}
	
	@PutMapping("/users/update/{userid}")
	@Secured("ROLE_ADMIN")
	ResponseEntity<?> approveUser(@PathVariable long userid) {
		// TODO: 9.10.2022 try catch gerekli mi
		try{
			userService.updateUserIsApproved(userid);
			userService.deleteIdentityByUserId(userid);
			return ResponseEntity.ok().body("User is approved successfully.");
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("User authentication failed ");
		}
	
	}
	
		
}







