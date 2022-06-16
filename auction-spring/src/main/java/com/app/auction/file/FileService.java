package com.app.auction.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.tika.Tika;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.auction.configuration.AppConfiguration;

@Service
@EnableScheduling
public class FileService {

	IdentityFileRepository identifyFileRepository;
	
	AppConfiguration appConfiguration;
	
	FileAttachmentRepository fileAttachmentRepository;
	
	Tika tika ;
	public FileService(AppConfiguration appConfiguration,FileAttachmentRepository fileAttachmentRepository
				,IdentityFileRepository identifyFileRepository) {
		this.tika = new Tika();//dosya kontrolü icin jpeg gif vs..
		this.appConfiguration=appConfiguration;
		this.fileAttachmentRepository=fileAttachmentRepository;
		this.identifyFileRepository=identifyFileRepository;
	}

	

	
	public String writeBase64EncodedStringToFile(String image) throws IOException {
		String fileName = generateRandomName();
		File target = new File(appConfiguration.getProfileStoragePath() + "/" + fileName);
		OutputStream outputStream = new FileOutputStream(target);

		byte[] base64enoded = Base64.getDecoder().decode(image);

		outputStream.write(base64enoded);
		outputStream.close();
		return fileName;
	}

	//remove - from random name
	public String generateRandomName() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	
	
	public void deleteProfileImage(String oldImageName) {
		if(oldImageName == null) {
			return;
		}
		deleteFile(Paths.get(appConfiguration.getUploadPath(), oldImageName));
	}
	
	
	
	private void deleteFile(Path path) {
		try {
			Files.deleteIfExists(path);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	public String detectType(String value) {
		
		byte[] base64encoded = Base64.getDecoder().decode(value);
		return tika.detect(base64encoded);
	}
	
	
	public String detectType(byte[] arr) {
		return tika.detect(arr);
	}



	public FileAttachment saveAuctionAttachment(MultipartFile multipartFile) {
		String fileName = generateRandomName();
		File target = new File(appConfiguration.getAttachmentStoragePath() + "/" + fileName);
		String fileType =null;
		try {
			byte[] arr = multipartFile.getBytes();
			OutputStream outputStream = new FileOutputStream(target);
			outputStream.write(arr);
			outputStream.close();
			fileType = detectType(arr);

		} catch (IOException e) {
			e.printStackTrace();
		}
		FileAttachment attachment = new FileAttachment();
		attachment.setName(fileName);
		attachment.setDate(new Date());
		attachment.setFileType(fileType);
		return fileAttachmentRepository.save(attachment);		
	}
	

	public ResponseEntity<?> saveIdentityAttachment(MultipartFile multipartFile) {
		String fileName = generateRandomName();
		File target = new File(appConfiguration.getIdentityStoragePath() + "/" + fileName);
		String fileType=null;
		try {
			byte[] arr = multipartFile.getBytes();
			OutputStream outputStream = new FileOutputStream(target);
			outputStream.write(arr);
			outputStream.close();
			fileType = detectType(arr);

		} catch (IOException e) {
			e.printStackTrace();
		}
		IdentityFile attachment = new IdentityFile();
		attachment.setImageId(fileName);
		attachment.setDate(new Date());
		if(fileType.contains("jpeg") || fileType.contains("png") ) {
			attachment.setFileType(fileType);			
		}else {
			return ResponseEntity.badRequest().body("Invalid file type. Upload png or jpeg.");
		}
		return ResponseEntity.ok().body(identifyFileRepository.save(attachment));	
	
	}

	
	
	
	@Scheduled(fixedRate = 60*1000)
	public void cleanupStorage() {
		Date twentyFourHoursAgo = new Date(System.currentTimeMillis() - (10* 60 * 1000));
		List<FileAttachment> filesToBeDeleted = fileAttachmentRepository.findByDateBeforeAndAuctionIsNull(twentyFourHoursAgo);
		if(!filesToBeDeleted.isEmpty()) {			
			for(FileAttachment file : filesToBeDeleted) {
				//delete from file
				deleteAttachmentFile(file.getName());//this sevice's method
				//delete from db
				fileAttachmentRepository.deleteById(file.getId());
			}
		}
	}
	
	@Scheduled(fixedRate= 5*60*1000)
	public void cleanupId() {
		List<IdentityFile> filesWillDelete = identifyFileRepository.getFilesMoreThan10MinAndNullUser();
		if(!filesWillDelete.isEmpty()) {
			for(IdentityFile file : filesWillDelete) {
				deleteIdentityFile(null);
				identifyFileRepository.deleteById(file.getId());
			}
		}
	}
	
	public void deleteAttachmentFile(String oldImageName) {
		if(oldImageName == null) {
			return;
		}
		deleteFile(Paths.get(appConfiguration.getAttachmentStoragePath(), oldImageName ));
	}
	
	public void deleteIdentityFile(String oldImageName) {
		if(oldImageName == null) {
			return;
		}
		deleteFile(Paths.get(appConfiguration.getIdentityStoragePath(), oldImageName ));
	}







	/*
	public void deleteAllStoredFilesForUser(User inDB) {
		deleteProfileImage(inDB.getImage());
		List<FileAttachment> filesToBeRemoved = fileAttachmentRepository.findByHoaxUser(inDB);
		for(FileAttachment file : filesToBeRemoved) {
			deleteAttachmentFile(file.getName());
		}
	}
	*/
}
