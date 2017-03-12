package com.tagtheagency.security.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtTokenMalformedException extends AuthenticationException {

	private static final long serialVersionUID = -3976632627371188612L;

	public JwtTokenMalformedException(String msg) {
        super(msg);
    }
}
