package com.ait.app.service;

import com.ait.app.dto.AddressDto;
import com.ait.app.dto.AddressResponseDto;
import com.ait.app.model.Address;

public interface AddressService {
	AddressResponseDto createAddress(int userId, AddressDto addressDto);


	public Address updateAddress(int addressid, int userId, AddressDto dto);

	public void deleteAddressByA_IdAndU_Id(int addressId, int userId);

}
