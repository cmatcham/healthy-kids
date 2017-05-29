package com.tagtheagency;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tagtheagency.healthykids.dto.AccountDTO;
import com.tagtheagency.healthykids.service.HealthyKidsManager;
import com.tagtheagency.healthykids.service.exception.DuplicateAccountException;
import com.tagtheagency.security.TokenHandler;

@SpringBootApplication
@RestController
@RequestMapping("/")
public class HealthyKids{


	@Autowired HealthyKidsManager manager;
	@Autowired TokenHandler tokenHandler;
	
	public static void main(String[] args) {
		SpringApplication.run(HealthyKids.class, args);
	}

	@RequestMapping(value="/account", method = RequestMethod.POST)
	public AccountDTO create(@RequestBody AccountDTO details, HttpServletResponse response) throws DuplicateAccountException {
		if (details.getEmail() == null || details.getEmail().isEmpty()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		System.out.println("Controller creating account");
		AccountDTO account = AccountDTO.createFrom(manager.createAccount(details.getEmail(), details.getPassword()));
		System.out.println("Which didn't throw excaption, so sret a token");
		account.setToken(tokenHandler.createTokenForUser(account.getEmail()));
		
		return account;
	}

	
	
	
}

