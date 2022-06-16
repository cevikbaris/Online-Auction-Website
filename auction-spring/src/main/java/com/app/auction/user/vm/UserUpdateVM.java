package com.app.auction.user.vm;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.app.auction.shared.FileType;

import lombok.Data;

@Data
public class UserUpdateVM {


	@NotNull
	@Size(min = 2, max=255, message = "Name must be more than 2 letters  ")
	private String name;

	@FileType(types = {"jpeg" ,"png"})
	private String image;
}
