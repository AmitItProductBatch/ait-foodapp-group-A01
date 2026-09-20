package com.ait.app.service;

import java.util.List;

import com.ait.app.dto.AddressDto;
import com.ait.app.dto.AddressResponseDto;
import com.ait.app.model.Address;

public interface AddressService {
     void createAddress(int userId, AddressDto addressDto);

    List<AddressResponseDto> getAllAddresses(int userId);

    AddressResponseDto getAddress(int userId, int addressId);

    public Address updateAddress(int addressid, int userId, AddressDto dto);

    public void deleteAddressByA_IdAndU_Id(int addressId, int userId);
}
