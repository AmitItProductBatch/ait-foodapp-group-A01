package com.ait.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ait.app.dto.UpdateUserDto;
import com.ait.app.dto.UserResponseDto;
import com.ait.app.dto.UsersDto;
import com.ait.app.model.Users;
import com.ait.app.service.UserService;

@RestController
@RequestMapping("api/user")
public class UserConttroller {

	@Autowired
	UserService userService;

	@PostMapping("register")
	public ResponseEntity register(@RequestBody UsersDto user) {

		userService.registerUser(user);
		return new ResponseEntity("User Registerd Successfully", HttpStatus.CREATED);
//
	}

	@GetMapping("/getUser/{id}")
	public ResponseEntity getUserDetails(@PathVariable int id) {

		UserResponseDto userResponseDto = userService.getUserDetails(id);
		return new ResponseEntity(userResponseDto, HttpStatus.OK);

	}

	@PutMapping("/updateUser/{id}")
	public ResponseEntity updateUserById(@PathVariable int id, @RequestBody UpdateUserDto dto) {
		Users updatedUser = userService.updateUserById(id, dto);
		return new ResponseEntity(updatedUser, HttpStatus.CREATED);

	}
}
