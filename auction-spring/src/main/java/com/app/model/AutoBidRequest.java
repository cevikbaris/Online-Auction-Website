package com.app.model;

import lombok.Data;

@Data
public class AutoBidRequest {
	
	private int maxBidLimit;
	
	private int auctionId;
	
	private String username;
}
