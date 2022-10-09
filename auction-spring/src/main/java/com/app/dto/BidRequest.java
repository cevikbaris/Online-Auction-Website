package com.app.dto;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.app.entity.Auction;

import lombok.Data;

@Data
public class BidRequest {
	
	private String buyerUsername;
	
	private int auctionID;
	
	@Temporal(TemporalType.TIMESTAMP)
    private Date bidTime;

	@NotNull
	private int price;
}
