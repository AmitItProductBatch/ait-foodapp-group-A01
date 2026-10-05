package com.ait.app.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import com.ait.app.dto.FeedbackDto;
import com.ait.app.dto.FeedbackResponseDto;
import com.ait.app.exception.FeedbackServiceException;
import com.ait.app.exception.OrderServiceException;
import com.ait.app.exception.RestaurantServiceException;
import com.ait.app.model.Feedback;
import com.ait.app.model.Order;
import com.ait.app.model.Restaurant;
import com.ait.app.repository.FeedbackRepository;
import com.ait.app.repository.OrderRepository;
import com.ait.app.repository.RestaurantRepository;
import com.ait.app.service.FeedbackService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FeedbackServiceImpl implements FeedbackService {

	@Autowired
	FeedbackRepository feedbackRepository;
	@Autowired
	OrderRepository orderRepository;
	@Autowired
	RestaurantRepository restaurantRepository;

	@Override
	public Feedback createFeedBack(FeedbackDto dto) {

		log.info("createFeedBack method execution started. orderId: {}, restaurantId: {}", dto.getOrderId(),
				dto.getRestaurantId());

		log.info("Finding Order in database for orderId: {}", dto.getOrderId());

		Optional<Order> optional = orderRepository.findById(dto.getOrderId());

		if (optional.isEmpty()) {
			log.error("Order not found for orderId: {}", dto.getOrderId());

			throw new OrderServiceException("Order is not found for order id: " + dto.getOrderId(),
					HttpStatus.NOT_FOUND);
		}

		log.info("Order found successfully for orderId: {}", dto.getOrderId());

		log.info("Finding Restaurant in database for restaurantId: {}", dto.getRestaurantId());

		Optional<Restaurant> optionalR = restaurantRepository.findById((long) dto.getRestaurantId());

		if (optionalR.isEmpty()) {
			log.error("Restaurant not found for restaurantId: {}", dto.getRestaurantId());

			throw new RestaurantServiceException(HttpStatus.NOT_FOUND,
					"Restaurant is not found for restaurant id: " + dto.getRestaurantId());
		}

		log.info("Restaurant found successfully for restaurantId: {}", dto.getRestaurantId());

		if (dto.getContent() == null || dto.getContent().isBlank()) {

			log.error("Feedback content is null or blank");

			throw new FeedbackServiceException("Content is Mandatory", HttpStatus.BAD_REQUEST);
		}

		if (dto.getRating() < 1 || dto.getRating() > 5) {

			log.error("Invalid rating: {}", dto.getRating());

			throw new FeedbackServiceException("Rating must be between 1 and 5", HttpStatus.BAD_REQUEST);
		}

		Feedback feedback = new Feedback();

		feedback.setOrderId(optional.get().getOrderId());
		feedback.setRestaurantId(optionalR.get().getId());
		feedback.setContent(dto.getContent());
		feedback.setRating(dto.getRating());

		Feedback savedFeedback = feedbackRepository.save(feedback);

		log.info("Feedback created successfully. feedbackId: {}, orderId: {}, restaurantId: {}",
				savedFeedback.getFeedbackId(), savedFeedback.getOrderId(), savedFeedback.getRestaurantId());

		return savedFeedback;
	}

	@Override
	public Page<FeedbackResponseDto> getFeedbackByRestaurantId(long restaurantId, int page, int size) {
		log.info("getFeedbackByRestaurantId method execution started. restaurantId: {}, page: {}, size: {}",
				restaurantId, page, size);

		Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
		if (optionalRestaurant.isEmpty()) {
			log.error("Restaurant not found for restaurantId: {}", restaurantId);
			throw new RestaurantServiceException(HttpStatus.NOT_FOUND, "Restaurant not found for id: " + restaurantId);
		}
		log.info("Restaurant found successfully for restaurantId: {}", restaurantId);
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<Feedback> feedbackPage = feedbackRepository
				.findByRestaurantIdAndIsDeletedFalseAndIsFlaggedFalse(restaurantId, pageable);
		log.info("Found {} feedback entries for restaurantId: {}", feedbackPage.getTotalElements(), restaurantId);

		Page<FeedbackResponseDto> responsePage = feedbackPage
				.map(feedback -> new FeedbackResponseDto(feedback.getFeedbackId(), feedback.getOrderId(),
						feedback.getRestaurantId(), feedback.getContent(), feedback.getRating(),
						feedback.getCreatedAt()));
		return responsePage;
	}

	@Override
	public Feedback updateFeedBack(int feedbackId, FeedbackDto dto) {

		log.info("updateFeedBack method execution started. feedbackId: {}", feedbackId);

		Optional<Feedback> optional = feedbackRepository.findById(feedbackId);

		if (optional.isEmpty()) {
			log.error("Feedback not found for feedbackId: {}", feedbackId);

			throw new FeedbackServiceException("Feedback not found for feedback id: " + feedbackId,
					HttpStatus.NOT_FOUND);
		}

		Feedback feedback = optional.get();

		if (dto.getContent() == null || dto.getContent().isBlank()) {

			log.error("Feedback content is null or blank");

			throw new FeedbackServiceException("Content is Mandatory", HttpStatus.BAD_REQUEST);
		}

		if (dto.getRating() < 1 || dto.getRating() > 5) {

			log.error("Invalid rating: {}", dto.getRating());

			throw new FeedbackServiceException("Rating must be between 1 and 5", HttpStatus.BAD_REQUEST);
		}

		feedback.setContent(dto.getContent());
		feedback.setRating(dto.getRating());

		Feedback updatedFeedback = feedbackRepository.save(feedback);

		log.info("Feedback updated successfully. feedbackId: {}", feedbackId);

		return updatedFeedback;
	}

	@Override
	public void deleteFeedBack(int feedbackId) {

		log.info("deleteFeedBack method execution started. feedbackId: {}", feedbackId);

		Optional<Feedback> optional = feedbackRepository.findById(feedbackId);

		if (optional.isEmpty()) {

			log.error("Feedback not found for feedbackId: {}", feedbackId);

			throw new FeedbackServiceException("Feedback not found for feedback id: " + feedbackId,
					HttpStatus.NOT_FOUND);
		}

		Feedback feedback = optional.get();

		feedbackRepository.save(feedback);

		log.info("Feedback deleted successfully. feedbackId: {}", feedbackId);
	}

}
