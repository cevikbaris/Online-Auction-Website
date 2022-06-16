package com.app.auction.user;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.auction.role.RoleRepository;
import com.app.auction.shared.GenericResponse;
import com.app.auction.user.vm.UserUpdateVM;
import com.app.auction.user.vm.UserVM;


@RestController
public class UserController {
	
	
	@Autowired
	UserService userService;
	
	@Autowired
	RoleRepository roleRepository;
	
	@PostMapping("/users") 
	GenericResponse createUser(@Valid @RequestBody UserWithoutRole userWithoutRole) {
		userService.save(userWithoutRole);
		return new GenericResponse("User created");
	}
	
	@GetMapping("/users") //without logged in user
	Page<UserVM> getUsers(Pageable page, Authentication authentication){
		User user=userService.getByUsername(authentication.getName());
		return userService.getUsers(page,user).map(UserVM::new);
	}
	
	@GetMapping("/users/{username}")
	UserVM getUser(@PathVariable String username) {
		return new UserVM(userService.getByUsername(username));
	}

	@PutMapping("/users/{username}")
	@PreAuthorize("#username == principal.username")
	UserVM updateUser(@Valid @RequestBody UserUpdateVM updatedUser, @PathVariable String username) {
		User user = userService.updateUser(username, updatedUser);
		return new UserVM(user);

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







