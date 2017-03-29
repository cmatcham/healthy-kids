package com.tagtheagency.healthykids.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="achievement")
public class Achievement {

	private int id;
	private Child child;
	private boolean sleep;
	private boolean nutrition;
	private boolean movement;
	private Date date;
	
	@Id
	@GeneratedValue
	@Column(name="achievement_id")
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "child_id", nullable = false)
	public Child getChild() {
		return child;
	}

	public void setChild(Child child) {
		this.child = child;
	}
	
	@Column(name="achievement_date")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="dd/MM/yyyy")
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}

	@Column
	public boolean isMovement() {
		return movement;
	}
	
	public void setMovement(boolean movement) {
		this.movement = movement;
	}
	
	@Column
	public boolean isNutrition() {
		return nutrition;
	}
	public void setNutrition(boolean nutrition) {
		this.nutrition = nutrition;
	}
	
	@Column
	public boolean isSleep() {
		return sleep;
	}
	public void setSleep(boolean sleep) {
		this.sleep = sleep;
	}

	@Override
	public String toString() {
		return "S: "+sleep+"; N: "+nutrition+"; M: "+movement;
	}
}

