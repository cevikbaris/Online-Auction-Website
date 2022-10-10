package com.app.model;

import com.app.entity.Auction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
@Setter
@Getter
public class BaseAuctionResponse {

    private String explanation;
    private String title;
    private int startPrice;
    private FileAttachmentDto fileAttachment;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    BaseAuctionResponse(){}

    public BaseAuctionResponse(Auction auction) {
        setExplanation(auction.getExplanation());
        setTitle(auction.getTitle());
        setStartPrice(auction.getPrice());
        setStartDate(auction.getStartDate());
        setEndDate(auction.getEndDate());
        if(auction.getFileAttachment() !=null) {
            setFileAttachment( new FileAttachmentDto(auction.getFileAttachment()) );
        }
    }
}
