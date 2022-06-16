package com.app.auction.shared;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailNotification {
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	public void sendNotification(String email, String message, String subject) 
			throws MailException{
		
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setFrom("bariscevik212@gmail.com");
		mail.setTo(email);
		mail.setSubject(subject);
		mail.setText(message);
		
		javaMailSender.send(mail);
	}
	
}
