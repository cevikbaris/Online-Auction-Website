package com.app.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.app.dto.AuctionRequest;
import com.app.entity.Auction;
import com.app.entity.Bid;
import com.app.entity.Category;
import com.app.entity.FileAttachment;
import com.app.entity.User;
import com.app.error.NotFoundException;
import com.app.repository.AuctionRepository;
import com.app.repository.BidRepository;
import com.app.repository.FileAttachmentRepository;

@Service
@EnableScheduling
public class AuctionService {

	@Autowired
	AuctionRepository auctionRepository;
	
	@Autowired
	UserService userService;

	@Autowired
	FileAttachmentRepository fileAttachmentRepository;
	
	@Autowired
	BidRepository bidRepository;
	
	@Autowired
	MailNotificationService mailNotification;

	@Autowired
	CategoryService categoryService;
	
	public int save(AuctionRequest auctionRequest, User owner) {
		Auction auction = new Auction(auctionRequest);
		auction.setCreator(owner);
		Category category = categoryService.findById(auctionRequest.getCategory());
		auction.setCategory(category);
		
		int id =  auctionRepository.save(auction).getId();
		
		Optional<FileAttachment> optionalFileAttachment = fileAttachmentRepository.findById(auctionRequest.getAttachmentId());

		if(optionalFileAttachment.isPresent()) {
			FileAttachment fileAttachment = optionalFileAttachment.get();
			fileAttachment.setAuction(auction);
			fileAttachmentRepository.save(fileAttachment);
		}
		return id;
	}


	public Page<Auction> getAuctions(Pageable page) {
		return auctionRepository.findAll(page);
	}

	public Auction findById(int id) {
		return	auctionRepository.findById(id).orElseThrow(NotFoundException::new);
	}


	public List<Auction> getUserAuctions(long id)  {
		List<Integer> idList=auctionRepository.getProductIdsOfUser(id); // TODO: 9.10.2022 null dönebilir hata atması zorunlu mu 
		
		if(idList.get(0)!=null) { // TODO: 9.10.2022 list !=null try catch kullanılmalı mı buralarda? 
			List<Auction> auctions = new ArrayList<>();
			for(Integer idx: idList) {
				auctions.add(auctionRepository.findById(idx).orElseThrow(NotFoundException::new) );
			}
			return auctions;
		}else {
			throw new NotFoundException();
		}
	}

	@Scheduled(fixedRate = 10*1000)
	public void updateAuction() {
		List<Integer> ids = auctionRepository.getIdsBiddedAuctions();//if there is a bid in auction and if there don't have buyer yet
		
		for(Integer id : ids) {//update all auctions. Find auction - winner user - set winner user to auction.
			Auction auction = auctionRepository.findById(id).orElseThrow(NotFoundException::new); // TODO: 9.10.2022 exceptionsız çözlür mü 
			int buyerID= bidRepository.findHighestBuyerId(auction.getId());
			User user= userService.findById(buyerID);
			auction.setBuyer(user);
			auctionRepository.save(auction);
			try{
				mailNotification.sendNotification(user.getEmail(),
						"You are Winner. Check the auction: http://localhost:3000/#/auction/"+auction.getId() ,
						"You Won the Auction !!! ");
			}catch(MailException e) {
				e.printStackTrace();
			}
		}			
	}


	public List<Auction> getMyWonAuctions(long id) {
		int myid=(int)id; // TODO: 9.10.2022 user id neden int e çevirildi
		return auctionRepository.getMyWonAuctions( myid);
	}

	@Scheduled(fixedRate = 20*1000)
	public void sendMail() {
		
		HashSet<Long> idListForEmails = new HashSet<Long>();
		
		List<Auction> nonFinishedAuctions =  auctionRepository.getNotFinishedAuctions();
		if(!nonFinishedAuctions.isEmpty()) {
			for(Auction auction : nonFinishedAuctions) {
				List<Bid> bids = bidRepository.getLast2minBiddersByIdOrderByPriceDesc(auction.getId());
				if(!bids.isEmpty()) {
					for(Bid bid : bids) {
						if(bid.getBidder().getId() != bids.get(0).getBidder().getId() ) { //if not equal highest bid . First bid has highest price!
							idListForEmails.add((long)bid.getBidder().getId()); 
						}
					}
				}
			}
		}
		
		if(idListForEmails.size()>0) {
			 Iterator<Long> value = idListForEmails.iterator();
			 while(value.hasNext()) {

				User user = userService.findById(value.next());
				if(user!=null) {
					System.out.println(user.getEmail());
				}else {
					System.out.println("user null");
				}
				try{
					mailNotification.sendNotification(user.getEmail(),
							"Your last bid has been passed. Please check auction.",
							"Auction Notification ");
				}catch(MailException e) {
					System.out.println(e.toString());
				}	
			}
		}
	}


	public void save(Auction auction) {
		auctionRepository.save(auction);
		
	}


	public List<Auction> findByCategoryId(int categoryId) {
		return auctionRepository.findByCategoryId(categoryId);
	}


	public void buyNow(int auctionId, Date bidTime, long buyerId) {
		auctionRepository.updateAuctionWithBuyNow( auctionId, bidTime, buyerId);
	}

	
	
	
	
}
