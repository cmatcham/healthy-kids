package com.tagtheagency.healthykids.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tagtheagency.healthykids.model.Achievement;

public class AchievementDTO {

	private String date;
	protected boolean sleep;
	protected boolean nutrition;
	protected boolean movement;
	
	//@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		AchievementDTO dto = new AchievementDTO();
		dto.date = sdf.format(a.getDate());
		dto.nutrition = a.isNutrition();
		dto.movement = a.isMovement();
		dto.sleep = a.isSleep();
		return dto;
	}
	
	public static class None extends AchievementDTO {
		public None(Date date) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sleep = false;
			nutrition = false;
			movement = false;
			setDate(sdf.format(date));
		}
	}
	@Override
	public String toString() {
		return "Date: "+date+"; Movement: "+movement+"; Nutrition: "+nutrition+"; Sleep: "+sleep;
	}
	
	
}
