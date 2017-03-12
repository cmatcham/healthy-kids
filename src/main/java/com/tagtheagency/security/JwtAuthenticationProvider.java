package com.tagtheagency.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    private TokenHandler tokenValidator;
    
	private UserDetailsService userDetailsService;

    @Override
    public boolean supports(Class<?> authentication) {
        return (UserAuthentication.class.isAssignableFrom(authentication));
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) {
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        System.out.println("In retrieve user, username "+username);
        System.out.println("Authentication: "+authentication);
        
        return userDetailsService.loadUserByUsername(username);
//    	U jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
//        String token = jwtAuthenticationToken.getToken();
//
//        JwtUserDto parsedUser = jwtTokenValidator.parseToken(token);
//
//        if (parsedUser == null) {
//            throw new JwtTokenMalformedException("JWT token is not valid");
//        }
//
//        List<GrantedAuthority> authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList(parsedUser.getRole());
//
//        return new AuthenticatedUser(parsedUser.getId(), parsedUser.getUsername(), token, authorityList);
    }

	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
		
	}

}