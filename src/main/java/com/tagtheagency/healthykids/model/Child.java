package com.tagtheagency.healthykids.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="child")
public class Child {

	private int id;
	private Account account;
	private String firstName;
	private String lastName;
	private String sticker;
	private Date dateOfBirth;
	private List<Achievement> achievements;
	private List<Reward> customRewards;
	
	@Id
	@GeneratedValue
	@Column(name="child_id")
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_id", nullable = false)
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	
	@Column(name="date_of_birth")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="dd/MM/yyyy")
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
	@Column(name="first_name")
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	@Column(name="last_name")
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="child")
	public List<Achievement> getAchievements() {
		return achievements;
	}
	
	public void setAchievements(List<Achievement> achievements) {
		this.achievements = achievements;
	}
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="child")
	public List<Reward> getCustomRewards() {
		return customRewards;
	}
	
	public void setCustomRewards(List<Reward> rewards) {
		this.customRewards = rewards;
	}
	
	@Column(name="sticker")
	public String getSticker() {
		return sticker;
	}
	
	public void setSticker(String sticker) {
		this.sticker = sticker;
	}
}

