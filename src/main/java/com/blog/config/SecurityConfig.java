package com.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@Configuration
@EnableWebSecurity
@EnableWebMvc
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig   {
	
	@Autowired
	public CustomUserDetailService customUserDetailService;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http.csrf().disable()
	        .cors() // Enable CORS
	        .and()
	        .authorizeHttpRequests()
	        .requestMatchers("/api/auth/**").permitAll()
	        .requestMatchers("/v3/api-docs").permitAll()
	        .requestMatchers("/swagger-ui/**").permitAll()
	        .requestMatchers("/swagger-resources/**").permitAll()
	        .requestMatchers(HttpMethod.GET).permitAll()
			 .requestMatchers(HttpMethod.POST).permitAll()
	        .anyRequest().authenticated()
	        .and()
	        .httpBasic();
	    
	    http.authenticationProvider(daoAuthenticationProvider());
	    
	    return http.build();
	}

	
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider()
	{
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(customUserDetailService);
		provider.setPasswordEncoder(passwordEncoder());
		
		return provider;
		
	}
	
	@Bean
	public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
	@Bean
	public CorsFilter corsFilter() {
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

	    CorsConfiguration corsConfiguration = new CorsConfiguration();
	    corsConfiguration.setAllowCredentials(true);
	    corsConfiguration.addAllowedOriginPattern("*"); // Allow all origins
	    corsConfiguration.addAllowedHeader("*"); // Allow all headers
	    corsConfiguration.addAllowedMethod("*"); // Allow all methods
	    corsConfiguration.setMaxAge(3600L); // Cache preflight response

	    source.registerCorsConfiguration("/**", corsConfiguration);

	    return new CorsFilter(source);
	}


}
