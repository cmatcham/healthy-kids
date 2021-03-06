package com.tagtheagency;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tagtheagency.healthykids.dto.AchievementDTO;
import com.tagtheagency.healthykids.dto.ChildDTO;
import com.tagtheagency.healthykids.dto.GoalDTO;
import com.tagtheagency.healthykids.dto.RewardDTO;
import com.tagtheagency.healthykids.dto.SummaryDTO;
import com.tagtheagency.healthykids.model.Account;
import com.tagtheagency.healthykids.model.Achievement;
import com.tagtheagency.healthykids.model.Child;
import com.tagtheagency.healthykids.model.Goal;
import com.tagtheagency.healthykids.model.Target;
import com.tagtheagency.healthykids.service.HealthyKidsManager;
import com.tagtheagency.healthykids.service.UnauthorisedException;

@RestController
@RequestMapping("/api")
public class ApiController {

	@Autowired HealthyKidsManager manager;
	
	@RequestMapping("/user")
	public Account user() {
		return getAccount();
	}
	
	@RequestMapping("/stickers")
	public List<String> getStickers() {
		return manager.getStickers();
	}
	
	@RequestMapping("/child/all")
	public List<ChildDTO> children() {
		return manager.getChildren(getAccount()).stream().map(ChildDTO::convertFrom).collect(Collectors.toList());
	}
	
	@RequestMapping("/child/{id}")
	public ChildDTO getChild(@PathVariable int id) {
		Child child = findChild(id);
		if (child == null) {
			return null;
		}

		List<Achievement> weeklyAchievements = manager.getWeeklyAchievements(child, new Date());
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -7);
		List<LocalDate> daysOfWeek = manager.getWeekOf(new Date());
		ChildDTO dto = ChildDTO.convertFrom(child, true);
		dto.setAchievements(weeklyAchievements, daysOfWeek);
		List<Achievement> lastWeeklyAchievements = manager.getWeeklyAchievements(child, cal.getTime());
		dto.setLastWeekAchievements(lastWeeklyAchievements, manager.getWeekOf(cal.getTime()));
		return dto;
	}
	
	@RequestMapping(value="/child", method = RequestMethod.PUT)
	//TODO fix exception handling
	public ChildDTO createChild(@RequestBody ChildDTO child) throws Exception {
//		SimpleDateFormat dateParser = new SimpleDateFormat("dd/MM/yyyy");
//		Date birthday;
//		try {
//			birthday = dateParser.parse(child.getDateOfBirth());
//		} catch (ParseException e) {
//			throw new Exception("Invalid date");
//		}
		return ChildDTO.convertFrom(manager.createChild(getAccount(), child.getFirstName(), child.getAge(), child.getSticker()));	
	}
	
	@RequestMapping(value="/child/{id}/reward", method = RequestMethod.PUT) 
	public ChildDTO addCustomReward(@PathVariable int id, @RequestBody List<RewardDTO> rewards) {
		Child child = findChild(id);
		if (child == null) {
			return null;
		}
		Map<Integer, String> rewardMap = rewards.stream().collect(Collectors.toMap(RewardDTO::getId, RewardDTO::getReward));
		manager.addCustomReward(child, rewardMap);
		return ChildDTO.convertFrom(child, true);
	}

	@RequestMapping(value="/child/{id}/goal", method = RequestMethod.PUT) 
	public GoalDTO addCustomGoal(@PathVariable int id, @RequestBody GoalDTO goal) {
		Child child = findChild(id);
		if (child == null) {
			return null;
		}
		
		if (goal.getId() < 0) {
			goal.setId(manager.addCustomGoal(child, goal));
		} else {
			manager.editCustomGoal(child, goal);
		}

		return goal;
	}
	
	@RequestMapping(value="/child/{id}/update/{type}/{target}", method=RequestMethod.POST)
	public ChildDTO updateGoalsAndRewards(@PathVariable int id, @PathVariable String type, @PathVariable String target, @RequestParam Integer selected) {
		Child child = findChild(id);
		if (child == null) {
			return null;
		}
		
		switch (target) {
		case "nutrition":
			if (type.equals("goal")) {
				child.setDefaultNutritionGoal(selected);
			} else {
				return null;
			}
			break;
		case "movement":
			if (type.equals("goal")) {
				child.setDefaultMovementGoal(selected);
			} else {
				return null;
			}
			break;
		case "sleep":
			if (type.equals("goal")) {
				child.setDefaultSleepGoal(selected);
			} else {
				return null;
			}
			break;
			default: return null;
				
		}
		return ChildDTO.convertFrom(child);
		
	}

	@RequestMapping(value="/child/{id}/update", method = RequestMethod.POST)
	public ChildDTO updateChild(@PathVariable int id, @RequestBody ChildDTO dto) throws Exception {
		if (id < 0) {
			return createChild(dto);
		}
		Child child = findChild(dto.getId());
		if (child == null) {
			return null;
		}
		
		child.setDefaultMovementGoal(dto.getDefaultMovementGoal());
		child.setDefaultNutritionGoal(dto.getDefaultNutritionGoal());
		child.setDefaultSleepGoal(dto.getDefaultSleepGoal());
		child.setCustomReward(dto.getCustomReward());
		child.setDefaultReward(dto.getDefaultReward());
		child.setUseCustomReward(dto.getUseCustomReward());
		
		return ChildDTO.convertFrom(manager.updateChild(child, dto.getFirstName(), dto.getAge(), dto.getSticker()));
	}
	
	@RequestMapping(value="/child/{id}/target", method = RequestMethod.POST)
	public AchievementDTO setTarget(@PathVariable int id, @RequestBody AchievementDTO dto) throws UnauthorisedException, ParseException {
		Child child = findChild(id);
		if (child == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		manager.setAchievement(getAccount(), child, Target.MOVEMENT, sdf.parse(dto.getDate()), dto.isMovement());
		manager.setAchievement(getAccount(), child, Target.NUTRITION, sdf.parse(dto.getDate()), dto.isNutrition());
		manager.setAchievement(getAccount(), child, Target.SLEEP, sdf.parse(dto.getDate()), dto.isSleep());
		return dto;
		
	}
	
	@RequestMapping(value="/child/{id}/sticker", method = RequestMethod.POST)
	public ChildDTO setSticker(@PathVariable int id, @RequestBody String sticker) throws UnauthorisedException, ParseException {
		Child child = findChild(id);
		if (child == null) {
			return null;
		}
		manager.setSticker(child, sticker);
		return ChildDTO.convertFrom(child);
		
	}
	
	@RequestMapping(value="/child/{id}", method = RequestMethod.DELETE)
	public List<ChildDTO> deleteChild(@PathVariable int id) {
		Child child = findChild(id);
		if (child == null) {
			return null;
		}
		manager.delete(child);
		return children();
	}
	
	@RequestMapping(value="/child/{id}/summary", method = RequestMethod.GET)
	public SummaryDTO getSummary(@PathVariable int id) throws UnauthorisedException {
		Child child = findChild(id);
		if (child == null) {
			return null;
		}
		return manager.getSummary(child);
		
	}

	private Account getAccount() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return manager.findByEmail(auth.getName());
	}

	/**
	 * Get a child if use has access to them, return null if not.
	 * @param id
	 * @return
	 */
	private Child findChild(int id) {
		Account account = getAccount();
		for (Child child : manager.getChildren(account)) {
			if (child.getId() == id) {
				return child;
			}
		}
		return null;
	}
}
