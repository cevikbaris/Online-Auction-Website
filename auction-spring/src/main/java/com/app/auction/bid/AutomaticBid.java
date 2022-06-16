package com.app.auction.bid;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class AutomaticBid {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private int id;
	
	private int maxBidLimit;
	
	private int auctionId;
	
	private long userId;
	
	public AutomaticBid() {
		
	}
	public AutomaticBid(AutoBidVM autoBidVM) {
		this.maxBidLimit=autoBidVM.getMaxBidLimit();
		this.auctionId=autoBidVM.getAuctionId();
	}
	
	
}
