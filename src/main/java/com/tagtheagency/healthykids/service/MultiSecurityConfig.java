package com.tagtheagency.healthykids.service;

import javax.servlet.Filter;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tagtheagency.security.JwtAuthenticationProvider;
import com.tagtheagency.security.JwtAuthenticationSuccessHandler;
import com.tagtheagency.security.JwtAuthenticationTokenFilter;
import com.tagtheagency.security.LoginFailureHandler;
import com.tagtheagency.security.LoginSuccessHandler;


@EnableWebSecurity
public class MultiSecurityConfig  {

	@Configuration
    @Order(1)
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

    	@Autowired
        @Qualifier("healthKidsUserDetailsService")
        UserDetailsService userDetailsService;
    	
    	@Bean(value="jwtAuthenticationProvider")
    	public AuthenticationProvider authenticationProvider() {
    		JwtAuthenticationProvider authenticationProvider = new JwtAuthenticationProvider();
    		authenticationProvider.setUserDetailsService(userDetailsService);
    		return authenticationProvider;
    	}

    	@Autowired
    	public void configure(AuthenticationManagerBuilder auth) throws Exception {
    		auth.authenticationProvider(authenticationProvider());
    		auth.eraseCredentials(false);
    	}
    	
    	@Override
        public void configure(WebSecurity web) throws Exception {
            //web.debug(true);
//            web.ignoring().antMatchers(")
        }


    	protected void configure(HttpSecurity http) throws Exception {
    		http
    		.antMatcher("/api/**")
    		// Custom JWT based security filter
    		.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class)
    		.authorizeRequests()
    		.anyRequest().authenticated().and()
    		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //.and()
    		

    		// disable page caching
//    		http
//    		.antMatcher("/services/**")
//    		.headers().cacheControl();
//    		
//    		http
//    		.antMatcher("/services/**")
//    		.csrf().disable();
    		//              .anyRequest().hasRole("API")
    		//              .and()
    		//              .httpBasic()
    		//              .and()
    		//              .csrf()
    		//              .disable();
    	}

        @Bean
        public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
            JwtAuthenticationTokenFilter authenticationTokenFilter = new JwtAuthenticationTokenFilter();
            authenticationTokenFilter.setAuthenticationManager(authenticationManager());
            authenticationTokenFilter.setAuthenticationSuccessHandler(new JwtAuthenticationSuccessHandler());
            return authenticationTokenFilter;
        }
    }

    @Configuration
    @Order(2)
    public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    	@Autowired
        @Qualifier("healthKidsUserDetailsService")
        UserDetailsService userDetailsService;
        
    	@Autowired
    	DataSource dataSource;
    	
    	@Autowired LoginSuccessHandler loginSuccessHandler;
    	@Autowired LoginFailureHandler loginFailureHandler;
    	
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
        
        @Override
        public void configure(WebSecurity web) throws Exception {
//            web.debug(true);
        	web.ignoring().antMatchers("/api/**");
        }
         
         
        @Bean(value="daoAuthenticationProvider")
        public DaoAuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
            authenticationProvider.setUserDetailsService(userDetailsService);
            authenticationProvider.setPasswordEncoder(passwordEncoder());
            return authenticationProvider;
        }
    	
        @Autowired
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(authenticationProvider());
            auth.eraseCredentials(false);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
            	.antMatchers("/static/**", "/css/**", "/js/**", "/styles/**", "/stickers/**", "/fonts/**", "/imgs/**").permitAll()
            	.antMatchers("/login", "/account", "/*.html", "/app/**/*.html", "/").permitAll()
                .anyRequest().authenticated()
            .and().formLogin().successHandler(loginSuccessHandler)
            	.failureHandler(loginFailureHandler)
//                .failureUrl("/login")
//                .loginPage("/login")
//                .loginProcessingUrl("/login")
//                .defaultSuccessUrl("/")
//                .usernameParameter("username")
//                .passwordParameter("password")
                .permitAll();

            http.csrf().disable();

            // iFRAMES SETTINGS
            http
                .headers()
                .frameOptions().sameOrigin()
                .httpStrictTransportSecurity().disable();

            // HTTPS
//            http
//                .requiresChannel()
//                .anyRequest()
//                .requiresSecure();

//            //MAP 8080 to HTTPS PORT
//            http.portMapper().http(8080).mapsTo(443);
        }

    }
}