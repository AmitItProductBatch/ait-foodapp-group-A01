package com.ait.app.service;

import com.ait.app.dto.UserResponseDto;
import com.ait.app.dto.UsersDto;
import com.ait.app.model.Users;

public interface UserService {

	public UserResponseDto registerUser(UsersDto user);

	// Story no FOO-101 //
	public UserResponseDto getUserDetails(int id);

	
}