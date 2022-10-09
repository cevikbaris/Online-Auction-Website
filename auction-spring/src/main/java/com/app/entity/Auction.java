package com.app.entity;

import com.app.model.AuctionRequest;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "auction_table")
public class Auction {

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
	private int price;


	@Column(nullable=false)
	private int minimumIncrease;
	

	@OneToOne(mappedBy = "auction",
			cascade = CascadeType.ALL)
	private FileAttachment fileAttachment;
	
	
	@OneToOne
	private Category category;
	
	
	@OneToMany(mappedBy = "auction", cascade = CascadeType.ALL)
	@JsonManagedReference(value="selling-item")
	@ToString.Exclude
	private List<Bid> bids;
	
	
	@ManyToOne()
	@JoinColumn(name = "creator_id")
	@JsonBackReference(value="sell")
	private User creator;
	

	@ManyToOne()
	@JoinColumn(name = "buyer_id")
	//@JsonBackReference(value="buyer")
	private User buyer;
	
	public Auction(AuctionRequest auctionRequest) {
		this.explanation=auctionRequest.getExplanation();
		this.title=auctionRequest.getTitle();
		this.startDate=auctionRequest.getStartDate();
		this.endDate=auctionRequest.getEndDate();
		this.sellNowPrice=auctionRequest.getSellNowPrice();
		this.price =auctionRequest.getStartPrice();
		this.minimumIncrease=auctionRequest.getMinimumIncrease();
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Auction auction = (Auction) o;
		return false;
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
