package com.ait.app.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.ait.app.controller.CustomerController;
import com.ait.app.dto.UserResponseDto;
import com.ait.app.dto.UsersDto;
import com.ait.app.exception.UserServiceException;
import com.ait.app.model.Users;
import com.ait.app.repository.UserRepository;
import com.ait.app.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserResponseDto registerUser(UsersDto dto) {

		if (dto.getFullName() == null || dto.getFullName().isBlank()) {
			throw new UserServiceException(HttpStatus.BAD_REQUEST, "Full name is required");
		}

		if (dto.getEmail() == null || dto.getEmail().isBlank()) {
			throw new UserServiceException(HttpStatus.BAD_REQUEST, "Email is required");
		}

		String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

		if (!dto.getEmail().matches(emailRegex)) {
			throw new UserServiceException(HttpStatus.BAD_REQUEST, "Invalid email format");
		}

		Optional<Users> optEmail = userRepository.findByEmail(dto.getEmail());

		if (optEmail.isPresent()) {
			throw new UserServiceException(HttpStatus.CONFLICT, "Email already registered");
		}

		if (dto.getPassword() == null || dto.getPassword().isBlank()) {
			throw new UserServiceException(HttpStatus.BAD_REQUEST, "Password is required");
		}

		String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";

		if (!dto.getPassword().matches(passwordRegex)) {
			throw new UserServiceException(HttpStatus.BAD_REQUEST,
					"Password must contain uppercase, lowercase, number, special character and minimum 8 characters");
		}

		Users user = new Users();

		user.setFullName(dto.getFullName());
		user.setEmail(dto.getEmail());
		user.setPhoneNo(dto.getPhoneNo());

		user.setPassword(dto.getPassword());

		Users savedUser = userRepository.save(user);

		UserResponseDto response = new UserResponseDto();
		response.setUserId(savedUser.getUserId());
		response.setFullName(savedUser.getFullName());
		response.setEmail(savedUser.getEmail());
		response.setPhoneNo(savedUser.getPhoneNo());

		return response;
	}

	@Override
	public UserResponseDto getUserDetails(int id) {

		Optional<Users> o = userRepository.findById(id);

		if (o.isEmpty()) {
			throw new UserServiceException(HttpStatus.NOT_FOUND, "Please Enter Valid User ID!");
		}

		Users user = o.get();

		UserResponseDto userResponseDto = new UserResponseDto();

		userResponseDto.setUserId(user.getUserId());
		userResponseDto.setFullName(user.getFullName());
		userResponseDto.setPhoneNo(user.getPhoneNo());
		userResponseDto.setEmail(user.getEmail());

		return userResponseDto;
	}
}
