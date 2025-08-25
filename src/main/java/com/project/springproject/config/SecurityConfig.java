package com.project.springproject.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer -> configurer
          
            .requestMatchers(HttpMethod.POST, "/users/add").permitAll()
            
            
            // Admin-only endpoints (category management)
            .requestMatchers(HttpMethod.POST, "/categories").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/categories/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/categories/**").hasRole("ADMIN")
            
            // Category read endpoints (both admin and users)
            .requestMatchers(HttpMethod.GET, "/categories/**").permitAll()
            
            // Task management endpoints
            .requestMatchers(HttpMethod.POST, "/tasks/**").hasAnyRole("USER")  
            .requestMatchers(HttpMethod.PUT, "/tasks/**").hasAnyRole("USER")
            .requestMatchers(HttpMethod.DELETE, "/tasks/**").hasAnyRole("USER")
            .requestMatchers(HttpMethod.GET, "/tasks", "/tasks/id").hasAnyRole("USER")
            .requestMatchers(HttpMethod.GET, "/tasks/category/**").permitAll()
            
            .anyRequest().authenticated()
        );

       
        http.httpBasic(Customizer.withDefaults());

      
        http.formLogin(Customizer.withDefaults());

        http.csrf(csrf -> csrf.disable());

        return http.build();
    }
}