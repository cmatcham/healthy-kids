package com.tagtheagency.healthykids.model;

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

@Entity
@Table(name="child")
public class Child {

	private int id;
	private Account account;
	private String name;
	private String sticker;
	private Integer age;
	private List<Achievement> achievements;
	private List<Reward> customRewards;
	private List<Goal> customGoals;
	
	private Integer defaultSleepGoal;
	private Integer defaultNutritionGoal;
	private Integer defaultMovementGoal;
	private Integer defaultSleepReward;
	private Integer defaultNutritionReward;
	private Integer defaultMovementReward;
	
	
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
	
/*	@Column(name="date_of_birth")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="dd/MM/yyyy")
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
*/
	
	@Column(name="age")
	public Integer getAge() {
		return age;
	}
	
	public void setAge(Integer age) {
		this.age = age;
	}
	
	@Column(name="first_name")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
/*	@Column(name="last_name")
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}*/
	
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
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="child")
	public List<Goal> getCustomGoals() {
		return customGoals;
	}
	
	@Column(name="sticker")
	public String getSticker() {
		return sticker;
	}
	
	public void setSticker(String sticker) {
		this.sticker = sticker;
	}
	public void setCustomGoals(List<Goal> customGoals) {
		this.customGoals = customGoals;
	}

	@Column
	public Integer getDefaultSleepGoal() {
		return defaultSleepGoal;
	}

	public void setDefaultSleepGoal(Integer defaultSleepGoal) {
		this.defaultSleepGoal = defaultSleepGoal;
	}

	@Column
	public Integer getDefaultNutritionGoal() {
		return defaultNutritionGoal;
	}

	public void setDefaultNutritionGoal(Integer defaultNutritionGoal) {
		this.defaultNutritionGoal = defaultNutritionGoal;
	}

	@Column
	public Integer getDefaultMovementGoal() {
		return defaultMovementGoal;
	}

	public void setDefaultMovementGoal(Integer defaultMovementGoal) {
		this.defaultMovementGoal = defaultMovementGoal;
	}

	@Column
	public Integer getDefaultSleepReward() {
		return defaultSleepReward;
	}

	public void setDefaultSleepReward(Integer defaultSleepReward) {
		this.defaultSleepReward = defaultSleepReward;
	}

	@Column
	public Integer getDefaultNutritionReward() {
		return defaultNutritionReward;
	}

	public void setDefaultNutritionReward(Integer defaultNutritionReward) {
		this.defaultNutritionReward = defaultNutritionReward;
	}

	@Column
	public Integer getDefaultMovementReward() {
		return defaultMovementReward;
	}

	public void setDefaultMovementReward(Integer defaultMovementReward) {
		this.defaultMovementReward = defaultMovementReward;
	}
	
	
}

