package com.app.model;

import com.app.entity.Auction;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuctionResponse extends BaseAuctionResponse{
    private int id;

    public AuctionResponse(Auction auction) {
        super(auction);
        setId(auction.getId());
    }
}
