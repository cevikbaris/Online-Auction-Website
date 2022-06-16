package com.app.auction.bid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.auction.shared.GenericResponse;
import com.app.auction.user.User;
import com.app.auction.user.UserService;

@RestController
public class BidController {
	
	@Autowired
	BidRepository bidRepository;
	
	@Autowired
	AutomaticBidRepository automaticBidRepository;
	
	@Autowired
	UserService userService;
	
	@PostMapping("/auto/bid")
	GenericResponse createAutoBidding (@RequestBody AutoBidVM autoBidVM) {
		
		AutomaticBid automaticBid = new AutomaticBid(autoBidVM);
		User user = userService.getByUsername(autoBidVM.getUsername());
		automaticBid.setUserId(user.getId());		
		AutomaticBid retruned = automaticBidRepository.save(automaticBid);
		return new GenericResponse("Automatic bidding creating is success.");
	}
	
}
