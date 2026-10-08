package com.ait.app.model;

import java.util.List;

import com.ait.app.enums.RoleType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int roleId;
	@Enumerated(EnumType.STRING)
	private RoleType roleName;
	
	@OneToMany(mappedBy = "role")
	private List<Users> user;
	

}
