package com.ait.app.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;

import com.ait.app.dto.RoleRequestDto;
import com.ait.app.exception.RoleServiceException;
import com.ait.app.model.Role;
import com.ait.app.model.Users;
import com.ait.app.repository.RoleRepository;
import com.ait.app.repository.UserRepository;
import com.ait.app.service.RoleService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	UserRepository userRepository;

	@Override
	public void createRole(int userId, RoleRequestDto dto) {

		log.info("Role creation started for userId: {}", userId);

		if (dto == null) {
			log.error("Role request is null");

			throw new RoleServiceException(HttpStatus.BAD_REQUEST, "Role request cannot be null");
		}

		if (dto.getRoleName() == null) {
			log.error("Role name is required");

			throw new RoleServiceException(HttpStatus.BAD_REQUEST, "Role name is required");
		}

		Users user = userRepository.findById(userId)
				.orElseThrow(() -> new RoleServiceException(HttpStatus.NOT_FOUND, "User not found with id: " + userId));

		if (user.getRole() != null) {

			log.warn("User {} already has role {}", userId, user.getRole().getRoleName());

			throw new RoleServiceException(HttpStatus.CONFLICT,
					"User already has a role assigned: " + user.getRole().getRoleName());
		}

		if (user.getRole() != null && user.getRole().getRoleName().equals(dto.getRoleName())) {

			log.warn("User {} already has role {}", userId, dto.getRoleName());

			throw new RoleServiceException(HttpStatus.CONFLICT, "User already has this role: " + dto.getRoleName());
		}

		Role role = roleRepository.findByRoleName(dto.getRoleName());

		if (role == null) {

			log.info("Creating role: {}", dto.getRoleName());

			role = new Role();
			role.setRoleName(dto.getRoleName());

			role = roleRepository.save(role);
		}

		user.setRole(role);

		userRepository.save(user);

		log.info("Role {} assigned successfully to userId: {}", dto.getRoleName(), userId);
	}
}
