package com.app.auction.bid;

import lombok.Data;

@Data
public class BidSuccess {
	
	private int price;
	private String name;
	private String username;
	public BidSuccess(int price, String name, String username) {
		this.price = price;
		this.name = name;
		this.username = username;
	} 
}
