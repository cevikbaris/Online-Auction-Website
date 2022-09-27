package com.app.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.app.shared.UniqueUsername;
import com.app.shared.ValidEmail;

import lombok.Data;

@Data
public class UserWithoutRoleDto {
	
	@NotNull(message="Name can not be null")
	@Size(min = 2, max=50, message = "Name must be more than 2 letters ")
	private String name;
	

	@NotNull(message="Username can not be null")
	@UniqueUsername
	@Size(min=1,max=100  )
	private String username;
	
	@NotNull(message="Email can not be null")
	@ValidEmail
	private String email;
	
	
	@NotNull(message="Password can not be null")
	@Size(min = 4, max=16 , message = "Password must be a minimum of 4 characters and a maximum of 16 characters. ")
	private String password;

	private String image;
	
}
