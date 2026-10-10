package com.ait.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ait.app.dto.RoleRequestDto;
import com.ait.app.service.RoleService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/roles")
@Slf4j
public class RoleController {
	
	 

	    @Autowired
	    private RoleService roleService;

	    @PostMapping("/userId/{userId}")
	    public ResponseEntity<String> createRole(
	            @PathVariable int userId,
	            @RequestBody RoleRequestDto dto) {

	        log.info("Create role API started for userId: {}", userId);

	        roleService.createRole(userId, dto);

	        log.info("Role assigned successfully to userId: {}", userId);

	        return new ResponseEntity<>(
	                "Role created and assigned successfully",
	                HttpStatus.CREATED
	        );
	    }
	}


