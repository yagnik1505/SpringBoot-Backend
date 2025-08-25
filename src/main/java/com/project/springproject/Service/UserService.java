package com.project.springproject.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.springproject.Entity.UserEntity;
import com.project.springproject.Exception.UserAlreadyExistsException;
import com.project.springproject.Repository.UserRepository;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JdbcUserDetailsManager userDetailsManager;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JdbcUserDetailsManager userDetailsManager) {
        this.userRepository = 
        		userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsManager = userDetailsManager;
    }
    
    @Transactional
    public UserEntity registerUser(UserEntity user) {
        if (userDetailsManager.userExists(user.getUsername())) {
            throw new UserAlreadyExistsException("User already exists: " + user.getUsername());
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        UserDetails userDetails = User.withUsername(user.getUsername())
                .password(encodedPassword)
                .roles("USER")
                .build();

        userDetailsManager.createUser(userDetails);
        return userRepository.save(user);
    }
  
}

