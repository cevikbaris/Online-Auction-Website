package com.app.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.entity.Bid;


@Repository
public interface BidRepository extends JpaRepository<Bid,Integer>{
	
	@Query(value="SELECT * FROM auction.bid where bid.price=:price and auction_id=:aid",nativeQuery = true)
	Bid findByPrice(@Param("price")int price , @Param("aid") int id );
	
	
	@Query(value="SELECT MAX(price) FROM auction_table as a\r\n"
			+ "INNER JOIN  bid as b\r\n"
			+ "on b.auction_id=a.id\r\n"
			+ "where a.id=:auction_id" , nativeQuery = true)
	Integer findHighestBid(@Param("auction_id")int auctionID);
	
	
	@Query(value="SELECT buyer_id FROM bid where auction_id=:id order by price desc LIMIT 1" ,nativeQuery = true)
	int findHighestBuyerId(@Param("id") int id);
	
	
	@Query(value = " SELECT * FROM auction.bid where auction_id=:id\r\n"
			+ "and bid_time>(  SELECT DATE_ADD( NOW(), INTERVAL -2 MINUTE) ) order by price desc " ,nativeQuery = true)
	List<Bid> getLast2minBiddersByIdOrderByPriceDesc(@Param("id") int id);
	
	
	@Modifying
	@Transactional
	@Query(value="UPDATE auction_table set start_price= (SELECT price FROM bid where auction_id=:id order by price desc LIMIT 1)\r\n"
			+ "where auction_table.id=:id " , nativeQuery=true)
	void updatePriceOfAuction(@Param("id") int id);
	
}
