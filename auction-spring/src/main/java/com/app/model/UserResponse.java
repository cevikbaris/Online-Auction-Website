package com.app.model;

import java.util.Collection;

import com.app.entity.Role;
import com.app.entity.User;

import lombok.Data;

@Data
public class UserResponse {
	
	private String name;
	
	private String username;
	
	private String email;
	
	private String image;
	
	private Collection<Role> role;
	
	private boolean isApproved;
	
	public UserResponse(User user) {
		this.name = user.getName();
		this.email = user.getEmail();
		this.username=user.getUsername();
		this.image=user.getImage();
		this.role=user.getRoles();
		this.isApproved= user.isApproved();
	}
}
