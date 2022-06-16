package com.app.auction.bid;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AutomaticBidRepository extends JpaRepository<AutomaticBid, Integer> {
	
	// these queries can be short if create a funciton for long select queries.
	
	// if there is a automatic bidding request but there is no bid in bid table for auction
	// the auto bidding will be first bid for auction. In this case there are not any bidder for auction.
	@Modifying
	@Transactional
	@Query(value="INSERT INTO `auction`.`bid`\r\n"
			+ "(`bid_time`,`price`,`auction_id`,`buyer_id`)\r\n"
			+ "SELECT NOW(),\r\n"
			+ "(SELECT minimum_increase+start_price FROM auction.auction_table \r\n"
			+ "left join automatic_bid on automatic_bid.auction_id=auction_table.id where auction_table.id=:auctionID ),\r\n"
			+ "auto.auction_id,auto.user_id\r\n"
			+ "FROM automatic_bid as auto left join bid \r\n"
			+ "on auto.auction_id = bid.auction_id\r\n"
			+ "where ISNULL(bid.auction_id) and auto.auction_id=:auctionID " , nativeQuery=true)
	void	ifBidIsNullAndAutoBidValid(@Param("auctionID") int id);

	
	
	
	// this case will bid for us to the auction that has an bid and that bid is not user's bid
	// the query get the higher bid for auction and check is bid from me. if is not make a new bid with minimum increase price
	@Modifying
	@Transactional
	@Query(value="INSERT INTO `auction`.`bid`\r\n"
			+ "(`bid_time`,`price`,`auction_id`,`buyer_id`)\r\n"
			+ "SELECT NOW(),\r\n"
			+ "(SELECT minimum_increase+start_price FROM auction.auction_table \r\n"
			+ "left join automatic_bid on automatic_bid.auction_id=auction_table.id where auction_table.id=:auctionID),\r\n"
			+ "auto.auction_id , auto.user_id \r\n"
			+ "FROM auction.automatic_bid as auto LEFT JOIN bid \r\n"
			+ "on bid.auction_id = auto.auction_id \r\n"
			+ "where auto.max_bid_limit>(SELECT minimum_increase+start_price FROM auction.auction_table \r\n"
			+ "left join automatic_bid on automatic_bid.auction_id=auction_table.id where auction_table.id=:auctionID) "
			+ "and bid.auction_id=:auctionID \r\n"
			+ "and (SELECT buyer_id FROM bid where auction_id=:auctionID order by price desc LIMIT 1) != auto.user_id LIMIT 1; " , nativeQuery=true)
	void ifBidValidForAuctionBidAuto(@Param("auctionID") int id);





	@Modifying
	@Transactional
	@Query(value="SELECT auto.auction_id FROM automatic_bid as auto left join bid \r\n"
			+ "on auto.auction_id = bid.auction_id where ISNULL(bid.auction_id)", nativeQuery=true)
	List<Integer> auctionIdNoBidAndAutoBidValid();





}
