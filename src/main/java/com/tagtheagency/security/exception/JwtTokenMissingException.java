package com.tagtheagency.security.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtTokenMissingException extends AuthenticationException {

	private static final long serialVersionUID = 6681844996710473729L;

	public JwtTokenMissingException(String msg) {
        super(msg);
    }
}
