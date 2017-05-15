package com.tagtheagency.healthykids.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.tagtheagency.healthykids.dto.GoalDTO;
import com.tagtheagency.healthykids.dto.SummaryDTO;
import com.tagtheagency.healthykids.model.Account;
import com.tagtheagency.healthykids.model.Achievement;
import com.tagtheagency.healthykids.model.Child;
import com.tagtheagency.healthykids.model.Goal;
import com.tagtheagency.healthykids.model.Target;
import com.tagtheagency.healthykids.service.exception.DuplicateAccountException;

public interface HealthyKidsManager {


	
	public Account createAccount(String email, CharSequence password) throws DuplicateAccountException;
	
	public Account findByEmail(String email);
	
	public void setAchievement(Account account, Child child, Target target, Date date, boolean set) throws UnauthorisedException;

	public Child createChild(Account account, String firstName, int age, String sticker);

	public Child updateChild(Child child, String firstName, int age, String sticker);

	List<Achievement> getWeeklyAchievements(Child child, Date date);

	public List<Child> getChildren(Account account);

	public List<LocalDate> getWeekOf(Date date);
	
	public List<String> getStickers();

	public void setSticker(Child child, String sticker);

	public void addCustomReward(Child child, Map<Integer, String> rewards);

	public int addCustomGoal(Child child, GoalDTO goal);

	public void editCustomGoal(Child child, GoalDTO goal);
	
	public SummaryDTO getSummary(Child child);

}
