package com.smart.config;

import com.smart.Dao.UserRepo;
import com.smart.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailServiceImplement implements UserDetailsService{
	
	@Autowired
	private UserRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//fetching user from dDatabase
		User user = userRepo.getUserByEmail(username);
		if(user == null) {
			throw new UsernameNotFoundException("Username is not found");
		}
		CustomUserDetail customUserDetail = new CustomUserDetail(user);
		return customUserDetail;
	}
	

}
