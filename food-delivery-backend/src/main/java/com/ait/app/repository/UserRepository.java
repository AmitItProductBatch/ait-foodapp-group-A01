package com.ait.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ait.app.model.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
	Optional<Users> findByEmail(String email);

	boolean existsByEmail(String email);

	Optional<Users> existsByPhoneNo(long phoneNo);

	boolean existsByPhoneNoAndUserIdNot(long phoneNo, int userId);
}
