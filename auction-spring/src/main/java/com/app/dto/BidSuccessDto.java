package com.app.dto;

import lombok.Data;

@Data
public class BidSuccessDto {
	
	private int price;
	private String name;
	private String username;
	public BidSuccessDto(int price, String name, String username) {
		this.price = price;
		this.name = name;
		this.username = username;
	} 
}
