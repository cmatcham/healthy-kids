package com.tagtheagency.healthykids.dto;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tagtheagency.healthykids.model.Achievement;
import com.tagtheagency.healthykids.model.Child;

public class ChildDTO {

	private String firstName;
	private String lastName;
	private String dateOfBirth;
	private int id;
	
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
	public Map<Integer, AchievementDTO> getDailyAchievements() {
		return dailyAchievements;
	}
	
	public static ChildDTO convertFrom(Child child) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		ChildDTO dto = new ChildDTO();
		dto.setFirstName(child.getFirstName());
		dto.setLastName(child.getLastName());
		dto.setDateOfBirth(formatter.format(child.getDateOfBirth()));
		dto.setId(child.getId());
		return dto;
	}
	
	public void setAchievements(List<Achievement> weeklyAchievements) {
		Calendar cal = Calendar.getInstance();

		dailyAchievements = new HashMap<Integer, AchievementDTO>();
		weeklyAchievements.forEach(a -> {
			cal.setTime(a.getDate());
			int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
			dailyAchievements.put(dayOfWeek, AchievementDTO.convertFrom(a));
		});
		System.out.println("Padding achievements");
		padAchievements(7);
	}
	
	private void padAchievements(int num) {
		System.out.println("Padding to "+num);
		for (int i = 0; i < num; i++) {
			if (dailyAchievements.containsKey(i)) {
				System.out.println("Contains num "+i);
				continue;
			}
			System.out.println("Doesn't, padding it");
			dailyAchievements.put(i, new AchievementDTO.None(new Date()));
		}
	}
	
	
}
