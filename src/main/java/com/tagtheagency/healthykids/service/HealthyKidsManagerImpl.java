package com.tagtheagency.healthykids.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tagtheagency.healthykids.dto.GoalDTO;
import com.tagtheagency.healthykids.dto.SummaryDTO;
import com.tagtheagency.healthykids.model.Account;
import com.tagtheagency.healthykids.model.Achievement;
import com.tagtheagency.healthykids.model.Child;
import com.tagtheagency.healthykids.model.Goal;
import com.tagtheagency.healthykids.model.PasswordReset;
import com.tagtheagency.healthykids.model.Reward;
import com.tagtheagency.healthykids.model.Target;
import com.tagtheagency.healthykids.persistence.AccountDAO;
import com.tagtheagency.healthykids.persistence.AchievementDAO;
import com.tagtheagency.healthykids.persistence.ChildDAO;
import com.tagtheagency.healthykids.persistence.GoalDAO;
import com.tagtheagency.healthykids.persistence.PasswordResetDAO;
import com.tagtheagency.healthykids.persistence.RewardDAO;
import com.tagtheagency.healthykids.service.exception.DuplicateAccountException;

@Service
public class HealthyKidsManagerImpl implements HealthyKidsManager {

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired AccountDAO accountDao;
	@Autowired ChildDAO childDao;
	@Autowired AchievementDAO achievementDao;
	@Autowired RewardDAO rewardDao;
	@Autowired GoalDAO goalDao;
	@Autowired PasswordResetDAO passwordResetDao;
	
	@Autowired NotificationService emailer;
	
	@Override
	public Account createAccount(String email, CharSequence password) throws DuplicateAccountException {
		try {
			Account account = new Account();
			account.setEmail(email);
			account.setPassword(passwordEncoder.encode(password));
			accountDao.save(account);
			return account;
		} catch (DataIntegrityViolationException e) {
			throw new DuplicateAccountException();
		}
	}
	
	@Override
	public Child createChild(Account account, String firstName, int age, String sticker) {
		Child child = new Child();
		child.setAccount(account);
		child.setName(firstName);
		child.setAge(age);
		child.setSticker(sticker);
		return childDao.save(child);
	}
	
	@Override
	public Child updateChild(Child child, String firstName, int age, String sticker) {
		child.setName(firstName);
		child.setAge(age);
		child.setSticker(sticker);
		return childDao.save(child);
	}
	
	@Override
	public Account findByEmail(String email) {
		List<Account> accounts = accountDao.findByEmail(email);
		if (accounts.isEmpty()) {
			return null;
		}
		return accounts.get(0);
	}
	
	@Override
	public void setAchievement(Account account, Child child, Target target, Date date, boolean set) throws UnauthorisedException {
		if (!ownedChild(account, child)) {
			throw new UnauthorisedException("That child does not belong to you!");
		}
		date = dateWithoutTime(date);
		List<Achievement> achievements = achievementDao.findByChildAndDate(child, date);
		Achievement achievement;
		if (achievements.isEmpty()) {
			achievement = new Achievement();
			achievement.setChild(child);
			achievement.setDate(date);
		} else {
			achievement = achievements.get(0);
		}
		switch (target) {
		case MOVEMENT:
			achievement.setMovement(set);
			break;
		case NUTRITION:
			achievement.setNutrition(set);
			break;
		case SLEEP:
			achievement.setSleep(set);
			break;
		}
		achievementDao.save(achievement);
		
		
	}
	
	@Override
	public List<Achievement> getWeeklyAchievements(Child child, Date date) {
		LocalDate today = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

	    LocalDate monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
	    LocalDate sunday = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

		Date from = Date.from(monday.atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date to = Date.from(sunday.atStartOfDay(ZoneId.systemDefault()).toInstant());
		return achievementDao.findByChildAndDateBetween(child, from, to);
	}

	@Override
	public List<LocalDate> getWeekOf(Date date) {
		LocalDate today = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

	    LocalDate monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
	    LocalDate sunday = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

		List<LocalDate> week = new ArrayList<LocalDate>();
		week.add(monday);
		LocalDate instance = monday;
		while (instance.isBefore(sunday)) {
			instance = instance.plusDays(1);
			week.add(instance);
		}
		return week;
	}
	
	
	private boolean ownedChild(Account account, Child child) {
		return account.getChildren().contains(child);
	}
	
	private Date dateWithoutTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	@Override
	public List<Child> getChildren(Account account) {
		return childDao.findByAccount(account);
	}
	
	@Override
	public List<String> getStickers() {
		//TODO - below code works locally, but fails on Heroku.
		//need to figure out how Heroku file system works.
		//for now just hardcode
		return Arrays.asList("sticker1.png", "sticker2.png", "sticker3.png", "sticker4.png", "sticker5.png", "sticker6.png", "sticker7.png", "sticker8.png", "sticker9.png", "sticker10.png", "sticker11.png", "sticker12.png");
		/*
		
		List<String> files = new ArrayList<>();
		try {
			
			URI uri = getClass().getResource("/static/stickers").toURI();
			Path myPath;
			if (uri.getScheme().equals("jar")) {
				FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
				myPath = fileSystem.getPath("/static/stickers");
			} else {
				myPath = Paths.get(uri);
			}
			Stream<Path> walk = Files.walk(myPath, 1);
			for (Iterator<Path> it = walk.iterator(); it.hasNext();){
				String filename = it.next().toFile().getName();
				if (filename.equals("stickers")) {
					continue;
				}
				files.add(filename);
			}
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
        
		return files;*/
	}
	
	@Override
	public void setSticker(Child child, String sticker) {
		child.setSticker(sticker);
		childDao.save(child);
	
	}
	
	@Override
	public void addCustomReward(Child child, Map<Integer, String> rewards) {
		List<Reward> currentRewards = child.getCustomRewards();
		rewards.keySet().forEach(key -> {
			if (key < 0 && currentRewards.size() >= 3) {
				return;
			}
			if (key < 0) {
				Reward reward = new Reward();
				reward.setChild(child);
				reward.setReward(rewards.get(key));
				rewardDao.save(reward);
				currentRewards.add(reward);
				return;
			}
			Reward reward = rewardDao.findOne(key);
			if (reward.getChild().getId() != child.getId()) {
				return;
			}
			reward.setReward(rewards.get(key));
			rewardDao.save(reward);
		});
	
	}
	
	@Override
	public int addCustomGoal(Child child, GoalDTO goalDto) {
		Goal goal = new Goal();
		goal.setChild(child);
		goal.setSelected(goalDto.isSelected());
		goal.setTarget(goalDto.getTarget());
		goal.setGoal(goalDto.getGoal());
		goalDao.save(goal);
		child.getCustomGoals().add(goal);
		return goal.getId();
	}
	
	@Override
	public void editCustomGoal(Child child, GoalDTO goalDto) {
		Goal goal = goalDao.findOne(goalDto.getId());
		if (goal == null || child.getId() != goal.getChild().getId()) {
			return;
		}
		goal.setSelected(goalDto.isSelected());
		goal.setTarget(goalDto.getTarget());
		goal.setGoal(goalDto.getGoal());
		goalDao.save(goal);
		
		if (goal.isSelected()) {
			for (Goal other : goalDao.findByChildAndTarget(child, goal.getTarget())) {
				if (other.getId() == goal.getId()) {
					continue;
				}
				other.setSelected(false);
				goalDao.save(other);
			}
		}
		
	}
	
	@Override
	public SummaryDTO getSummary(Child child) {
		SummaryDTO dto = new SummaryDTO();
		dto.setTotalMovement(achievementDao.getTotalMovement(child.getId()));
		dto.setTotalNutrition(achievementDao.getTotalNutrition(child.getId()));
		dto.setTotalSleep(achievementDao.getTotalSleep(child.getId()));
		return dto;
	}
	
	@Override
	@Transactional
	public void delete(Child child) {
		goalDao.deleteByChild(child);
		rewardDao.deleteByChild(child);
		achievementDao.deleteByChild(child);
		childDao.delete(child);
	}	

	@Override
	@Transactional
	public String createResetCode(Account account) throws IOException {
		passwordResetDao.deleteByEmail(account.getEmail());
		PasswordReset reset = new PasswordReset();
		reset.setEmail(account.getEmail());
		TokenIdentifierGenerator gen = new TokenIdentifierGenerator();

		reset.setLocalCode(gen.nextSessionId());
		reset.setRemoteCode(gen.nextSessionId());

		passwordResetDao.save(reset);

		StringWriter writer = new StringWriter();
		PrintWriter printer = new PrintWriter(writer);
		
		printer.println("Somebody (hopefully you) has requested asked to reset your password on the healthy kids application.");
		printer.println();
		printer.println("Please copy the following code into the field where requested, and choose a new password.");
		printer.println();
		printer.println(reset.getRemoteCode());
		printer.println();
		printer.println("If you have any questions please contact...");
		printer.close();
		printer.flush();
		
		emailer.sendEmail(account.getEmail(), "service@healthykids.co.nz", "Password reset code", writer.getBuffer().toString(), null);
		
		return reset.getLocalCode();
		
	}
}
