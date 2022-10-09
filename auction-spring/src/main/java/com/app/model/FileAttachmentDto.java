package com.app.model;

import com.app.entity.FileAttachment;

import lombok.Data;

@Data
public class FileAttachmentDto {
	
	private String name;
	
	private String fileType;

	public FileAttachmentDto(FileAttachment fileAttachment) {
		this.setName(fileAttachment.getName());
		this.setFileType(fileAttachment.getFileType());
	}

}
