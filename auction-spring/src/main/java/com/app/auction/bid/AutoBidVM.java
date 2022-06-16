package com.app.auction.bid;

import lombok.Data;

@Data
public class AutoBidVM {
	
	private int maxBidLimit;
	
	private int auctionId;
	
	private String username;
}
