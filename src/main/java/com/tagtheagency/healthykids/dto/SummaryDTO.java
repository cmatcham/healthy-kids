package com.tagtheagency.healthykids.dto;

public class SummaryDTO {

	private long totalSleep;
	private long totalMovement;
	private long totalNutrition;
	
	public long getTotalMovement() {
		return totalMovement;
	}
	public long getTotalNutrition() {
		return totalNutrition;
	}
	public long getTotalSleep() {
		return totalSleep;
	}
	public void setTotalMovement(long totalMovement) {
		this.totalMovement = totalMovement;
	}
	public void setTotalNutrition(long totalNutrition) {
		this.totalNutrition = totalNutrition;
	}
	public void setTotalSleep(long totalSleep) {
		this.totalSleep = totalSleep;
	}
}
