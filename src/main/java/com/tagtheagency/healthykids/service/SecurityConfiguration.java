//package com.tagtheagency.healthykids.service;
//
//import javax.sql.DataSource;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
//
//import com.tagtheagency.security.StatelessAuthenticationFilter;
//import com.tagtheagency.security.TokenAuthenticationService;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
// 
//    @Autowired
//    @Qualifier("healthKidsUserDetailsService")
//    UserDetailsService userDetailsService;
//    
//	@Autowired
//	DataSource dataSource;
//     
//    private final TokenAuthenticationService tokenAuthenticationService;
//
//    public SecurityConfiguration() {
//        super(true);
//        tokenAuthenticationService = new TokenAuthenticationService("h34lthyK1d5", userDetailsService);
//    }
//	
//    @Autowired
//    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService);
//        auth.authenticationProvider(authenticationProvider());
//    }
//    
////    @Autowired
////	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
////		
////		auth.jdbcAuthentication().dataSource(dataSource)
////			.passwordEncoder(passwordEncoder())
////			.usersByUsernameQuery("SELECT username, password FROM users WHERE USERNAME = ?");
//////			.authoritiesByUsernameQuery("select username, role from user_roles where username=?");
////		
////	}	
////     
//     
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//     
//     
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userDetailsService);
//        authenticationProvider.setPasswordEncoder(passwordEncoder());
//        return authenticationProvider;
//    }
////    
////    
////    @Autowired
////    private JwtAuthenticationEntryPoint unauthorizedHandler;
////    @Autowired
////    private JwtAuthenticationProvider authenticationProvider;
////    @Bean
////    @Override
////    public AuthenticationManager authenticationManager() throws Exception {
////        return new ProviderManager(Arrays.asList(authenticationProvider));
////    }
////    @Bean
////    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
////        JwtAuthenticationTokenFilter authenticationTokenFilter = new JwtAuthenticationTokenFilter();
////        authenticationTokenFilter.setAuthenticationManager(authenticationManager());
////        authenticationTokenFilter.setAuthenticationSuccessHandler(new JwtAuthenticationSuccessHandler());
////        return authenticationTokenFilter;
////    }
////    
//    
//    @Override
//	protected void configure(HttpSecurity http) throws Exception {
//        http
//        // we don't need CSRF because tokens 
//        .csrf().disable()
//        // 
//        .authorizeRequests()
//	        .antMatchers("/static/**", "/css/**", "/js/**").permitAll()
//			.antMatchers("/login", "/index.html", "/home.html", "/login.html", "/details.html", "/account", "/").permitAll()
//	        .anyRequest().authenticated()
//        .and()
//        // Call our errorHandler if authentication/authorisation fails
//        .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
//        .and()
//        // don't create session
//        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //.and()
//        // Custom JWT based security filter
//        http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
//        // disable page caching
//        http.headers().cacheControl();
//    	
////    	
////    	System.out.println("CSJM configuring security");
////		http
////			.exceptionHandling().and()
////	        .anonymous().and()
////	        .servletApi().and()
////	        .headers().cacheControl().and()
////	        .authorizeRequests()
////		.httpBasic().and()
////			.authorizeRequests()
////				.antMatchers("/static/**", "/css/**", "/js/**").permitAll()
////				.antMatchers("/login", "/index.html", "/home.html", "/login.html", "/details.html", "/account", "/").permitAll()
////				.anyRequest().authenticated()
////				.and()
//////			.csrf().disable()
////			.addFilterBefore(new StatelessAuthenticationFilter(tokenAuthenticationService),
////                    UsernamePasswordAuthenticationFilter.class);
//////				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
//////    	http
//////        .httpBasic()
//////        .and()
//////        .authorizeRequests()
//////          .antMatchers("/index.html", "/home.html", "/login.html", "/").permitAll()
//////          .anyRequest().authenticated()
//////          .and()
//////          .csrf();
//
//    	
//    }
//}