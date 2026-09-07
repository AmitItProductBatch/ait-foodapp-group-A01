package com.ait.app.service;

import com.ait.app.dto.UserResponseDto;
import com.ait.app.dto.UsersDto;
import com.ait.app.model.Users;

public interface UserService {
	
	
	public UserResponseDto registerUser(UsersDto user);
	//public void loginUser();

}