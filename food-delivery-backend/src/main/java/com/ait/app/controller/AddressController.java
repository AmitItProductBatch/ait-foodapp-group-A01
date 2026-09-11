package com.ait.app.controller;

import com.ait.app.dto.AddressDto;
import com.ait.app.dto.AddressResponseDto;
import com.ait.app.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

	@DeleteMapping("/delete/{addressId}/{userId}")
	public ResponseEntity deleteAddressByA_IdAndU_Id(@PathVariable int addressId, @PathVariable int userId) {
		addressService.deleteAddressByA_IdAndU_Id(addressId, userId);
		return new ResponseEntity("Address deleted successfully for id :" + addressId + " User Id :" + userId,
				HttpStatus.OK);

	}
}
