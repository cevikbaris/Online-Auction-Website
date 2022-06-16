package com.app.auction.user;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.auction.error.NotFoundException;
import com.app.auction.file.FileService;
import com.app.auction.file.IdentityFileRepository;
import com.app.auction.role.RoleRepository;
import com.app.auction.user.vm.UserUpdateVM;


@Service
public class UserService {
	
	UserRepository userRepository;
	
	PasswordEncoder passwordEncoder;
	
	RoleRepository roleRepository;
	
	FileService fileService;
	
	IdentityFileRepository identityFileRepository;
	
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,RoleRepository roleRepository,
					   FileService fileService, IdentityFileRepository identityFileRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.roleRepository = roleRepository;
		this.fileService = fileService;
		this.identityFileRepository=identityFileRepository;
	}

	public void save(UserWithoutRole userWithoutRole) {
		User user = new User(userWithoutRole);
		user.setPassword(this.passwordEncoder.encode(userWithoutRole.getPassword()));
		user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));

		userRepository.save(user);
	}

	public Page<User> getUsers(Pageable page, User user) {
		if(user != null) {
			return userRepository.findByUsernameNot(user.getUsername(),page);
		}
		return userRepository.findAll(page);
	}

	public User getByUsername( String username) {
		User user =userRepository.findByUsername(username);
		if(user ==null) {
			throw new NotFoundException();//get 404 not fund
		}
		return user;
	}

	public User updateUser(String username, UserUpdateVM updatedUser) {
		User user = userRepository.findByUsername(username);
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

	public User findById(int buyerId) {
		User user =userRepository.findById(buyerId);
		return user;
		
	}
	public User findById(long buyerId) {
		long id=buyerId;
		User user =userRepository.findById(id);
		return user;
		
	}

	public void updateUserIsApproved(long userid) {
		System.out.println(userid);
		 userRepository.updateUserApproved(userid);
	}

	public void deleteIdentityByUserId(long userid) {
		identityFileRepository.deleteIdentityFile(userid);
	}

	public void deleteUser(User user) {
		userRepository.delete(user);
	}
	
	
}
