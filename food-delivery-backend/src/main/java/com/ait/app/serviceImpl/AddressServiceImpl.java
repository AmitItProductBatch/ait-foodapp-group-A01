package com.ait.app.serviceImpl;

import com.ait.app.dto.AddressDto;
import com.ait.app.dto.AddressResponseDto;
import com.ait.app.exception.AddressServiceException;
import com.ait.app.exception.UserServiceException;
import com.ait.app.model.Address;
import com.ait.app.model.Users;
import com.ait.app.repository.AddressRepository;
import com.ait.app.repository.UserRepository; // Assuming you have this
import com.ait.app.service.AddressService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public AddressResponseDto createAddress(int userId, AddressDto addressDto) {
        
//verifying if user exists in db
          Users user = userRepository.findById(userId)
            .orElseThrow(() -> new UserServiceException(HttpStatus.NOT_FOUND, "User not found"));


      //mapping data through dto to entitiy
        Address address = new Address();
        address.setLabel(addressDto.getLabel());
        address.setStreet(addressDto.getStreet());
        address.setApartment(addressDto.getApartment());
        address.setLandmark(addressDto.getLandmark());
        address.setCity(addressDto.getCity());
        address.setPostalCode(addressDto.getPostalCode());
        address.setDeliveryInstructions(addressDto.getDeliveryInstructions());
        address.setUser(user);

       
        Address savedAddress = addressRepository.save(address);

       //respose mapping after saving 
        AddressResponseDto response = new AddressResponseDto();
        response.setAddressId(savedAddress.getAddressId());
        response.setLabel(savedAddress.getLabel());
        response.setStreet(savedAddress.getStreet());
        response.setCity(savedAddress.getCity());
        response.setPostalCode(savedAddress.getPostalCode());

        return response;
    }
    
    @Override
	public AddressResponseDto updateAddress(int userId, int addressId, AddressDto addressDto) {

		Optional<Users> user = userRepository.findById(userId);

		if (!user.isPresent())
			throw new UserServiceException(HttpStatus.NOT_FOUND, "User not found");

		Optional<Address> address = addressRepository.findById(addressId);

		if (!address.isPresent())
			throw new AddressServiceException(HttpStatus.NOT_FOUND, "Address not found for id "+addressId);

		Address add = address.get();
		 if (add.getUser() == null || add.getUser().getUserId() != userId) 
		        throw new AddressServiceException(HttpStatus.NOT_FOUND,"Address does not belong to this user");
		        
		add.setLabel(addressDto.getLabel());
		add.setStreet(addressDto.getStreet());
		add.setApartment(addressDto.getApartment());
		add.setLandmark(addressDto.getLandmark());
		add.setCity(addressDto.getCity());
		add.setPostalCode(addressDto.getPostalCode());
		add.setDeliveryInstructions(addressDto.getDeliveryInstructions());

		Address updatedAddress = addressRepository.save(add);
		AddressResponseDto response = new AddressResponseDto();
		response.setAddressId(updatedAddress.getAddressId());
		response.setLabel(updatedAddress.getLabel());
		response.setStreet(updatedAddress.getStreet());
		response.setCity(updatedAddress.getCity());
		response.setPostalCode(updatedAddress.getPostalCode());

		return response;
	
	}

	@Override
	public void deleteAddressByA_IdAndU_Id(int addressId, int userId) {
		Optional<Address> optional = addressRepository.findByAddressIdAndUserUserId(addressId, userId);

		if (optional.isEmpty()) {
			throw new AddressServiceException(HttpStatus.NOT_FOUND,
					"Address is not found for id " + addressId + " User Id :" + userId);
		}
		Address address = optional.get();

		addressRepository.deleteById(address.getAddressId());
		 

	}


    
    
    
    
}
