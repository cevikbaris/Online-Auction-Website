package com.app.model;

import com.app.entity.Auction;
import com.app.entity.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AuctionDetailsResponse extends BaseAuctionResponse {
	
	private int id;
	private int sellNowPrice;
	private int minimumIncrease;
	private String username;
	private String name;
	private String category;

	public AuctionDetailsResponse(Auction auction, User user) {
		this(auction);
		this.username=user.getUsername();
		this.name=user.getName();
	}

	public AuctionDetailsResponse(Auction auction) {
		super(auction);
		setId(auction.getId());
		setSellNowPrice(auction.getSellNowPrice());
		setCategory(auction.getCategory().getCategoryName());
		setMinimumIncrease(auction.getMinimumIncrease());

	
	}
	
}
