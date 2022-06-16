package com.app.auction.shared;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.app.auction.file.FileService;

public class FileTypeValidator implements ConstraintValidator<FileType, String>{

	@Autowired
	FileService fileService;
	
	String[] types;
	
	@Override //ANNOTATIONDAKI VERILERI CEKMEK ICIN
	public void initialize(FileType constraintAnnotation) {
		this.types = constraintAnnotation.types();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value == null || value.isEmpty()) {
			return true;
		}
		String fileType = fileService.detectType(value);
		
		for(String supportedTypes: this.types) {
			if(fileType.contains(supportedTypes)) {
				return true; 
			}
		}

		return false;
	}


}
