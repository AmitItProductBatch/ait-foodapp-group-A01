package com.ait.app.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ait.app.dto.OrderRequest;
import com.ait.app.dto.OrderResponse;
import com.ait.app.dto.OrderSummaryResponse;
import com.ait.app.enums.OrderStatus;
import com.ait.app.enums.PaymentStatus;
import com.ait.app.exception.OrderServiceException;
import com.ait.app.model.Address;
import com.ait.app.model.Cart;
import com.ait.app.model.CartItem;
import com.ait.app.model.Order;
import com.ait.app.model.Users;
import com.ait.app.repository.AddressRepository;
import com.ait.app.repository.CartItemRepository;
import com.ait.app.repository.CartRepository;
import com.ait.app.repository.OrderRepository;
import com.ait.app.repository.UserRepository;
import com.ait.app.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Override
	@Transactional
	public OrderResponse createOrder(int userId, OrderRequest request) {

		logger.info("Creating order for user id: {}", userId);

		Users user = userRepository.findById(userId).orElseThrow(() -> {
			logger.error("User not found with id: {}", userId);

			return new OrderServiceException("User not found with id: " + userId, HttpStatus.NOT_FOUND);
		});

		Cart cart = cartRepository.findByUserUserId(userId).orElseThrow(() -> {
			logger.error("Cart not found for user id: {}", userId);

			return new OrderServiceException("Cart not found for user id: " + userId, HttpStatus.NOT_FOUND);
		});

		Address address = addressRepository.findById(request.getAddressId()).orElseThrow(() -> {
			logger.error("Address not found with id: {}", request.getAddressId());

			return new OrderServiceException("Address not found with id: " + request.getAddressId(),
					HttpStatus.NOT_FOUND);
		});

		if (address.getUser().getUserId() != userId) {

			logger.error("Address id: {} does not belong to user id: {}", request.getAddressId(), userId);

			throw new OrderServiceException("Address does not belong to user", HttpStatus.BAD_REQUEST);
		}

		List<CartItem> cartItems = cartItemRepository.findByCartCartId(cart.getCartId());

		if (cartItems == null || cartItems.isEmpty()) {

			logger.error("Cart is empty for user id: {}", userId);

			throw new OrderServiceException("Cart is empty", HttpStatus.BAD_REQUEST);
		}

		Order order = new Order();

		order.setUser(user);
		order.setAddress(address);
		order.setTotalAmount(cart.getTotalAmount());
		order.setOrderDate(LocalDateTime.now());
		order.setOrderStatus(OrderStatus.PLACED);
		order.setPaymentStatus(PaymentStatus.PENDING);

		Order savedOrder = orderRepository.save(order);

		logger.info("Order created successfully. Order id: {}, User id: {}", savedOrder.getOrderId(), userId);

		return convertToOrderResponse(savedOrder);
	}

	@Override
	@Transactional(readOnly = true)
	public OrderResponse getOrderById(int orderId) {

		logger.info("Fetching order with id: {}", orderId);

		Order order = orderRepository.findById(orderId).orElseThrow(() -> {
			logger.error("Order not found with id: {}", orderId);

			return new OrderServiceException("Order not found with id: " + orderId, HttpStatus.NOT_FOUND);
		});

		logger.info("Order fetched successfully. Order id: {}", orderId);

		return convertToOrderResponse(order);
	}

	@Override
	@Transactional(readOnly = true)
	public List<OrderSummaryResponse> getAllOrdersByUserId(int userId) {

		logger.info("Fetching all orders for user id: {}", userId);

		userRepository.findById(userId).orElseThrow(() -> {
			logger.error("User not found with id: {}", userId);

			return new OrderServiceException("User not found with id: " + userId, HttpStatus.NOT_FOUND);
		});

		List<Order> orders = orderRepository.findByUserUserId(userId);

		if (orders.isEmpty()) {

			logger.info("No orders found for user id: {}", userId);

			throw new OrderServiceException("No orders found for user id: " + userId, HttpStatus.NOT_FOUND);
		}

		List<OrderSummaryResponse> responseList = new ArrayList<>();

		for (Order order : orders) {

			OrderSummaryResponse response = new OrderSummaryResponse();

			response.setOrderId(order.getOrderId());
			response.setTotalAmount(order.getTotalAmount());
			response.setOrderStatus(order.getOrderStatus().name());
			response.setPaymentStatus(order.getPaymentStatus().name());
			response.setOrderDate(order.getOrderDate());

			responseList.add(response);
		}

		logger.info("Fetched {} orders successfully for user id: {}", orders.size(), userId);

		return responseList;
	}

	private OrderResponse convertToOrderResponse(Order order) {

		OrderResponse response = new OrderResponse();

		response.setOrderId(order.getOrderId());
		response.setTotalAmount(order.getTotalAmount());
		response.setOrderStatus(order.getOrderStatus().name());
		response.setPaymentStatus(order.getPaymentStatus().name());
		response.setOrderDate(order.getOrderDate());

		Users user = order.getUser();

		response.setUserId(user.getUserId());
		response.setUserName(user.getFullName());
		response.setUserEmail(user.getEmail());

		Address address = order.getAddress();

		response.setAddressId(address.getAddressId());
		response.setAddressLabel(address.getLabel());
		response.setStreet(address.getStreet());
		response.setApartment(address.getApartment());
		response.setLandmark(address.getLandmark());
		response.setCity(address.getCity());
		response.setPostalCode(address.getPostalCode());
		response.setDeliveryInstructions(address.getDeliveryInstructions());

		return response;
	}
}