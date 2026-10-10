package com.ait.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ait.app.enums.RoleType;
import com.ait.app.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

	Role findByRoleName(RoleType roleName);

}
