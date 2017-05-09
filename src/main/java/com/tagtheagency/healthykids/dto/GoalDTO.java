package com.tagtheagency.healthykids.dto;

import com.tagtheagency.healthykids.model.Goal;
import com.tagtheagency.healthykids.model.Target;

public class GoalDTO {

	private int id;
	private String goal;
	private Target target;
	private boolean selected;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGoal() {
		return goal;
	}
	public void setGoal(String Goal) {
		this.goal = Goal;
	}

	public static GoalDTO createFrom(Goal goal) {
		GoalDTO dto = new GoalDTO();
		dto.setId(goal.getId());
		dto.setGoal(goal.getGoal());
		dto.selected = goal.isSelected();
		dto.target = goal.getTarget();
		return dto;
	}
	
	public Target getTarget() {
		return target;
	}
	public boolean isSelected() {
		return selected;
	}





}
