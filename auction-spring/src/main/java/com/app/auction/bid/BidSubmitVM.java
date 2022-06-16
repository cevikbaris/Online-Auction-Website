package com.app.auction.bid;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.app.auction.auction.Auction;

import lombok.Data;

@Data
public class BidSubmitVM {
	
	private String buyerUsername;
	
	private int auctionID;
	
	@Temporal(TemporalType.TIMESTAMP)
    private Date bidTime;
	
	private int price;
}
