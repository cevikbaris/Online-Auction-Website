package com.app.controller;

import java.util.ArrayList;
import java.util.List;

import com.app.model.AuctionDetailsResponse;
import com.app.entity.Auction;
import com.app.model.AuctionResponse;
import com.app.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.app.entity.Category;
import com.app.repository.CategoryRepository;

@RestController
public class CategoryController {

	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	AuctionService auctionService;
	
	@GetMapping("/category")
	List<Category> getCategories(){
		return categoryRepository.findAll();

	}

	@GetMapping("/category/auction/{categoryName}")
	public Page<AuctionResponse> getAuctionsByCategory (Pageable page, @PathVariable("categoryName") String categoryName){
		List<Integer> auctionIds= categoryRepository.getAuctionsByCategoryName(categoryName);
		List<Auction> auctions = new ArrayList<>() ;

		if(auctionIds.isEmpty()) {
			return null;
		}else {
			for(Integer id : auctionIds) {
				auctions.add(auctionService.findById(id));
			}
			List<AuctionResponse> auctionReturnVMs = new ArrayList<>();
			for(Auction auction : auctions) {
				auctionReturnVMs.add(new AuctionResponse(auction));
			}
			return new PageImpl<>(auctionReturnVMs);
		}

	}
}
