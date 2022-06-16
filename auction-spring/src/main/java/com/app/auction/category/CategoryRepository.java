package com.app.auction.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.auction.auction.Auction;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

	
	@Query(value="SELECT id FROM auction.category where category_name=:name " , nativeQuery=true)
	int findByCategoryName(@Param("name")String category);
	
	@Query(value="SELECT auction_table.id FROM auction_table \r\n"
			+ "left join category \r\n"
			+ "on auction_table.category_id=category.id \r\n"
			+ "where category.category_name=:categoryName ",nativeQuery=true)
	List<Integer> getAuctionsByCategoryName(@Param("categoryName") String categoryName);

}
