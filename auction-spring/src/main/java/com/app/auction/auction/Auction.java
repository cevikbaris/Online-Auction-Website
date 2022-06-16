package com.app.auction.auction;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;

import com.app.auction.bid.Bid;
import com.app.auction.category.Category;
import com.app.auction.file.FileAttachment;
import com.app.auction.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Data
@Entity
@Table(name = "auction_table")
public class Auction {
	

	//'6', 'bariscevik378@gmail.com', NULL, '0', 'Baris Admin', '$2a$12$8.NZKKVL6abVaAog4zDAKOx9U/8xsQq1gSSGL4pqnMB0ByY0/cws6', 'admin'


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
    @Column(nullable = false,columnDefinition="TEXT")
	private String explanation;
	
    @Column(nullable = false)
	private String title;
	

	@Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
	private Date startDate;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
	private Date endDate;
		
	private int sellNowPrice;
	
    @Column(nullable = false)
	private int startPrice;


	@Min(1)
	@Column(nullable=false)
	private int minimumIncrease;
	
    //------------------------
    
	@OneToOne(mappedBy = "auction",
			cascade = CascadeType.ALL)
	private FileAttachment fileAttachment;
	
	
	@OneToOne
	private Category category;
	
	
	@OneToMany(mappedBy = "auction", cascade = CascadeType.ALL)
	@JsonManagedReference(value="selling-item")
	private List<Bid> bids;
	
	
	@ManyToOne()
	@JoinColumn(name = "creator_id")
	@JsonBackReference(value="sell")
	private User creator;
	

	@ManyToOne()
	@JoinColumn(name = "buyer_id")
	//@JsonBackReference(value="buyer")
	private User buyer;
	
	public Auction(AuctionVM auctionVM) {
		this.explanation=auctionVM.getExplanation();
		this.title=auctionVM.getTitle();
		this.startDate=auctionVM.getStartDate();
		this.endDate=auctionVM.getEndDate();
		this.sellNowPrice=auctionVM.getSellNowPrice();
		this.startPrice=auctionVM.getStartPrice();
		this.minimumIncrease=auctionVM.getMinimumIncrease();
	}
	
	public Auction() {}
}
