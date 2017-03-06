package com.tagtheagency.healthykids.persistence;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tagtheagency.healthykids.model.Account;


public interface AccountDAO extends CrudRepository<Account, Long>{

	List<Account> findByEmail(String email);
	
}
