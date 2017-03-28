package com.tagtheagency.healthykids.persistence;

import org.springframework.data.repository.CrudRepository;

import com.tagtheagency.healthykids.model.Reward;


public interface RewardDAO extends CrudRepository<Reward, Integer>{

}
