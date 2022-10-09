package com.app.model;

import lombok.Data;

@Data
public class AutoBidDto {
	
	private int maxBidLimit;
	
	private int auctionId;
	
	private String username;
}
