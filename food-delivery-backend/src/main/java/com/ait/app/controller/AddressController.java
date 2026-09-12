package com.ait.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ait.app.dto.AddressDto;
import com.ait.app.dto.AddressResponseDto;
import com.ait.app.model.Address;
import com.ait.app.service.AddressService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class AddressController {

	@Autowired
	private AddressService addressService;

	@PostMapping("/{userId}/addresses")
	public ResponseEntity<AddressResponseDto> createAddress(@PathVariable int userId,
			@Valid @RequestBody AddressDto addressDto) {

		AddressResponseDto createdAddress = addressService.createAddress(userId, addressDto);
		return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
	}

	@PatchMapping("/users/{userId}/addresses/{addressId}")
	public ResponseEntity<Address> updateAddress(@PathVariable int userId,
			@PathVariable int addressId, @RequestBody AddressDto dto) {

		Address address = addressService.updateAddress(addressId, userId, dto);

		return new ResponseEntity<>(address, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{addressId}/{userId}")
	public ResponseEntity deleteAddress(@PathVariable int addressId, @PathVariable int userId) {
		addressService.deleteAddressByA_IdAndU_Id(addressId, userId);
		return new ResponseEntity("Address deleted successfully for id :" + addressId + " User Id :" + userId,
				HttpStatus.OK);

	}

}
