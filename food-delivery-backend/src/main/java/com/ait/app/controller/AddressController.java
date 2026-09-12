package com.ait.app.controller;

import com.ait.app.dto.AddressDto;
import com.ait.app.dto.AddressResponseDto;
import com.ait.app.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


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

    }
