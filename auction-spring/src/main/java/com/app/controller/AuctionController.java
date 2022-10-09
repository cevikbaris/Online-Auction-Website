package com.app.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.AuctionRequest;
import com.app.dto.AuctionResponse;
import com.app.dto.BidRequest;
import com.app.dto.BidResponse;
import com.app.entity.Auction;
import com.app.entity.Bid;
import com.app.entity.User;
import com.app.error.NotFoundException;
import com.app.repository.BidRepository;
import com.app.repository.CategoryRepository;
import com.app.service.AuctionService;
import com.app.service.UserService;

@RestController
public class AuctionController {
	
	@Autowired
	AuctionService auctionService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	BidRepository bidRepository;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	
	@PostMapping("/auction/{username}")
	public int createAuction(@Valid @RequestBody AuctionRequest auction , @PathVariable String username) {
		
		User user = userService.getByUsername(username);
		return auctionService.save(auction,user);
		
	}
	
	@GetMapping("/auction/{id}")
	public AuctionResponse getAuction (@PathVariable int id) {
		AuctionResponse auctionResponse =new AuctionResponse(auctionService.findById(id));
		User user=userService.getUserOfAuction(id);
		auctionResponse.setUsername(user.getUsername());
		auctionResponse.setName(user.getName());
		//auctionReturnVM.setCreator(user);
		return auctionResponse;
	}
	
	@GetMapping("/auction")
	public Page<AuctionResponse> getAuctions (Pageable page){
		Page<Auction> auctions= auctionService.getAuctions(page);
		return auctions.map(AuctionResponse::new);
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
			if(!auctions.isEmpty()) {
				for(Auction auction : auctions) {
					auctionReturnVMs.add(new AuctionResponse(auction));
				}
			}
			Page<AuctionResponse> auctionReturnVM = new PageImpl<>(auctionReturnVMs);
			return auctionReturnVM;
		}

	}
	
	@GetMapping("/auction/user/{username}")
	public Page<AuctionResponse> getAuctionOfUser (@PathVariable String username){
		
		User user =userService.getByUsername(username);
		List<Auction> auctions = auctionService.getUserAuctions(user.getId());

		List<AuctionResponse> auctionResponseList = new ArrayList<>();

		// TODO: 9.10.2022 bu stream kullanımı doğru mu test et
		//auctionResponseList= auctions.stream().map(AuctionResponse::new).collect(Collectors.toList());

		for(Auction auction : auctions) {
			auctionResponseList.add(new AuctionResponse(auction));
		}

		return new PageImpl<>(auctionResponseList);
	}
	
	
	@PostMapping("/auction/bid")
	BidResponse createBid(@RequestBody BidRequest bid) {
		
		User user = userService.getByUsername(bid.getBuyerUsername());
		Auction auction = auctionService.findById(bid.getAuctionID());
		Bid newBid = new Bid();
		newBid.setAuction(auction);
		newBid.setBidder(user);
		newBid.setPrice(bid.getPrice());
		newBid.setBidTime(bid.getBidTime());

		try {
			bidRepository.save(newBid);
			auction.setPrice(bid.getPrice());
			auctionService.save(auction);
		}catch (Exception e) {//if same price trying to save throw exception
			System.out.println(e.toString());
		}
		return new BidResponse(bid.getPrice(),user.getName(),bid.getBuyerUsername());
	}
	
	@GetMapping("/bid/auction/{id}")
	BidResponse getBidsOfAuction(@PathVariable int id) {
		Auction auction =auctionService.findById(id); 
		List<Bid> bids = auction.getBids();
		if(bids.isEmpty()) {
			throw new NotFoundException();
		}
		int max = 0;
		for(Bid bid : bids) {
			if(bid.getPrice()> max){
				max=bid.getPrice();
			}
		}
		Bid maxBid = bidRepository.findByPrice(max,id);
		User user= maxBid.getBidder();
		
		return new BidResponse(maxBid.getPrice(), user.getName(), user.getUsername());
	}
	
	@GetMapping("/auction/mybids/{username}")
	List<AuctionResponse> getMyBids (@PathVariable String username) {
		User myUser = userService.getByUsername(username);
		
		List<Auction> auctions = auctionService.getMyWonAuctions(myUser.getId());
		if(auctions.isEmpty()) {
			return null; 
		}
		List<AuctionResponse> auctionReturnVM = new ArrayList<AuctionResponse>();
		for(Auction auction : auctions) {
			auctionReturnVM.add(new AuctionResponse(auction));
		}
		return auctionReturnVM;
	}
	
	@PutMapping("/auction/buy-now")
	BidResponse buyAuctionNow (@RequestBody BidRequest bidSubmitVM) {
		
		User buyer = userService.getByUsername(bidSubmitVM.getBuyerUsername());
		Auction auction = auctionService.findById(bidSubmitVM.getAuctionID());
		auctionService.buyNow( auction.getId(), bidSubmitVM.getBidTime(), buyer.getId());
		//save to bid table to see last bidder in page 
		Bid newBid = new Bid();
		newBid.setAuction(auction);
		newBid.setBidder(buyer);
		newBid.setPrice(bidSubmitVM.getPrice());
		newBid.setBidTime(bidSubmitVM.getBidTime());
		bidRepository.save(newBid);
		
		return new BidResponse(bidSubmitVM.getPrice(), buyer.getName(), buyer.getUsername());
	}
		
}











