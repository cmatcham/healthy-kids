package com.tagtheagency.healthykids.persistence;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tagtheagency.healthykids.model.Child;
import com.tagtheagency.healthykids.model.Goal;
import com.tagtheagency.healthykids.model.Target;


public interface GoalDAO extends CrudRepository<Goal, Integer>{

	List<Goal> findByChildAndTarget(Child child, Target target);
}
