package com.app.configuration;

import java.util.Collection;
import java.util.stream.Collectors;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.entity.Role;
import com.app.entity.User;
import com.app.repository.UserRepository;


@Service
public class UserAuthService implements UserDetailsService{

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username); //my user implements UserDetails it can return
		 if(user != null && user.isEnabled()){
			return  buildUserForAuthentication(user, mapRolesToAuthorities(user.getRoles()));
		}else {
			throw new UsernameNotFoundException("User not found");
		}
	}
	
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());
	}
	
	UserDetails buildUserForAuthentication(User user, Collection<? extends GrantedAuthority> collection) {
		  return new org.springframework.security.core.userdetails.User(user.getUsername(),
		    user.getPassword(), collection);
	}
	
}
