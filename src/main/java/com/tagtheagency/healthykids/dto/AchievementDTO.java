package com.tagtheagency.healthykids.dto;

import java.util.Date;

import com.tagtheagency.healthykids.model.Achievement;

public class AchievementDTO {

	private Date date;
	private boolean sleep;
	private boolean nutrition;
	private boolean movement;
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public boolean isSleep() {
		return sleep;
	}
	public void setSleep(boolean sleep) {
		this.sleep = sleep;
	}
	public boolean isNutrition() {
		return nutrition;
	}
	public void setNutrition(boolean nutrition) {
		this.nutrition = nutrition;
	}
	public boolean isMovement() {
		return movement;
	}
	public void setMovement(boolean movement) {
		this.movement = movement;
	}
	public static AchievementDTO convertFrom(Achievement a) {
		AchievementDTO dto = new AchievementDTO();
		dto.date = a.getDate();
		dto.nutrition = a.isNutrition();
		dto.movement = a.isMovement();
		dto.sleep = a.isSleep();
		return dto;
	}
	
	
	
}
