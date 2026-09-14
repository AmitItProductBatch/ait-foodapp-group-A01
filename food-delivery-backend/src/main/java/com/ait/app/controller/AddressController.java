package com.ait.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ait.app.dto.AddressDto;
import com.ait.app.dto.AddressResponseDto;
import com.ait.app.model.Address;
import com.ait.app.service.AddressService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping("/{userId}/addresses")
    public ResponseEntity<AddressResponseDto> createAddress(
            @PathVariable int userId, 
            @Valid @RequestBody AddressDto addressDto) {
        AddressResponseDto createdAddress = addressService.createAddress(userId, addressDto);
        return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}/addresses")
    public ResponseEntity<List<AddressResponseDto>> getAllAddresses(@PathVariable int userId) {
        List<AddressResponseDto> addresses = addressService.getAllAddresses(userId);
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @GetMapping("/{userId}/addresses/{addressId}")
    public ResponseEntity<AddressResponseDto> getAddress(
            @PathVariable int userId,
            @PathVariable int addressId) {
        AddressResponseDto address = addressService.getAddress(userId, addressId);
        return new ResponseEntity<>(address, HttpStatus.OK);
    }

    @PatchMapping("/{userId}/addresses/{addressId}")
    public ResponseEntity<Address> updateAddress(@PathVariable int userId,
            @PathVariable int addressId, @RequestBody AddressDto dto) {
        Address address = addressService.updateAddress(addressId, userId, dto);
        return new ResponseEntity<>(address, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{addressId}/{userId}")
    public ResponseEntity<String> deleteAddress(@PathVariable int addressId, @PathVariable int userId) {
        addressService.deleteAddressByA_IdAndU_Id(addressId, userId);
        return new ResponseEntity<>("Address deleted successfully for id :" + addressId + " User Id :" + userId,
                HttpStatus.OK);
    }
}
