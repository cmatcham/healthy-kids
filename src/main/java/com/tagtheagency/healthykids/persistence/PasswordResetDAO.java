package com.tagtheagency.healthykids.persistence;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tagtheagency.healthykids.model.PasswordReset;


public interface PasswordResetDAO extends CrudRepository<PasswordReset, Integer>{

	void deleteByEmail(String email);
	
	List<PasswordReset> findByLocalCode(String localCode);
}
