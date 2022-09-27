package com.app.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.dto.UserUpdateDto;
import com.app.dto.UserWithoutRoleDto;
import com.app.entity.User;
import com.app.error.NotFoundException;
import com.app.repository.IdentityFileRepository;
import com.app.repository.RoleRepository;
import com.app.repository.UserRepository;


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

	public void save(UserWithoutRoleDto userWithoutRole) {
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

	public User updateUser(String username, UserUpdateDto updatedUser) {
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
