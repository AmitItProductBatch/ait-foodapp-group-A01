package com.ait.app.service;

import com.ait.app.dto.AddressResponseDto;
import com.ait.app.dto.AddressDto;


public interface AddressService {
      AddressResponseDto createAddress(int userId, AddressDto addressDto);
      public AddressResponseDto updateAddress(int userId, int addressId, AddressDto addressDto);
      public void deleteAddressByA_IdAndU_Id(int userId, int addressId);

}
