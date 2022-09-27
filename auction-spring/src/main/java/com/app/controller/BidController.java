package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.AutoBidDto;
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
	GenericResponse createAutoBidding (@RequestBody AutoBidDto autoBidVM) {
		
		AutomaticBid automaticBid = new AutomaticBid(autoBidVM);
		User user = userService.getByUsername(autoBidVM.getUsername());
		automaticBid.setUserId(user.getId());		
		AutomaticBid retruned = automaticBidRepository.save(automaticBid);
		return new GenericResponse("Automatic bidding creating is success.");
	}
	
}
