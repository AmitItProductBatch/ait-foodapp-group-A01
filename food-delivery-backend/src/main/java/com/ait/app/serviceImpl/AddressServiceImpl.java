package com.ait.app.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ait.app.dto.AddressDto;
import com.ait.app.dto.AddressResponseDto;
import com.ait.app.exception.AddressServiceException;
import com.ait.app.exception.UserServiceException;
import com.ait.app.model.Address;
import com.ait.app.model.Users;
import com.ait.app.repository.AddressRepository;
import com.ait.app.repository.UserRepository; // Assuming you have this
import com.ait.app.service.AddressService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private UserRepository userRepository;
	
	@PersistenceContext
	private EntityManager entityManager;


	@Override
	public AddressResponseDto createAddress(int userId, AddressDto addressDto) {

//verifying if user exists in db
		Users user = userRepository.findById(userId)
				.orElseThrow(() -> new UserServiceException(HttpStatus.NOT_FOUND, "User not found"));

		// mapping data through dto to entitiy
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

		// respose mapping after saving
		AddressResponseDto response = new AddressResponseDto();
		response.setAddressId(savedAddress.getAddressId());
		response.setLabel(savedAddress.getLabel());
		response.setStreet(savedAddress.getStreet());
		response.setCity(savedAddress.getCity());
		response.setPostalCode(savedAddress.getPostalCode());

		return response;
	}
	
	
	@Override
	@Transactional
	public Address updateAddress(int addressid, AddressDto dto) {

		Optional<Address> optional = addressRepository.findById(addressid);

		if (optional.isEmpty()) {
			throw new AddressServiceException("Address not found for id " + addressid, HttpStatus.NOT_FOUND);
		}

		StringBuilder sql = new StringBuilder("UPDATE addresses SET ");

		boolean hasUpdate = false;

		if (dto.getLabel() != null && !dto.getLabel().isBlank()) {
			sql.append("label = :label, ");
			hasUpdate = true;
		}

		if (dto.getStreet() != null && !dto.getStreet().isBlank()) {
			sql.append("street = :street, ");
			hasUpdate = true;
		}

		if (dto.getApartment() != null && !dto.getApartment().isBlank()) {
			sql.append("apartment = :apartment, ");
			hasUpdate = true;
		}

		if (dto.getLandmark() != null && !dto.getLandmark().isBlank()) {
			sql.append("landmark = :landmark, ");
			hasUpdate = true;
		}

		if (dto.getCity() != null && !dto.getCity().isBlank()) {
			sql.append("city = :city, ");
			hasUpdate = true;
		}


		if (dto.getPostalCode() != null && !dto.getPostalCode().isBlank()) {
		    sql.append("postal_code = :postalCode, ");
		    hasUpdate = true;
		}
		
		
		if (dto.getDeliveryInstructions() != null && !dto.getDeliveryInstructions().isBlank()) {

			sql.append("delivery_Instructions = :deliveryInstructions, ");
			hasUpdate = true;
		}

		if (!hasUpdate) {
			return optional.get();
		}

		sql.setLength(sql.length() - 2);

		sql.append(" WHERE address_id = :id");

		Query query = entityManager.createNativeQuery(sql.toString());

		query.setParameter("id", addressid);

		if (dto.getLabel() != null && !dto.getLabel().isBlank()) {
			query.setParameter("label", dto.getLabel());
		}

		if (dto.getStreet() != null && !dto.getStreet().isBlank()) {
			query.setParameter("street", dto.getStreet());
		}

		if (dto.getApartment() != null && !dto.getApartment().isBlank()) {
			query.setParameter("apartment", dto.getApartment());
		}

		if (dto.getLandmark() != null && !dto.getLandmark().isBlank()) {
			query.setParameter("landmark", dto.getLandmark());
		}

		if (dto.getCity() != null && !dto.getCity().isBlank()) {
			query.setParameter("city", dto.getCity());
		}

		if (dto.getPostalCode() != null && !dto.getPostalCode().isBlank()) {
			query.setParameter("postalCode", dto.getPostalCode());
		}

		if (dto.getDeliveryInstructions() != null && !dto.getDeliveryInstructions().isBlank()) {

			query.setParameter("deliveryInstructions", dto.getDeliveryInstructions());
		}

		query.executeUpdate();

		entityManager.clear();

		return entityManager.find(Address.class, addressid);
	}
	
	

	@Override
	public void deleteAddressByA_IdAndU_Id(int addressId, int userId) {

		Optional<Users> optionalUser = userRepository.findById(userId);

		if (optionalUser.isEmpty()) {
			throw new UserServiceException(HttpStatus.NOT_FOUND, "User not found for id " + userId);
		}

		Optional<Address> optionalAddress = addressRepository.findByAddressIdAndUserUserId(addressId, userId);

		if (optionalAddress.isEmpty()) {
			throw new AddressServiceException(
					"Address not found for addressId: " + addressId + " and userId: " + userId, HttpStatus.NOT_FOUND);
		}

		addressRepository.delete(optionalAddress.get());
	}

}
