package com.app.auction.auction;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.app.auction.file.FileAttachmentVM;
import com.app.auction.user.User;

import lombok.Data;

@Data
public class AuctionReturnVM {
	
	private int id;
	
	private String explanation;
	
	private String title;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;
		
	private int sellNowPrice;
	
	private int startPrice;
	
	private int minimumIncrease;
	
	private FileAttachmentVM fileAttachment;
	
	private String username;
	private String name;
	private String category;
	
	public AuctionReturnVM(Auction auction,User user) {
		this.id=auction.getId();
		this.explanation=auction.getExplanation();
		this.title=auction.getTitle();
		this.startDate=auction.getStartDate();
		this.endDate=auction.getEndDate();
		this.sellNowPrice=auction.getSellNowPrice();
		this.startPrice=auction.getStartPrice();
		this.username=user.getUsername();
		this.name=user.getName();
		this.category=auction.getCategory().getCategoryName();
		this.minimumIncrease=auction.getMinimumIncrease();
		if(auction.getFileAttachment() !=null) {
			this.fileAttachment = new FileAttachmentVM(auction.getFileAttachment());
		}
	}
	
	public AuctionReturnVM(Auction auction) {
		this.id=auction.getId();
		this.explanation=auction.getExplanation();
		this.title=auction.getTitle();
		this.startDate=auction.getStartDate();
		this.endDate=auction.getEndDate();
		this.sellNowPrice=auction.getSellNowPrice();
		this.startPrice=auction.getStartPrice();
		this.category=auction.getCategory().getCategoryName();
		this.minimumIncrease=auction.getMinimumIncrease();
		if(auction.getFileAttachment() !=null) {
			this.fileAttachment = new FileAttachmentVM(auction.getFileAttachment());
		}
	
	}
	
}
