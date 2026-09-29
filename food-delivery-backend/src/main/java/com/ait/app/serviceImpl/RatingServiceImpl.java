package com.ait.app.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ait.app.dto.RatingRequest;
import com.ait.app.dto.RatingResponse;
import com.ait.app.exception.RatingServiceException;
import com.ait.app.model.Order;
import com.ait.app.model.Rating;
import com.ait.app.model.Restaurant;
import com.ait.app.model.Users;
import com.ait.app.repository.OrderRepository;
import com.ait.app.repository.RatingRepository;
import com.ait.app.repository.RestaurantRepository;
import com.ait.app.repository.UserRepository;
import com.ait.app.service.RatingService;

@Service
public class RatingServiceImpl implements RatingService {

	private static final Logger logger = LoggerFactory.getLogger(RatingServiceImpl.class);

	@Autowired
	private RatingRepository ratingRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Override
	public RatingResponse addRating(RatingRequest request) {

		logger.info("Rating operation started for userId: {}, orderId: {}", request.getUserId(), request.getOrderId());

		Users user = userRepository.findById(request.getUserId()).orElseThrow(() -> {

			logger.error("User not found. userId: {}", request.getUserId());

			return new RatingServiceException("User not found", HttpStatus.NOT_FOUND);
		});

		logger.info("User found successfully. userId: {}", user.getUserId());

		Order order = orderRepository.findById(request.getOrderId()).orElseThrow(() -> {

			logger.error("Order not found. orderId: {}", request.getOrderId());

			return new RatingServiceException("Order not found", HttpStatus.NOT_FOUND);
		});

		logger.info("Order found successfully. orderId: {}", order.getOrderId());

		if (order.getUser().getUserId() != user.getUserId()) {

			logger.warn("Order does not belong to user. orderId: {}, userId: {}", request.getOrderId(),
					request.getUserId());

			throw new RatingServiceException("Order does not belong to this user", HttpStatus.BAD_REQUEST);
		}

		if (request.getRatingValue() < 1 || request.getRatingValue() > 5) {

			logger.warn("Invalid rating value: {} for orderId: {}", request.getRatingValue(), request.getOrderId());

			throw new RatingServiceException("Rating must be between 1 and 5", HttpStatus.BAD_REQUEST);
		}

		if (!order.getOrderStatus().name().equals("DELIVERED")) {

			logger.warn("Order is not delivered. orderId: {}", request.getOrderId());

			throw new RatingServiceException("Rating is allowed only for delivered orders", HttpStatus.BAD_REQUEST);
		}

		Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId()).orElseThrow(() -> {

			logger.error("Restaurant not found. restaurantId: {}", request.getRestaurantId());

			return new RatingServiceException("Restaurant not found", HttpStatus.NOT_FOUND);
		});

		logger.info("Restaurant found successfully. restaurantId: {}", restaurant.getId());

		Rating rating = ratingRepository.findByOrderOrderId(request.getOrderId()).orElse(null);

		if (rating == null) {

			logger.info("Creating new rating for orderId: {}", request.getOrderId());

			rating = new Rating();

			rating.setUser(user);
			rating.setOrder(order);
			rating.setRestaurant(restaurant);
			order.setRating(rating);
			

		} else {

			logger.info("Existing rating found. Updating rating for orderId: {}", request.getOrderId());
		}

		rating.setRatingValue(request.getRatingValue());

		Rating savedRating = ratingRepository.save(rating);

		logger.info("Rating saved successfully. ratingId: {}, orderId: {}, ratingValue: {}", savedRating.getRatingid(),
				savedRating.getOrder().getOrderId(), savedRating.getRatingValue());

		return convertToResponse(savedRating);
	}

	private RatingResponse convertToResponse(Rating rating) {

		logger.debug("Converting Rating entity to RatingResponse. ratingId: {}", rating.getRatingid());

		RatingResponse response = new RatingResponse();

		response.setRatingId(rating.getRatingid());
		response.setUserId(rating.getUser().getUserId());
		response.setOrderId(rating.getOrder().getOrderId());
		response.setRestaurantId(rating.getRestaurant().getId());
		response.setRatingValue(rating.getRatingValue());

		return response;
	}

}