package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.app.dto.UserDto;
import com.app.dto.UserUpdateDto;
import com.app.dto.UserWithoutRoleDto;
import com.app.entity.User;
import com.app.repository.RoleRepository;
import com.app.service.UserService;
import com.app.shared.GenericResponse;

import javax.validation.Valid;


@RestController
public class UserController {
	
	
	@Autowired
	UserService userService;
	
	@Autowired
	RoleRepository roleRepository;
	
	@PostMapping("/users") 
	GenericResponse createUser(@Valid @RequestBody UserWithoutRoleDto userWithoutRole) {
		userService.save(userWithoutRole);
		return new GenericResponse("User created");
	}
	
	@GetMapping("/users") //without logged in user
	Page<UserDto> getUsers(Pageable page, Authentication authentication){
		User user=userService.getByUsername(authentication.getName());
		return userService.getUsers(page,user).map(UserDto::new);
	}
	
	@GetMapping("/users/{username}")
	UserDto getUser(@PathVariable String username) {
		return new UserDto(userService.getByUsername(username));
	}

	@PutMapping("/users/{username}")
	@PreAuthorize("#username == principal.username")
	UserDto updateUser(@Valid @RequestBody UserUpdateDto updatedUser, @PathVariable String username) {
		User user = userService.updateUser(username, updatedUser);
		return new UserDto(user);

	}
	
	@PutMapping("/users/update/{userid}")
	@Secured("ROLE_ADMIN")
	ResponseEntity<?> approveUser(@PathVariable long userid) {
		 	
		try{
			userService.updateUserIsApproved(userid);
			userService.deleteIdentityByUserId(userid);
			return ResponseEntity.ok().body("User is approved successfully.");
		}catch (Exception e) {
			System.out.println(e.toString());
			return ResponseEntity.badRequest().body("User authentication failed ");
		}
	
	}
	
		
}







