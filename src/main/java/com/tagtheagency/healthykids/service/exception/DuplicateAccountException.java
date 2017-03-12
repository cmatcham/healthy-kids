package com.tagtheagency.healthykids.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.CONFLICT, reason="Account already exists")
public class DuplicateAccountException extends Exception {

	
}
