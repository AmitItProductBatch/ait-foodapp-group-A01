package com.ait.app.dto;

import com.ait.app.enums.RoleType;

import lombok.Data;

@Data
public class RoleRequestDto {
	
	private RoleType roleName;
	private int userId;

}
