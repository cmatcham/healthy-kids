package com.tagtheagency.healthykids.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="password_reset")
public class PasswordReset {

	private int id;
	private String email;
	private String localCode;
	private String remoteCode;
	
	@Id
	@GeneratedValue
	@Column(name="reset_id")
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

	@Column(name="local_code")
	public String getLocalCode() {
		return localCode;
	}
	
	public void setLocalCode(String localCode) {
		this.localCode = localCode;
	}
	
	@Column(name="remote_code")
	public String getRemoteCode() {
		return remoteCode;
	}
	
	public void setRemoteCode(String remoteCode) {
		this.remoteCode = remoteCode;
	}
	
}

