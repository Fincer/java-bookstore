// Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.service.session;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fjordtek.bookstore.model.auth.User;
import com.fjordtek.bookstore.model.auth.UserRepository;
import com.fjordtek.bookstore.model.auth.UserRole;
import com.fjordtek.bookstore.model.auth.UserRoleRepository;

/**
 *
 * This class implements Spring Framework security UserDetailsService interface,
 * adding custom methods to define proper user authorities.
 *
 * @author Pekka Helenius
 */

@Service
public class UserDetailServiceImpl implements UserDetailsService {

	private UserRepository userRepository;
	private UserRoleRepository userRoleRepository;

	@Autowired
	public void instanceAttributeUserDetailsService(
			UserRepository userRepository,
			UserRoleRepository userRoleRepository
			) {
		this.userRepository = userRepository;
		this.userRoleRepository = userRoleRepository;
	}

	/*
	 * Retrieve user roles from the USER_ROLE table
	 */
	private Collection<GrantedAuthority> getUserGrantedAuthorities(String userName) {

		User currentUser = userRepository.findByUsername(userName);

		Collection<GrantedAuthority> authorities = new ArrayList<>();

		for (UserRole role : userRoleRepository.findAll()) {

			if (role.getUser().getId() == currentUser.getId()) {
				authorities.add(
						new SimpleGrantedAuthority(role.getRole().getName())
						);
			}
		}

		return authorities;
	}

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

		User currentUser = userRepository.findByUsername(userName);

		if (currentUser == null) {
			throw new UsernameNotFoundException("");
		}

		UserDetails userDetails = new org.springframework.security.core.userdetails.User(
				userName, currentUser.getPassword(), getUserGrantedAuthorities(userName)
				);

		return userDetails;

	}

}