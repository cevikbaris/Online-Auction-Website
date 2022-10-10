package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.model.AutoBidRequest;
import com.app.entity.AutomaticBid;
import com.app.entity.User;
import com.app.repository.AutomaticBidRepository;
import com.app.repository.BidRepository;
import com.app.service.UserService;
import com.app.shared.GenericResponse;

@RestController
public class BidController {
	
	@Autowired
	BidRepository bidRepository;
	
	@Autowired
	AutomaticBidRepository automaticBidRepository;
	
	@Autowired
	UserService userService;
	
	@PostMapping("/auto/bid")
	GenericResponse createAutoBidding (@RequestBody AutoBidRequest autoBid) {
		AutomaticBid automaticBid = new AutomaticBid(autoBid);
		User user = userService.getByUsername(autoBid.getUsername());
		automaticBid.setUser(user);
		automaticBidRepository.save(automaticBid);
		return new GenericResponse("Automatic bidding creating is success.");
	}
	
}
