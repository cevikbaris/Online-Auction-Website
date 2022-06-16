package com.app.auction.auction;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class AuctionVM {
	
	@NotNull
	private String explanation;
	
	@NotNull
	private String title;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;
		
	@NotNull
	private int sellNowPrice;
	
	@NotNull
	private int startPrice;
	
	private int category;
	
	private long attachmentId;
	
	@NotNull
	private int minimumIncrease;
	
}
