package com.tagtheagency.healthykids.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="reward")
public class Reward {
	
	private int id;
	private Child child;
	private String reward;

	
	@Id
	@GeneratedValue
	@Column(name="reward_id")
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "child_id", nullable = false)
	public Child getChild() {
		return child;
	}

	public void setChild(Child child) {
		this.child = child;
	}
	
	@Column
	public String getReward() {
		return reward;
	}
	
	public void setReward(String reward) {
		this.reward = reward;
	}
}
