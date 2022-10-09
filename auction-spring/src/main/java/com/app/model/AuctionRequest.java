package com.app.model;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AuctionRequest {
	
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

	@Min(1)
	@NotNull
	private int minimumIncrease;
	
}
