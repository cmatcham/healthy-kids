package com.tagtheagency;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tagtheagency.healthykids.dto.AchievementDTO;
import com.tagtheagency.healthykids.dto.ChildDTO;
import com.tagtheagency.healthykids.model.Account;
import com.tagtheagency.healthykids.model.Achievement;
import com.tagtheagency.healthykids.model.Child;
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
		List<LocalDate> daysOfWeek = manager.getWeekOf(new Date());
		ChildDTO dto = ChildDTO.convertFrom(child);
		dto.setAchievements(weeklyAchievements);
		return dto;
	}
	
	@RequestMapping(value="/child", method = RequestMethod.PUT)
	//TODO fix exception handling
	public ChildDTO createChild(@RequestBody ChildDTO child) throws Exception {
		SimpleDateFormat dateParser = new SimpleDateFormat("dd/MM/yyyy");
		Date birthday;
		try {
			birthday = dateParser.parse(child.getDateOfBirth());
		} catch (ParseException e) {
			throw new Exception("Invalid date");
		}
		return ChildDTO.convertFrom(manager.createChild(getAccount(), child.getFirstName(), child.getLastName(), birthday));	
	}
	
	@RequestMapping(value="/child/{id}/target", method = RequestMethod.POST)
	public AchievementDTO setTarget(@PathVariable int id, @RequestBody AchievementDTO dto) throws UnauthorisedException {
		Child child = findChild(id);
		if (child == null) {
			return null;
		}
		if (dto.isMovement()) {
			manager.setAchievement(getAccount(), child, Target.MOVEMENT, dto.getDate());
		}
		if (dto.isNutrition()) {
			manager.setAchievement(getAccount(), child, Target.NUTRITION, dto.getDate());
		}
		if (dto.isSleep()) {
			manager.setAchievement(getAccount(), child, Target.SLEEP, dto.getDate());
		}
		return dto;
		
	}

	private Account getAccount() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		System.out.println("Getting an auth, username is "+auth.getName());
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
