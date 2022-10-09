package com.app.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(uniqueConstraints={ //user can't bid same price for auction
	    @UniqueConstraint(columnNames = {"auction_id", "price"}) 
	}) 
public class Bid {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private int id;
	
	@ManyToOne()
    @JoinColumn(name = "buyer_id")
	//@JsonBackReference(value="bidder")
	private User bidder;
	
	
	@ManyToOne()
    @JoinColumn(name = "auction_id")
	//@JsonBackReference(value="selling-item")
	private Auction auction;
	
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
	private Date bidTime;
	
	
    @Column(nullable = false)
	private int price;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Bid bid = (Bid) o;
		return false;
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}







