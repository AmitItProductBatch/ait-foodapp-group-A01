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

	private static final Logger log = LoggerFactory.getLogger(AddressServiceImpl.class);

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private UserRepository userRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void createAddress(int userId, AddressDto addressDto) {

		log.info("Creating address for userId: {}", userId);

		Users user = userRepository.findById(userId).orElseThrow(() -> {
			log.error("User not found with userId: {}", userId);
			return new UserServiceException(HttpStatus.NOT_FOUND, "User not found");
		});

		log.info("User found successfully with userId: {}", userId);

		Address address = new Address();

		address.setLabel(addressDto.getLabel());
		address.setStreet(addressDto.getStreet());
		address.setApartment(addressDto.getApartment());
		address.setLandmark(addressDto.getLandmark());
		address.setCity(addressDto.getCity());
		address.setPostalCode(addressDto.getPostalCode());
		address.setDeliveryInstructions(addressDto.getDeliveryInstructions());
		address.setUser(user);

		log.info("Saving address for userId: {}", userId);

		Address savedAddress = addressRepository.save(address);

		if (savedAddress != null) {

			log.info("Address created successfully with addressId: {} for userId: {}", savedAddress.getAddressId(),
					userId);

		} else {

			log.error("Failed to save address for userId: {}", userId);
		}
	}

	@Override
	public List<AddressResponseDto> getAllAddresses(int userId) {

		log.info("Fetching all addresses for userId: {}", userId);

		userRepository.findById(userId).orElseThrow(() -> {
			log.error("User not found with userId: {}", userId);
			return new UserServiceException(HttpStatus.NOT_FOUND, "User not found");
		});

		log.info("User found successfully with userId: {}", userId);

		List<Address> addresses = addressRepository.findByUser_UserId(userId);

		if (addresses.isEmpty()) {

			log.info("No addresses found for userId: {}", userId);

		} else {

			log.info("Found {} addresses for userId: {}", addresses.size(), userId);
		}

		return addresses.stream().map(address -> {

			log.debug("Mapping addressId: {} for userId: {}", address.getAddressId(), userId);

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

		log.info("Fetching address for userId: {} and addressId: {}", userId, addressId);

		Address address = addressRepository.findByAddressIdAndUser_UserId(addressId, userId).orElseThrow(() -> {

			log.error("Address not found with addressId: {} and userId: {}", addressId, userId);

			return new UserServiceException(HttpStatus.NOT_FOUND, "Address not found");
		});

		log.info("Address found successfully with addressId: {} and userId: {}", addressId, userId);

		AddressResponseDto response = new AddressResponseDto();

		response.setAddressId(address.getAddressId());
		response.setLabel(address.getLabel());
		response.setStreet(address.getStreet());
		response.setCity(address.getCity());
		response.setPostalCode(address.getPostalCode());

		log.info("Address response created successfully for addressId: {}", addressId);

		return response;
	}

	@Override
	@Transactional
	public Address updateAddress(int addressid, int userId, AddressDto dto) {

		log.info("Updating address for addressId: {} and userId: {}", addressid, userId);

		Optional<Address> optional = addressRepository.findByAddressIdAndUserUserId(addressid, userId);

		if (optional.isEmpty()) {

			log.error("Address not found for addressId: {} and userId: {}", addressid, userId);

			throw new AddressServiceException("Address not found for id " + addressid + " for user id " + userId,
					HttpStatus.NOT_FOUND);
		}

		log.info("Address found successfully for addressId: {} and userId: {}", addressid, userId);

		StringBuilder sql = new StringBuilder("UPDATE addresses SET ");

		boolean hasUpdate = false;

		if (dto.getLabel() != null && !dto.getLabel().isBlank()) {

			log.debug("Label will be updated for addressId: {}", addressid);

			sql.append("label = :label, ");
			hasUpdate = true;
		}

		if (dto.getStreet() != null && !dto.getStreet().isBlank()) {

			log.debug("Street will be updated for addressId: {}", addressid);

			sql.append("street = :street, ");
			hasUpdate = true;
		}

		if (dto.getApartment() != null && !dto.getApartment().isBlank()) {

			log.debug("Apartment will be updated for addressId: {}", addressid);

			sql.append("apartment = :apartment, ");
			hasUpdate = true;
		}

		if (dto.getLandmark() != null && !dto.getLandmark().isBlank()) {

			log.debug("Landmark will be updated for addressId: {}", addressid);

			sql.append("landmark = :landmark, ");
			hasUpdate = true;
		}

		if (dto.getCity() != null && !dto.getCity().isBlank()) {

			log.debug("City will be updated for addressId: {}", addressid);

			sql.append("city = :city, ");
			hasUpdate = true;
		}

		if (dto.getPostalCode() != null && !dto.getPostalCode().isBlank()) {

			log.debug("Postal code will be updated for addressId: {}", addressid);

			sql.append("postal_code = :postalCode, ");
			hasUpdate = true;
		}

		if (dto.getDeliveryInstructions() != null && !dto.getDeliveryInstructions().isBlank()) {

			log.debug("Delivery instructions will be updated for addressId: {}", addressid);

			sql.append("delivery_instructions = :deliveryInstructions, ");

			hasUpdate = true;
		}

		if (!hasUpdate) {

			log.warn("No valid fields provided for update for addressId: {} and userId: {}", addressid, userId);

			return optional.get();
		}

		sql.setLength(sql.length() - 2);

		sql.append(" WHERE address_id = :id AND user_id = :userId");

		log.debug("Executing update query for addressId: {} and userId: {}", addressid, userId);

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

		int rowsUpdated = query.executeUpdate();

		if (rowsUpdated > 0) {

			log.info("Address updated successfully. addressId: {}, userId: {}, rowsUpdated: {}", addressid, userId,
					rowsUpdated);

		} else {

			log.warn("No address record updated for addressId: {} and userId: {}", addressid, userId);
		}

		entityManager.clear();

		Address updatedAddress = entityManager.find(Address.class, addressid);

		if (updatedAddress != null) {

			log.info("Updated address retrieved successfully for addressId: {}", addressid);

		} else {

			log.error("Failed to retrieve updated address for addressId: {}", addressid);
		}

		return updatedAddress;
	}

	@Override
	public void deleteAddressByA_IdAndU_Id(int addressId, int userId) {

		log.info("Deleting address for addressId: {} and userId: {}", addressId, userId);

		Optional<Users> optionalUser = userRepository.findById(userId);

		if (optionalUser.isEmpty()) {

			log.error("User not found with userId: {}", userId);

			throw new UserServiceException(HttpStatus.NOT_FOUND, "User not found for id " + userId);
		}

		log.info("User found successfully with userId: {}", userId);

		Optional<Address> optionalAddress = addressRepository.findByAddressIdAndUserUserId(addressId, userId);

		if (optionalAddress.isEmpty()) {

			log.error("Address not found with addressId: {} and userId: {}", addressId, userId);

			throw new AddressServiceException(
					"Address not found for addressId: " + addressId + " and userId: " + userId, HttpStatus.NOT_FOUND);
		}

		log.info("Address found successfully with addressId: {} and userId: {}", addressId, userId);

		addressRepository.delete(optionalAddress.get());

		log.info("Address deleted successfully for addressId: {} and userId: {}", addressId, userId);
	}
}