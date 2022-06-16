package com.app.auction.auction;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.app.auction.bid.Bid;
import com.app.auction.bid.BidRepository;
import com.app.auction.category.Category;
import com.app.auction.category.CategoryRepository;
import com.app.auction.category.CategoryService;
import com.app.auction.error.NotFoundException;
import com.app.auction.file.FileAttachment;
import com.app.auction.file.FileAttachmentRepository;
import com.app.auction.shared.MailNotification;
import com.app.auction.user.User;
import com.app.auction.user.UserService;

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
	MailNotification mailNotification;

	@Autowired
	CategoryService categoryService;
	
	public int save( AuctionVM auctionVM, User owner) {
		Auction auction = new Auction(auctionVM);
		auction.setCreator(owner);
		Category category = categoryService.findById(auctionVM.getCategory());
		auction.setCategory(category);
		
		int id =  auctionRepository.save(auction).getId();
		
		Optional<FileAttachment> optionalFileAttachment = fileAttachmentRepository.findById(auctionVM.getAttachmentId());

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
		Auction auction=  auctionRepository.findById(id);
		if(auction == null) {
			throw new NotFoundException();
		}
		return auction;
	}


	public User getUserOfAuction(int id) {
		String username = auctionRepository.usernameOfAuctionOwner(id);
		return userService.getByUsername(username);
	}


	public List<Auction> getAuctions(long id) {
		List<Integer> idList=auctionRepository.getProductIdsOfUser(id);
	
		if(idList.get(0)!=null) {
			List<Auction> auctions = new ArrayList<Auction>();
			for(Integer idx: idList) {
				
				auctions.add(auctionRepository.findById(idx.intValue()));
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
			Auction auction = auctionRepository.findById(id.intValue());
			int buyerID= bidRepository.findHighestBuyerId(auction.getId());
			User user= userService.findById(buyerID);
			auction.setBuyer(user);
			auctionRepository.save(auction);
			try{
				mailNotification.sendNotification(user.getEmail(),
						"You are Winner. Check the auction: http://localhost:3000/#/auction/"+auction.getId() ,
						"You Won the Auction !!! ");
			}catch(MailException e) {
				System.out.println(e.toString());
			}
		}			
	}


	public List<Auction> getMyWonAuctions(long id) {
		int myid=(int)id;
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
