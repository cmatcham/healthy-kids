package com.tagtheagency.healthykids.persistence;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tagtheagency.healthykids.model.Achievement;
import com.tagtheagency.healthykids.model.Child;


public interface AchievementDAO extends CrudRepository<Achievement, Long>{

	List<Achievement> findByChildAndDate(Child child, Date date);
	
	List<Achievement> findByChildAndDateBetween(Child child, Date from, Date to);
	
	@Query(value = "SELECT count(nutrition) FROM achievement a WHERE a.child_id = :childId AND nutrition = true", nativeQuery=true)
	public long getTotalNutrition(@Param("childId") int childId);

	@Query(value = "SELECT count(sleep) FROM achievement a WHERE a.child_id = :childId AND sleep = true", nativeQuery=true)
	public long getTotalSleep(@Param("childId") int childId);

	@Query(value = "SELECT count(movement) FROM achievement a WHERE a.child_id = :childId AND movement = true", nativeQuery=true)
	public long getTotalMovement(@Param("childId") int childId);

}
