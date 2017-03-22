package com.tagtheagency.healthykids.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="account")
public class Account {

	private int id;
	private String email;
	private String password;
	private List<Child> children;
	
	@Id
	@GeneratedValue
	@Column(name="account_id")
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(unique=true)
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="account")
	public List<Child> getChildren() {
		return children;
	}
	
	public void setChildren(List<Child> children) {
		this.children = children;
	}
	
//	/**
//	 * Set the password on this account.  This will be hashed before being stored in the db.
//	 * @param rawPassword
//	 */
//	public void setRawPassword(byte[] rawPassword) {
//		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//		String hashedPassword = passwordEncoder.encode(new String(rawPassword, StandardCharsets.UTF_8));
//		this.password = hashedPassword;
//	}
//	
//	public boolean matchesPassword(byte[] rawPassword) {
//		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//		return passwordEncoder.matches(new String(rawPassword, StandardCharsets.UTF_8), this.password);
//	}
	
	
}

