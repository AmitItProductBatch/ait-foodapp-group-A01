package com.ait.app.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.ait.app.repository.UserRepository;
import com.ait.app.service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Service
public class AddressServiceImpl implements AddressService {
	
	private static final Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private UserRepository userRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public AddressResponseDto createAddress(int userId, AddressDto addressDto) {
		logger.info("Creating loggers for userId :"+userId);
		Users user = userRepository.findById(userId)
				.orElseThrow(() -> new UserServiceException(HttpStatus.NOT_FOUND, "User not found"));

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
		logger.info("Address saved with address id :"+savedAddress.getAddressId());
		AddressResponseDto response = new AddressResponseDto();
		response.setAddressId(savedAddress.getAddressId());
		response.setLabel(savedAddress.getLabel());
		response.setStreet(savedAddress.getStreet());
		response.setCity(savedAddress.getCity());
		response.setPostalCode(savedAddress.getPostalCode());

		return response;
	}

	@Override
	public List<AddressResponseDto> getAllAddresses(int userId) {
		
		logger.info("Fatching all addresses for User id :"+userId);
		userRepository.findById(userId)
				.orElseThrow(() -> new UserServiceException(HttpStatus.NOT_FOUND, "User not found"));

		List<Address> addresses = addressRepository.findByUser_UserId(userId);

		return addresses.stream().map(address -> {
			AddressResponseDto response = new AddressResponseDto();
			response.setAddressId(address.getAddressId());
			response.setLabel(address.getLabel());
			response.setStreet(address.getStreet());
			response.setCity(address.getCity());
			response.setPostalCode(address.getPostalCode());
			return response;
		}).collect(Collectors.toList());
	}

	@Override
	public AddressResponseDto getAddress(int userId, int addressId) {
		
		logger.info("Faching address for user id :"+userId+"address id :"+addressId);
		Address address = addressRepository.findByAddressIdAndUser_UserId(addressId, userId)
				.orElseThrow(() -> new UserServiceException(HttpStatus.NOT_FOUND, "Address not found"));
		
		AddressResponseDto response = new AddressResponseDto();
		response.setAddressId(address.getAddressId());
		response.setLabel(address.getLabel());
		response.setStreet(address.getStreet());
		response.setCity(address.getCity());
		response.setPostalCode(address.getPostalCode());
		
		return response;
	}

	@Override
	@Transactional
	public Address updateAddress(int addressid, int userId, AddressDto dto) {
		
		logger.info("updating address for user id :"+userId+"address Id :"+addressid);
		Optional<Address> optional = addressRepository.findByAddressIdAndUserUserId(addressid, userId);
		if (optional.isEmpty()) {
			logger.info("Address not found for Address Id :"+addressid+"User Id :"+userId);
			throw new AddressServiceException("Address not found for id " + addressid + " for user id " + userId,
					HttpStatus.NOT_FOUND);
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
			sql.append("delivery_instructions = :deliveryInstructions, ");
			hasUpdate = true;
		}

		if (!hasUpdate) {
			return optional.get();
		}

		sql.setLength(sql.length() - 2);

		sql.append(" WHERE address_id = :id AND user_id = :userId");

		Query query = entityManager.createNativeQuery(sql.toString());

		query.setParameter("id", addressid);
		query.setParameter("userId", userId);

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
		
		logger.info("Address updated Successfully for address id :"+addressid+"user id"+userId);

		return entityManager.find(Address.class, addressid);
	}

	@Override
	public void deleteAddressByA_IdAndU_Id(int addressId, int userId) {
		logger.info("delete address for address id :"+addressId+"user id :"+userId);
		Optional<Users> optionalUser = userRepository.findById(userId);
	
		if (optionalUser.isEmpty()) {
			logger.error("user not found for user id :"+userId);

			throw new UserServiceException(HttpStatus.NOT_FOUND, "User not found for id " + userId);
		}

		Optional<Address> optionalAddress = addressRepository.findByAddressIdAndUserUserId(addressId, userId);
		if (optionalAddress.isEmpty()) {
			logger.error("address not found address id :"+addressId);
			throw new AddressServiceException(
					"Address not found for addressId: " + addressId + " and userId: " + userId, HttpStatus.NOT_FOUND);
		}

		addressRepository.delete(optionalAddress.get());
		logger.info("address deleted successfully for address id :"+addressId+"user id :"+userId);

	}
}
