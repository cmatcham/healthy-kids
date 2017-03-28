package com.tagtheagency.healthykids.dto;

import com.tagtheagency.healthykids.model.Reward;

public class RewardDTO {

	private int id;
	private String reward;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getReward() {
		return reward;
	}
	public void setReward(String reward) {
		this.reward = reward;
	}

	public static RewardDTO createFrom(Reward reward) {
		RewardDTO dto = new RewardDTO();
		dto.setId(reward.getId());
		dto.setReward(reward.getReward());
		return dto;
	}





}
