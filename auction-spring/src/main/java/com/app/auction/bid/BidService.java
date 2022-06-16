package com.app.auction.bid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class BidService {
	
	@Autowired
	BidRepository bidRepository;
	
	@Autowired
	AutomaticBidRepository automaticBidRepository;

	@Scheduled(fixedRate = 1*1000)
	void makeAutoBids() {
		List<AutomaticBid> autoBidList = automaticBidRepository.findAll();
		List<Integer> nonBiddedAuctions = automaticBidRepository.auctionIdNoBidAndAutoBidValid();
		if(!nonBiddedAuctions.isEmpty()) {
			for(Integer id : nonBiddedAuctions) {
				automaticBidRepository.ifBidIsNullAndAutoBidValid(id.intValue());
				bidRepository.updatePriceOfAuction(id.intValue());
			}
		}
		if(!autoBidList.isEmpty()) {
			for(AutomaticBid automaticBid : autoBidList) {
				automaticBidRepository.ifBidValidForAuctionBidAuto(automaticBid.getAuctionId());
				bidRepository.updatePriceOfAuction(automaticBid.getAuctionId());
			}
		}
	}
	
}
