package com.ait.app.controller.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

 
import com.ait.app.model.User;
import com.ait.app.service.UserService;

@RestController
@RequestMapping("api/user")
public class UserConttroller {
	
	@Autowired
	UserService userService;
	
	@PostMapping("register")
	public ResponseEntity register(@RequestBody User user) {
		
		userService.registerUser(user);
		return new ResponseEntity("User Registerd",HttpStatus.CREATED);
		
		
	}
	
//	@PostMapping("login")
//	public ResponseEntity login(@RequestBody UserDto userDto) {
//		
//		
//		return null;
//		
//	}

}
