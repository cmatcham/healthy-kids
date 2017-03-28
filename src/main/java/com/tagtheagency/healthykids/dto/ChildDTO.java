package com.tagtheagency.healthykids.dto;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.tagtheagency.healthykids.model.Achievement;
import com.tagtheagency.healthykids.model.Child;
import com.tagtheagency.healthykids.model.Reward;

public class ChildDTO {

	private String firstName;
	private String lastName;
	private String dateOfBirth;
	private String sticker;
	private int id;
	private List<RewardDTO> customRewards;
	
	private Map<Integer, AchievementDTO> dailyAchievements;
	
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSticker() {
		return sticker;
	}
	public void setSticker(String sticker) {
		this.sticker = sticker;
	}
	
	public Map<Integer, AchievementDTO> getDailyAchievements() {
		return dailyAchievements;
	}
	
	public List<RewardDTO> getCustomRewards() {
		return customRewards;
	}
	
	public void setCustomRewards(List<Reward> customRewards) {
		this.customRewards = customRewards.stream().map(RewardDTO::createFrom).collect(Collectors.toList());
	}
	
	public static ChildDTO convertFrom(Child child) {
		return convertFrom(child, false);
	}
	
	public static ChildDTO convertFrom(Child child, boolean populateRewards) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		ChildDTO dto = new ChildDTO();
		dto.setFirstName(child.getFirstName());
		dto.setLastName(child.getLastName());
		dto.setDateOfBirth(formatter.format(child.getDateOfBirth()));
		dto.setId(child.getId());
		dto.setSticker(child.getSticker());
		if (populateRewards) {
			dto.setCustomRewards(child.getCustomRewards());
		}
		return dto;
	}
	
	public void setAchievements(List<Achievement> weeklyAchievements, List<LocalDate> daysOfWeek) {
		Calendar cal = Calendar.getInstance();

		dailyAchievements = new HashMap<Integer, AchievementDTO>();
		weeklyAchievements.forEach(a -> {
			cal.setTime(a.getDate());
			int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
			dayOfWeek = (dayOfWeek + 5) % 7;
			dailyAchievements.put(dayOfWeek, AchievementDTO.convertFrom(a));
		});
		padAchievements(daysOfWeek);
	}
	
	private void padAchievements(List<LocalDate> daysOfWeek) {
		for (int i = 0; i < daysOfWeek.size(); i++) {
			if (dailyAchievements.containsKey(i)) {
				continue;
			}
			Date date = Date.from(daysOfWeek.get(i).atStartOfDay(ZoneId.systemDefault()).toInstant());
			dailyAchievements.put(i, new AchievementDTO.None(dateWithoutTime(date)));
		}
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
	
	
}
