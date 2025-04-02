package com.project.springproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.project.springproject.Service.CustomUserDetailsService;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(List.of(authProvider));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
            .authorizeHttpRequests(auth -> auth
                // Public endpoints that don't require authentication
                .requestMatchers(
                    new AntPathRequestMatcher("/auth/register"),
                    new AntPathRequestMatcher("/auth/login"),
                    new AntPathRequestMatcher("/auth/logout"),
                    new AntPathRequestMatcher("/auth/me")
                ).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/users/register")).permitAll()

                // Admin only endpoints
                .requestMatchers(new AntPathRequestMatcher("/categories/**", HttpMethod.POST.name())).hasRole("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/categories/**", HttpMethod.PUT.name())).hasRole("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/categories/**", HttpMethod.DELETE.name())).hasRole("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/users/**")).hasRole("ADMIN")

                // User and Admin endpoints
                .requestMatchers(new AntPathRequestMatcher("/categories/**", HttpMethod.GET.name())).hasAnyRole("USER", "ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/tasks/**", HttpMethod.GET.name())).hasAnyRole("USER", "ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/tasks/**", HttpMethod.POST.name())).hasAnyRole("USER", "ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/tasks/**", HttpMethod.PUT.name())).hasAnyRole("USER", "ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/tasks/**", HttpMethod.DELETE.name())).hasAnyRole("USER", "ADMIN")

                .anyRequest().authenticated()
            )
            .formLogin(formLogin -> formLogin.disable())
            .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}