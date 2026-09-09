package com.ait.app.serviceImpl;

import com.ait.app.dto.AddressDto;
import com.ait.app.dto.AddressResponseDto;
import com.ait.app.exception.UserServiceException;
import com.ait.app.model.Address;
import com.ait.app.model.Users;
import com.ait.app.repository.AddressRepository;
import com.ait.app.repository.UserRepository; // Assuming you have this
import com.ait.app.service.AddressService;
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
}
