package com.project.springproject.Controller;

import org.springframework.web.bind.annotation.*;

import com.project.springproject.Entity.UserEntity;
import com.project.springproject.Service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/add")
	public UserEntity addUser(@RequestBody UserEntity user) {
		return userService.registerUser(user);
	}
}
