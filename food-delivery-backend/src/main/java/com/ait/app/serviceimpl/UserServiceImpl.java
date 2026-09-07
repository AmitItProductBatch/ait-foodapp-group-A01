package com.ait.app.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.ait.app.controller.exception.UserConttroller;

import com.ait.app.model.User;
import com.ait.app.repository.UserRepository;
import com.ait.app.service.UserService;
import com.ait.app.userexception.UserException;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Override
	public void registerUser(User user) {
		// TODO Auto-generated method stub

		if (userRepository.existsByEmail(user.getEmail())) {
			throw new UserException(HttpStatus.CONFLICT, "Email Already Registerd");
		}
		if(userRepository.existsByPhoneNo(user.getPhoneNo())) {
			throw new UserException(HttpStatus.CONFLICT, "Phone Number Already Registerd");
		}
		if(user.getFullName()==null) {
			throw new UserException(HttpStatus.BAD_REQUEST,"Enter valid user name" );
		}
		
		if(user.getEmail()==null) {
			throw new UserException(HttpStatus.BAD_REQUEST,"Enter valid mail" );
		}
		
		if(user.getPassword()==null) {
			throw new UserException(HttpStatus.BAD_REQUEST,"Enter valid Password" );
		}

		userRepository.save(user);
	}

//	@Override
//	public ResponseEntity loginUser() {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
