package com.tagtheagency.healthykids.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tagtheagency.healthykids.model.Account;
import com.tagtheagency.healthykids.model.Achievement;
import com.tagtheagency.healthykids.model.Child;
import com.tagtheagency.healthykids.model.Reward;
import com.tagtheagency.healthykids.model.Target;
import com.tagtheagency.healthykids.persistence.AccountDAO;
import com.tagtheagency.healthykids.persistence.AchievementDAO;
import com.tagtheagency.healthykids.persistence.ChildDAO;
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
	public Child createChild(Account account, String firstName, String lastName, Date dateOfBirth) {
		Child child = new Child();
		child.setAccount(account);
		child.setFirstName(firstName);
		child.setLastName(lastName);
		child.setDateOfBirth(dateOfBirth);
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
		return Arrays.asList("abstract1.jpg", "abstract2.jpg", "abstract3.jpg", "abstract4.jpg", 
				"pattern2.png", "pattern3.png", "pattern4.png", "pattern1.png", 
				"default.png");
		/*
		
		List<String> files = new ArrayList<>();
		try {
			System.out.println(getClass());
			
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
	
}
