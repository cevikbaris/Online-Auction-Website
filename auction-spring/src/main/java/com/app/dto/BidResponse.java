package com.app.dto;

import lombok.Data;

@Data
public class BidResponse {
	
	private int price;
	private String name;
	private String username;
	public BidResponse(int price, String name, String username) {
		this.price = price;
		this.name = name;
		this.username = username;
	} 
}
