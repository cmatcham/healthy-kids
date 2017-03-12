package com.tagtheagency.healthykids.dto;

import com.tagtheagency.healthykids.model.Account;

public class AccountDTO {

	private String email;
	private CharSequence password;
	private int id;
	private String token;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public CharSequence getPassword() {
		return password;
	}
	public void setPassword(CharSequence password) {
		this.password = password;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public static AccountDTO createFrom(Account account) {
		AccountDTO dto = new AccountDTO();
		dto.setEmail(account.getEmail());
		dto.setId(account.getId());
		dto.setPassword("redacted");
		return dto;
	}
	
	

	
	
}
