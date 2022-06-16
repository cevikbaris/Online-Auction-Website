package com.app.auction.user.vm;

import java.util.Collection;

import com.app.auction.role.Role;
import com.app.auction.user.User;

import lombok.Data;

@Data
public class UserVM {
	
	private String name;
	
	private String username;
	
	private String email;
	
	private String image;
	
	private Collection<Role> role;
	
	private boolean isApproved;
	
	public UserVM(User user) {
		this.name = user.getName();
		this.email = user.getEmail();
		this.username=user.getUsername();
		this.image=user.getImage();
		this.role=user.getRoles();
		this.isApproved= user.isApproved();
	}
}
