package com.ait.app.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

}

//@Service
//public class UserServiceImpl implements UserService {
//
//	@Autowired
//	UserRepository userRepository;
//
//	@Override
//	public void registerUser(UsersDto user) {]
//			
//			// Validate name 
//			if (dto.getFullName() == null || dto.getFullName().isBlank()) 
//			{ 
//				throw new UserServiceException( HttpStatus.BAD_REQUEST, "Full name is required"); 
//				
//			}
//
//	}
//
////	@Override
////	public void registerUser(UsersDto dto) {
////
////		Optional<Users> optEmail = userRepository.findByEmail(dto.getEmail());
////
////		if (optEmail.isPresent()) {
////			throw new UserServiceException(HttpStatus.CONFLICT, "Email already registered");
////		}
////
////		if (dto.getFullName().isBlank()) {
////			throw new UserServiceException(HttpStatus.CONFLICT, "Enter Valid Name");
////		}
////
////		if (dto.getEmail().isBlank()) {
////			throw new UserServiceException(HttpStatus.CONFLICT, "Enter Valid Email");
////		}
////
////		String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
////
////		if (!dto.getPassword().matches(passwordRegex)) {
////			throw new UserServiceException(HttpStatus.BAD_REQUEST,
////					"Password must contain uppercase, lowercase, number, special character and be at least 8 characters");
////		}
////		if (dto.getPassword().isBlank()) {
////			throw new UserServiceException(HttpStatus.CONFLICT, "Enter Valid Password");
////		}
////
////		String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
////		if (!dto.getEmail().matches(emailRegex)) {
////			throw new UserServiceException(HttpStatus.BAD_REQUEST, "Invalid email format");
////		}
////		if (dto.getEmail().isBlank()) {
////			throw new UserServiceException(HttpStatus.CONFLICT, "Enter Valid Email");
////		}
////
////		Users u = optEmail.get();
////		u.setEmail(dto.getEmail());
////		u.setFullName(dto.getFullName());
////		u.setPhoneNo(dto.getPhoneNo());
////		u.setPassword(dto.getPassword());
////
////		userRepository.save(u);
////
////		// if (userRepository.existsByEmail(user.getEmail())) {
//////			throw new UserException(HttpStatus.CONFLICT, "Email Already Registerd");
//////		}
//////
//////		if (userRepository.existsByPhoneNo(user.getPhoneNo())) {
//////			throw new UserException(HttpStatus.CONFLICT, "Phone Number Already Registerd");
//////		}
//////
//////		if (user.getFullName().isBlank()) {
//////			throw new UserException(HttpStatus.CONFLICT, "Enter Valid Name");
//////		}
//////		if (user.getEmail().isBlank()) {
//////			throw new UserException(HttpStatus.CONFLICT, "Enter Valid Email");
//////		}
//////		String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
//////		if (user.getEmail().matches(emailRegex)) {
//////			throw new UserException(HttpStatus.BAD_REQUEST, "Invalid email format");
//////		}
//////		if (user.getPassword().isBlank()) {
//////			throw new UserException(HttpStatus.CONFLICT, "Enter Valid Password");
//////		}
//////
//////		String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
//////
//////		if (user.getPassword().matches(passwordRegex)) {
//////			throw new UserException(HttpStatus.BAD_REQUEST,
//////					"Password must contain uppercase, lowercase, number, special character and be at least 8 characters");
//////		}
////
////	}
////	
////	
//
////	@Override
////	public ResponseEntity loginUser() {
////		// TODO Auto-generated method stub
////		return null;
////	}
//
//}
