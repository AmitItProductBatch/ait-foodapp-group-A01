package com.ait.app.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ait.app.dto.UpdateUserDto;
import com.ait.app.dto.UserResponseDto;
import com.ait.app.dto.UsersDto;
import com.ait.app.exception.UserServiceException;
import com.ait.app.model.Users;
import com.ait.app.repository.UserRepository;
import com.ait.app.service.UserService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@PersistenceContext
	private EntityManager entityManager;

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

	@Override
	@Transactional
	public Users updateUserById(int id, UpdateUserDto dto) {

		Optional<Users> optional = userRepository.findById(id);

		if (optional.isEmpty()) {
			throw new UserServiceException(HttpStatus.NOT_FOUND, "User is not found for id " + id);
		}

		StringBuilder sql = new StringBuilder("UPDATE food_delivery_users SET ");
		boolean hasUpdate = false;

		if (dto.getFullName() != null && !dto.getFullName().isBlank()) {
			sql.append("full_name = :fullName, ");
			hasUpdate = true;
		}

		if (dto.getPhoneNo() != 0) {
			sql.append("phone_no = :phoneNo, ");
			hasUpdate = true;
		}

		if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
			sql.append("email = :email, ");
			hasUpdate = true;
		}

		if (!hasUpdate) {
			return optional.get();
		}

		sql.setLength(sql.length() - 2);

		sql.append(" WHERE user_id = :id");

		Query query = entityManager.createNativeQuery(sql.toString());

		query.setParameter("id", id);

		if (dto.getFullName() != null && !dto.getFullName().isBlank()) {
			query.setParameter("fullName", dto.getFullName());
		}

		if (dto.getPhoneNo() != 0) {
			query.setParameter("phoneNo", dto.getPhoneNo());
		}

		if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
			query.setParameter("email", dto.getEmail());
		}

		query.executeUpdate();

		entityManager.clear();

		return entityManager.find(Users.class, id);
	}

	@Override
	public UserResponseDto deleteUserByID(int id, UsersDto dto) {
		 Optional<Users> optional = userRepository.findById(id);

		    if (optional.isEmpty()) {
		        throw new UserServiceException(HttpStatus.NOT_FOUND,"User is not found for id: " + id);
		    }

		    Users user = optional.get();

		    if (!user.getEmail().equals(dto.getEmail()) || !user.getPassword().equals(dto.getPassword())) {

		        throw new UserServiceException( HttpStatus.UNAUTHORIZED, "Invalid email or password" );
		    }

		    UserResponseDto response = new UserResponseDto();

		    response.setUserId(user.getUserId());
		    response.setFullName(user.getFullName());
		    response.setEmail(user.getEmail());
		    response.setPhoneNo(user.getPhoneNo());

		    userRepository.deleteById(id);

		    return response;
	}
}
