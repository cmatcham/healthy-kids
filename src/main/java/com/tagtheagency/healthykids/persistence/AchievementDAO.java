package com.tagtheagency.healthykids.persistence;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tagtheagency.healthykids.model.Achievement;
import com.tagtheagency.healthykids.model.Child;


public interface AchievementDAO extends CrudRepository<Achievement, Long>{

	List<Achievement> findByChildAndDate(Child child, Date date);
	
	List<Achievement> findByChildAndDateBetween(Child child, Date from, Date to);
	
}
