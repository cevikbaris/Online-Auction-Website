package com.app.auction.error;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)//null olan degerleri eklemicez sonuca
public class ApiError {

	private int Status;
	
	private String message;
	
	private String path;
	
	private long timestamp = new Date().getTime();
	
	private Map<String,String> validationErrors;

	public ApiError(int status, String message, String path) {
		Status = status;
		this.message = message;
		this.path = path;
	}
	
	
	
}
