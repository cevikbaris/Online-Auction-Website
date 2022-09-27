package com.app.dto;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.app.entity.Auction;

import lombok.Data;

@Data
public class BidSubmitDto {
	
	private String buyerUsername;
	
	private int auctionID;
	
	@Temporal(TemporalType.TIMESTAMP)
    private Date bidTime;
	
	private int price;
}
