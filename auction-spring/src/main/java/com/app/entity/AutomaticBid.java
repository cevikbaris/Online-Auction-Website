package com.app.entity;

import javax.persistence.*;

import com.app.model.AutoBidRequest;

import lombok.Data;

@Data
@Entity
public class AutomaticBid {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private int id;
	
	private int maxBidLimit;
	
	private int auctionId;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	public AutomaticBid() {
		
	}
	public AutomaticBid(AutoBidRequest autoBidVM) {
		this.maxBidLimit=autoBidVM.getMaxBidLimit();
		this.auctionId=autoBidVM.getAuctionId();
	}
	
	
}
