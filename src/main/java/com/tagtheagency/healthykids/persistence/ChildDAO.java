package com.tagtheagency.healthykids.persistence;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tagtheagency.healthykids.model.Account;
import com.tagtheagency.healthykids.model.Child;


public interface ChildDAO extends CrudRepository<Child, Long>{

	List<Child> findByAccount(Account account);

}
