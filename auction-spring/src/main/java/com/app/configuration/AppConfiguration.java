package com.app.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties
public class AppConfiguration {
	private String uploadPath;
	
	private String profileStorage = "profile";
	
	private String attachmentStorage = "attachments";
	
	private String identityStorage = "identity";
	
	public String getProfileStoragePath() {
		return uploadPath+ "/" +profileStorage;
	}
	
	public String getAttachmentStoragePath() {
		return uploadPath+ "/" +attachmentStorage;
	}

	public String getIdentityStoragePath() {
		return uploadPath+ "/" +identityStorage;
	}

	
}
