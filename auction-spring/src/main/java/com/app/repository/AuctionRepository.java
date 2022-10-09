package com.app.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.entity.Auction;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Integer> {

	@Query(value=" SELECT username FROM auction_table \r\n"
			+ "LEFT JOIN user ON auction_table.creator_id=user.id \r\n"
			+ "where auction_table.id=:id " , nativeQuery = true)
	String usernameOfAuctionOwner(@Param("id") int id);

	
	@Query(value="SELECT a.id FROM user \r\n"
			+ "LEFT JOIN auction_table as a\r\n"
			+ "on user.id=a.creator_id\r\n"
			+ "where user.id=:user_id\r\n" , nativeQuery = true)
	List<Integer> getProductIdsOfUser(@Param("user_id") long id);


	@Query(value="SELECT * FROM auction.auction_table WHERE  end_date<now() and ISNULL(buyer_id)" , nativeQuery = true)
	List<Auction> findEndAuctionNullBuyers();
	

	@Query(value="SELECT auction_id FROM bid  LEFT JOIN auction_table  on bid.auction_id=auction_table.id   WHERE  end_date<now() and ISNULL(auction_table.buyer_id)  \r\n"
			+ "group by auction_id" , nativeQuery = true)
	List<Integer> getIdsBiddedAuctions();


	@Query(value="SELECT * FROM auction.auction_table where buyer_id=:id",nativeQuery = true)
	List<Auction> getMyWonAuctions(@Param("id") long id);
	

	@Query(value="SELECT * FROM auction.auction_table WHERE  end_date>now() " ,nativeQuery = true)
	List<Auction> getNotFinishedAuctions();


	@Query(value="SELECT * FROM auction.auction_table WHERE category_id=:categoryId", nativeQuery=true)
	List<Auction> findByCategoryId(@Param("categoryId") int categoryId);

	@Modifying
	@Transactional
	@Query(value="update auction_table SET end_date=:date_now , buyer_id =:buyer_id ,start_price=sell_now_price"
			+ " where auction_table.id=:auction_id " ,nativeQuery=true)
	void updateAuctionWithBuyNow(@Param("auction_id")int auctionId,@Param("date_now") Date bidTime, @Param("buyer_id") long buyerId);
	
	
	
}





