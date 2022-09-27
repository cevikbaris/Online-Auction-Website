package com.app.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
@Table(uniqueConstraints={ //user cant bid same price for auction
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
	
	
	@NotNull
    @Column(nullable = false)
	private int price;
	
}







