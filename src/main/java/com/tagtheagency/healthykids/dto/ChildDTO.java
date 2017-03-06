package com.tagtheagency.healthykids.dto;

import java.text.SimpleDateFormat;

import com.tagtheagency.healthykids.model.Child;

public class ChildDTO {

	private String firstName;
	private String lastName;
	private String dateOfBirth;
	private int id;
	
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
	public static ChildDTO convertFrom(Child child) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		ChildDTO dto = new ChildDTO();
		dto.setFirstName(child.getFirstName());
		dto.setLastName(child.getLastName());
		dto.setDateOfBirth(formatter.format(child.getDateOfBirth()));
		dto.setId(child.getId());
		return dto;
	}
	
	
}
