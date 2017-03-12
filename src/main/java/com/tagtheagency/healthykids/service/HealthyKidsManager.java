package com.tagtheagency.healthykids.service;

import java.util.Date;
import java.util.List;

import com.tagtheagency.healthykids.model.Account;
import com.tagtheagency.healthykids.model.Achievement;
import com.tagtheagency.healthykids.model.Child;
import com.tagtheagency.healthykids.model.Target;
import com.tagtheagency.healthykids.service.exception.DuplicateAccountException;

public interface HealthyKidsManager {


	
	public Account createAccount(String email, CharSequence password) throws DuplicateAccountException;
	
	public Account findByEmail(String email);
	
	public void setAchievement(Account account, Child child, Target target, Date date) throws UnauthorisedException;

	public Child createChild(Account account, String firstName, String lastName, Date dateOfBirth);

	List<Achievement> getWeeklyAchievements(Child child, Date date);

	public List<Child> getChildren(Account account);
	
}
