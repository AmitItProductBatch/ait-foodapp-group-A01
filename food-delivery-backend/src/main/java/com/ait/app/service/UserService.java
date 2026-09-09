package com.ait.app.service;

import com.ait.app.dto.UpdateUserDto;
import com.ait.app.dto.UserResponseDto;
import com.ait.app.dto.UsersDto;
import com.ait.app.model.Users;

public interface UserService {

	public UserResponseDto registerUser(UsersDto user);

	// Story no FOO-101 //
	public UserResponseDto getUserDetails(int id);

	// Story no FOO-104 //
	public Users updateUserById(int id, UpdateUserDto u);
	
	//Story no FOO-105 //
	public void deleteUserByID(int id, UsersDto dto);

}