package com.app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.model.IdentityDto;
import com.app.entity.FileAttachment;
import com.app.entity.IdentityFile;
import com.app.entity.User;
import com.app.repository.IdentityFileRepository;
import com.app.service.FileService;
import com.app.service.UserService;

@RestController
public class IdentityController {
	
	@Autowired
	FileService	fileService;
	@Autowired
	IdentityFileRepository identityFileRepository;
	@Autowired
	UserService userService;
	
	
	//attachment.append('file', file); JS de formData ismi file: olduğundan parametre adını file yaptık spring bunu kullanıyor yoksa empty hatası veriyor
	@PostMapping("/auction-attachments")
	public FileAttachment saveAuctionAttachment(MultipartFile file) {
		return fileService.saveAuctionAttachment(file);
	}
	
	@PostMapping("/identity-attachment")
	public ResponseEntity<?> saveIdentity(MultipartFile file) {
		return fileService.saveIdentityAttachment(file);
	}
	
	@PutMapping("/identity")
	public ResponseEntity<?> saveIdentity(@RequestBody IdentityDto identityVM) {
		
		Optional<IdentityFile> idFileOptional = identityFileRepository.findById(identityVM.getImageId());
		if(idFileOptional.isPresent()) {
			User user = userService.getByUsername(identityVM.getLoggedInUsername());
			IdentityFile idFile = idFileOptional.get();
			idFile.setIdNumber(identityVM.getIdNumber());
			
			idFile.setUser(user);
			try {				
				IdentityFile idReturn = identityFileRepository.save(idFile);
				return ResponseEntity.ok().body(idReturn);
			}catch(Exception ex) {
				System.out.println(ex.toString());
				return ResponseEntity.badRequest().body("User has uploaded identity before.");				
			}
		}else {
			return ResponseEntity.badRequest().body("Uploading failed. ");
		}	
	}
	
	@GetMapping("/identity")
	public List<IdentityFile> getAllIdentities() {
		List<IdentityFile> ids =  identityFileRepository.getAllIdentityFiles();
		if(!ids.isEmpty()) {
			return ids;
		}
		return null;
	}
	
	@GetMapping("/identity/{id}")
	public IdentityFile getIdentityById(@PathVariable long id) {
		return identityFileRepository.findByIdNumber(id);
	}
	
	@DeleteMapping("/identity/user/{userid}")
	void deleteIdentityByUserId(@PathVariable long userid) {
		identityFileRepository.deleteIdentityFile(userid);
	}
	
}


















