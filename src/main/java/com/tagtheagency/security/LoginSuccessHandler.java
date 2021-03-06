package com.tagtheagency.security;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import com.tagtheagency.healthykids.service.HealthyKidsManager;

@Service
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired HealthyKidsManager manager;
	
	@Autowired TokenHandler tokenHandler;
	
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response, Authentication authentication)
            throws ServletException, IOException {
    	User auth = new User(authentication.getName(), "", Collections.emptyList());
    	String token = tokenHandler.createTokenForUser(auth);
    	
    	Cookie tokenCookie = new Cookie("access_token",token);
        tokenCookie.setPath("/child-select");
        tokenCookie.setSecure(true);
        tokenCookie.setHttpOnly(true);
        response.addCookie(tokenCookie);

        response.getWriter().append(token);
        response.setStatus(200);
    }

}