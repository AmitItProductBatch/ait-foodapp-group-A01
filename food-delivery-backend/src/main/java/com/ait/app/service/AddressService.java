package com.ait.app.service;

import com.ait.app.dto.AddressResponseDto;
import com.ait.app.dto.AddressDto;
import java.util.List;



public interface AddressService {
      AddressResponseDto createAddress(int userId, AddressDto addressDto);
     List< AddressResponseDto>getAllAddresses(int userId);
     AddressResponseDto getAddress(int userId, int addressId);
}
