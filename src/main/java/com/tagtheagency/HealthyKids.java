package com.tagtheagency;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tagtheagency.healthykids.dto.AccountDTO;
import com.tagtheagency.healthykids.model.Account;
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
	public AccountDTO create(@RequestBody AccountDTO details) throws DuplicateAccountException {
		AccountDTO account = AccountDTO.createFrom(manager.createAccount(details.getEmail(), details.getPassword()));
		account.setToken(tokenHandler.createTokenForUser(account.getEmail()));
		
		return account;
	}

	@RequestMapping(value="/resetCode", method = RequestMethod.POST)
	public ResetDTO getResetCode(@RequestBody ResetDTO email) throws IOException {
		Account account = manager.findByEmail(email.email);
		if (account == null) {
			return null;
		}
		String localCode = manager.createResetCode(account);
		ResetDTO dto = new ResetDTO();
		dto.code = localCode;
		return dto;
	
	}

	@RequestMapping(value="/resetPassword", method = RequestMethod.POST)
	public boolean resetPassword(@RequestBody ResetDTO values) {
		System.out.println(values);
		return true;
	}
	
	
	static class ResetDTO {
		public String email;
		public String code;
		public String password;
	}
	
	
}

