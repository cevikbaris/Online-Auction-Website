package com.app.auction.configuration;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfigurer implements WebMvcConfigurer {

	@Autowired
	AppConfiguration appConfiguration;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/images/**")
		.addResourceLocations("file:./"+appConfiguration.getUploadPath() +"/");
		//.setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));
	}
	@Bean
	CommandLineRunner createStorageDirectories() {
		return (args) -> {
			createFolder(appConfiguration.getUploadPath());
			createFolder(appConfiguration.getProfileStoragePath());
			createFolder(appConfiguration.getAttachmentStoragePath());
			createFolder(appConfiguration.getIdentityStoragePath());
		};
	}

	private void createFolder(String path) {
		File folder = new File(path);
		boolean folderExist = folder.exists() && folder.isDirectory();
		if(!folderExist) {
			folder.mkdir();
		}
	}

}
