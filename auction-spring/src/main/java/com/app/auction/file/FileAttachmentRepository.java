package com.app.auction.file;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileAttachmentRepository extends JpaRepository<FileAttachment, Long>{
	
	List<FileAttachment> findByDateBeforeAndAuctionIsNull(Date date);

	//fileAttachment -> hoax -> user
	//List<FileAttachment> findByHoaxUser(User inDB);


}
