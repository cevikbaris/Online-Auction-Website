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

import com.app.dto.AuctionDto;
import com.app.dto.AuctionReturnDto;
import com.app.dto.BidSubmitDto;
import com.app.dto.BidSuccessDto;
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
	public int createAuction(@Valid @RequestBody AuctionDto auctionVM ,@PathVariable String username) {
		
		User user = userService.getByUsername(username);
		return auctionService.save(auctionVM,user);
		
	}
	
	@GetMapping("/auction/{id}")
	public AuctionReturnDto getAuction (@PathVariable int id) {
		AuctionReturnDto auctionReturnVM =new AuctionReturnDto(auctionService.findById(id));
		User user=auctionService.getUserOfAuction(id);
		auctionReturnVM.setUsername(user.getUsername());
		auctionReturnVM.setName(user.getName());
		//auctionReturnVM.setCreator(user);
		return auctionReturnVM;
	}
	
	@GetMapping("/auction")
	public Page<AuctionReturnDto> getAuctions (Pageable page){
		
		Page<Auction> auctions= auctionService.getAuctions(page);

		return auctions.map(AuctionReturnDto::new);
	}
	
	
	@GetMapping("/category/auction/{categoryName}")
	public Page<AuctionReturnDto> getAuctionsByCategory (Pageable page,@PathVariable("categoryName") String categoryName){
		

		List<Integer> auctionIds= categoryRepository.getAuctionsByCategoryName(categoryName);
		List<Auction> auctions = new ArrayList<Auction>() ;
		
		if(auctionIds.isEmpty()) {
			return null;
		}else {
			for(Integer id : auctionIds) {
				auctions.add(auctionService.findById(id));
			}
			List<AuctionReturnDto> auctionReturnVMs = new ArrayList<AuctionReturnDto>();
			if(!auctions.isEmpty()) {
				for(Auction auction : auctions) {
					auctionReturnVMs.add(new AuctionReturnDto(auction));
				}
			}
			Page<AuctionReturnDto> auctionReturnVM = new PageImpl<>(auctionReturnVMs);
			return auctionReturnVM;
		}

	}
	
	@GetMapping("/auction/user/{username}")
	public Page<AuctionReturnDto> getAuctionOfUser (@PathVariable String username){	
		
		User user =userService.getByUsername(username);
		List<Auction> auctions = auctionService.getAuctions(user.getId());

		List<AuctionReturnDto> auctionReturnVMs = new ArrayList<AuctionReturnDto>();
		for(Auction auction : auctions) {
			auctionReturnVMs.add(new AuctionReturnDto(auction));
		}
		Page<AuctionReturnDto> auctionReturnVM = new PageImpl<>(auctionReturnVMs);
		return auctionReturnVM;
	}
	
	
	@PostMapping("/auction/bid")
	BidSuccessDto createBid(@RequestBody BidSubmitDto bid) {
		
		User user = userService.getByUsername(bid.getBuyerUsername());
		Auction auction = auctionService.findById(bid.getAuctionID());
		Bid newBid = new Bid();
		newBid.setAuction(auction);
		newBid.setBidder(user);
		newBid.setPrice(bid.getPrice());
		newBid.setBidTime(bid.getBidTime());
		try {
			bidRepository.save(newBid);
			auction.setStartPrice(bid.getPrice());
			auctionService.save(auction);
		}catch (Exception e) {//if same price trying to save throw exception
			System.out.println(e.toString());
		}
		return new BidSuccessDto(bid.getPrice(),user.getName(),bid.getBuyerUsername());
	}
	
	@GetMapping("/bid/auction/{id}")
	BidSuccessDto getBidsOfAuction(@PathVariable int id) {
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
		
		return new BidSuccessDto(maxBid.getPrice(), user.getName(), user.getUsername());
	}
	
	@GetMapping("/auction/mybids/{username}")
	List<AuctionReturnDto> getMyBids (@PathVariable String username) {
		User myUser = userService.getByUsername(username);
		
		List<Auction> auctions = auctionService.getMyWonAuctions(myUser.getId());
		if(auctions.isEmpty()) {
			return null; 
		}
		List<AuctionReturnDto> auctionReturnVM = new ArrayList<AuctionReturnDto>();
		for(Auction auction : auctions) {
			auctionReturnVM.add(new AuctionReturnDto(auction));
		}
		return auctionReturnVM;
	}
	
	@PutMapping("/auction/buy-now")
	BidSuccessDto buyAuctionNow (@RequestBody BidSubmitDto bidSubmitVM) {
		
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
		
		return new BidSuccessDto(bidSubmitVM.getPrice(), buyer.getName(), buyer.getUsername());
	}
		
}











