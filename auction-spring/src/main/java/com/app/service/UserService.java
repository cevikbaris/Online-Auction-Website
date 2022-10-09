package com.app.service;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import com.app.repository.AuctionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.dto.UserUpdateRequest;
import com.app.dto.UserRequest;
import com.app.entity.User;
import com.app.error.NotFoundException;
import com.app.repository.IdentityFileRepository;
import com.app.repository.RoleRepository;
import com.app.repository.UserRepository;


@Service
public class UserService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final RoleRepository roleRepository;
	private final FileService fileService;
	private final IdentityFileRepository identityFileRepository;
	private final AuctionRepository auctionRepository;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository,
					   FileService fileService, IdentityFileRepository identityFileRepository, AuctionRepository auctionRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.roleRepository = roleRepository;
		this.fileService = fileService;
		this.identityFileRepository=identityFileRepository;
		this.auctionRepository = auctionRepository;
	}

	public void save(UserRequest userWithoutRole) {
		User user = new User(userWithoutRole);
		user.setPassword(this.passwordEncoder.encode(userWithoutRole.getPassword()));
		user.setRoles(Collections.singletonList(roleRepository.findByName("ROLE_USER")));
		userRepository.save(user);
	}

	public Page<User> getUsers(Pageable page, User user) {
		if(user != null) {
			return userRepository.findByUsernameNot(user.getUsername(),page);
		}
		return userRepository.findAll(page);
	}

	public User getByUsername( String username) {
		return userRepository.findByUsername(username).orElseThrow(NotFoundException::new);

	}

	public User updateUser(String username, UserUpdateRequest updatedUser) {
		Optional<User> optionalUser = userRepository.findByUsername(username);
		User user= optionalUser.orElseThrow(NotFoundException::new);
		user.setName(updatedUser.getName());
		if(updatedUser.getImage()!=null) {
			String oldImageName = user.getImage();
			try {
				String storedFileName = fileService.writeBase64EncodedStringToFile(updatedUser.getImage());
				user.setImage(storedFileName);
			} catch (IOException e) {
				e.printStackTrace();
			}
			fileService.deleteProfileImage(oldImageName);
		}
		return userRepository.save(user);
		
	}

	public User getUserOfAuction(int id) {
		String username = auctionRepository.usernameOfAuctionOwner(id);
		return getByUsername(username);
	}

	public User findById(long buyerId) {
		return userRepository.findById(buyerId).orElseThrow(NotFoundException::new);
	}

	public void updateUserIsApproved(long userid) {
		 userRepository.updateUserApproved(userid);
	}

	public void deleteIdentityByUserId(long userid) {
		identityFileRepository.deleteIdentityFile(userid);
	}

	public void deleteUser(User user) {
		userRepository.delete(user);
	}
	
	
}
