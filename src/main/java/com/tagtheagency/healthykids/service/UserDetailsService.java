package com.tagtheagency.healthykids.service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tagtheagency.healthykids.model.Account;
import com.tagtheagency.healthykids.persistence.AccountDAO;

@Service(value="healthKidsUserDetailsService")
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

	@Autowired
	AccountDAO accountDao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("User details service loading a username "+username);
		List<Account> accounts = accountDao.findByEmail(username);
		if (accounts.isEmpty()) {
			throw new UsernameNotFoundException("Username not found");
		}
		Account account = accounts.get(0);
		System.out.println("Found an account "+account);
		UserDetails userDetails = new UserDetails() {
			
			private static final long serialVersionUID = 1084814500320805342L;

			@Override
			public boolean isEnabled() {
				return true;
			}
			
			@Override
			public boolean isCredentialsNonExpired() {
				return true;
			}
			
			@Override
			public boolean isAccountNonLocked() {
				return true;
			}
			
			@Override
			public boolean isAccountNonExpired() {
				return true;
			}
			
			@Override
			public String getUsername() {
				return account.getEmail();
			}
			
			@Override
			public String getPassword() {
				return account.getPassword();
			}
			
			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				return Collections.emptySet();
			}
		};
		return userDetails;
	}

}
