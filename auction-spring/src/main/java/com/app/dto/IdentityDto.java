package com.app.dto;

import lombok.Data;

@Data
public class IdentityDto {
	
	private long idNumber;
	
	private int imageId;
	
	private String loggedInUsername;
}
