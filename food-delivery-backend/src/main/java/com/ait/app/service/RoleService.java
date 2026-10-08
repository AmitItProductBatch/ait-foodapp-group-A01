package com.ait.app.service;

import com.ait.app.dto.RoleRequestDto;
import com.ait.app.model.Role;

public interface RoleService {
	
	public void createRole(int userId,RoleRequestDto dto);

}
