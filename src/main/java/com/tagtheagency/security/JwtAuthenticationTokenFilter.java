package com.tagtheagency.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.web.util.UrlPathHelper;

import com.tagtheagency.security.exception.JwtTokenMissingException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter that orchestrates authentication by using supplied JWT token
 */
public class JwtAuthenticationTokenFilter extends AbstractAuthenticationProcessingFilter {

    @Value("token")	//${jwt.header}")
    private String tokenHeader;

    @Autowired TokenHandler handler;
    
    UrlPathHelper pathHelper;
    
    public JwtAuthenticationTokenFilter() {
        super("/api/**");
        pathHelper = new UrlPathHelper();
    }

    /**
     * Attempt to authenticate request - basically just pass over to another method to authenticate request headers
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
    	System.out.println("Attempting JWT authentication");
    	String header = request.getHeader(this.tokenHeader);

        final String HEADER_START = "Bearer ";
        
        if (header == null || !header.startsWith(HEADER_START)) {
            throw new JwtTokenMissingException("No JWT token found in request headers");
        }

        String authToken = header.substring(HEADER_START.length());

        System.out.println("Attempting Authentication of "+authToken);
        UserDetails user = handler.parseUserFromToken(authToken);
        System.out.println("Got a user, returning UserAuthentication");
        return new UserAuthentication(user);
//        JwtAuthenticationToken authRequest = new JwtAuthenticationToken(authToken);
//
//        return getAuthenticationManager().authenticate(authRequest);
    }

//    @Override
//    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
//    	boolean def = super.requiresAuthentication(request, response);
//    	System.out.println("Default impl says "+def);
//    	String path  = pathHelper.getPathWithinApplication(request);
//    	System.out.println("Path with app is "+path);
//    	return path.startsWith("/api/");
//    }
    
    /**
     * Make sure the rest of the filterchain is satisfied
     *
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
    	System.out.println("JWT successful auth");
        super.successfulAuthentication(request, response, chain, authResult);
        
        // As this authentication is in HTTP header, after success we need to continue the request normally
        // and return the response as if the resource was not secured at all
        chain.doFilter(request, response);
    }
}