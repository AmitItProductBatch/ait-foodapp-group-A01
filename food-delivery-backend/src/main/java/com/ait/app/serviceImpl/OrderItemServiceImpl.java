package com.ait.app.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ait.app.dto.OrderItemResponse;
import com.ait.app.exception.OrderServiceException;
import com.ait.app.model.MenuItem;
import com.ait.app.model.Order;
import com.ait.app.model.OrderItem;
import com.ait.app.repository.MenuItemRepository;
import com.ait.app.repository.OrderItemRepository;
import com.ait.app.repository.OrderRepository;
import com.ait.app.service.OrderItemService;

import jakarta.transaction.Transactional;

@Service
public class OrderItemServiceImpl implements OrderItemService {

	private static final Logger logger = LoggerFactory.getLogger(OrderItemServiceImpl.class);

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private MenuItemRepository menuItemRepository;

	@Override
	@Transactional
	public OrderItemResponse addOrderItem(int orderId, long menuItemId, int quantity) {

		logger.info("Adding order item. orderId: {}, menuItemId: {}, quantity: {}", orderId, menuItemId, quantity);

		Order order = orderRepository.findById(orderId).orElseThrow(() -> {

			logger.error("Order not found with id: {}", orderId);

			return new OrderServiceException("Order not found with id: " + orderId, HttpStatus.NOT_FOUND);
		});

		MenuItem menuItem = menuItemRepository.findById(menuItemId).orElseThrow(() -> {

			logger.error("Menu item not found with id: {}", menuItemId);

			return new OrderServiceException("Menu item not found with id: " + menuItemId, HttpStatus.NOT_FOUND);
		});

		if (quantity <= 0) {

			logger.warn("Invalid quantity: {}. Setting quantity to 1", quantity);

			quantity = 1;
		}

		double unitPrice = menuItem.getFullPrice();

		double subtotal = unitPrice * quantity;

		logger.info("Calculated unitPrice: {}, subtotal: {}", unitPrice, subtotal);

		OrderItem orderItem = new OrderItem(order, menuItem, quantity, unitPrice, subtotal);

		OrderItem savedItem = orderItemRepository.save(orderItem);

		logger.info("Order item saved successfully. orderItemId: {}", savedItem.getOrderItemId());

		// Update order total
		updateOrderTotal(order);

		return convertToResponse(savedItem);
	}

	@Override
	public OrderItemResponse getOrderItemById(int orderItemId) {

		logger.info("Fetching order item with id: {}", orderItemId);

		OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(() -> {
			logger.error("Order item not found with id: {}", orderItemId);
			return new OrderServiceException("Order item not found with id: " + orderItemId, HttpStatus.NOT_FOUND);
		});

		logger.info("Order item found successfully. orderItemId: {}", orderItemId);

		return convertToResponse(orderItem);
	}

	@Override
	public List<OrderItemResponse> getAllOrderItemsByOrderId(int orderId) {

		logger.info("Fetching all order items for orderId: {}", orderId);

		if (!orderRepository.existsById(orderId)) {

			logger.error("Order not found with id: {}", orderId);

			throw new OrderServiceException("Order not found with id: " + orderId, HttpStatus.NOT_FOUND);
		}

		List<OrderItem> items = orderItemRepository.findByOrderOrderId(orderId);

		List<OrderItemResponse> responses = new ArrayList<>();

		for (OrderItem item : items) {
			responses.add(convertToResponse(item));
		}

		logger.info("Total order items found for orderId {}: {}", orderId, responses.size());

		return responses;
	}

	private OrderItemResponse convertToResponse(OrderItem item) {

		OrderItemResponse response = new OrderItemResponse();

		response.setOrderItemId(item.getOrderItemId());
		response.setOrderId(item.getOrder().getOrderId());
		response.setMenuItemId(item.getMenuItem().getId());
		response.setItemName(item.getMenuItem().getName());
		response.setQuantity(item.getQuantity());
		response.setUnitPrice(item.getUnitPrice());
		response.setSubtotal(item.getSubtotal());

		return response;
	}

	@Override
	@Transactional
	public OrderItemResponse updateOrderItemQuantity(int orderId, int orderItemId, int quantity) {

		logger.info("Updating order item quantity. orderId: {}, orderItemId: {}, quantity: {}", orderId, orderItemId,
				quantity);

		Order order = orderRepository.findById(orderId).orElseThrow(() -> {
			logger.error("Order not found with id: {}", orderId);

			return new OrderServiceException("Order not found with id: " + orderId, HttpStatus.NOT_FOUND);
		});

		OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(() -> {
			logger.error("Order item not found with id: {}", orderItemId);

			return new OrderServiceException("Order Item not found with id: " + orderItemId, HttpStatus.NOT_FOUND);
		});

		if (orderItem.getOrder().getOrderId() != orderId) {

			logger.error("Order item {} does not belong to order {}", orderItemId, orderId);

			throw new OrderServiceException("Order Item does not belong to Order", HttpStatus.BAD_REQUEST);
		}

		if (quantity <= 0) {

			logger.error("Invalid quantity {} for orderItemId: {}", quantity, orderItemId);

			throw new OrderServiceException("Quantity must be greater than 0", HttpStatus.BAD_REQUEST);
		}

		orderItem.setQuantity(quantity);

		double subtotal = orderItem.getUnitPrice() * quantity;

		orderItem.setSubtotal(subtotal);

		logger.info("Order item subtotal updated. orderItemId: {}, subtotal: {}", orderItemId, subtotal);

		OrderItem updatedItem = orderItemRepository.save(orderItem);

		logger.info("Order item updated successfully. orderItemId: {}", orderItemId);

		updateOrderTotal(order);

		return convertToResponse(updatedItem);
	}

	@Override
	public void deleteOrderItem(int orderItemId) {

		logger.info("Deleting order item with id: {}", orderItemId);

		OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(() -> {
			logger.error("Order item not found with id: {}", orderItemId);
			return new OrderServiceException("Order Item not found with id : " + orderItemId, HttpStatus.NOT_FOUND);
		});

		Order order = orderItem.getOrder();

		orderItemRepository.delete(orderItem);

		logger.info("Order item deleted successfully. orderItemId: {}", orderItemId);

		updateOrderTotal(order);
	}

	@Override
	public void deleteAllOrderItems(int orderId) {

		logger.info("Deleting all order items for orderId: {}", orderId);

		Order order = orderRepository.findById(orderId).orElseThrow(() -> {
			logger.error("Order not found with id: {}", orderId);
			return new OrderServiceException("Order not found with id : " + orderId, HttpStatus.NOT_FOUND);
		});

		List<OrderItem> items = orderItemRepository.findByOrderOrderId(orderId);

		logger.info("Total order items found for deletion: {}", items.size());

		orderItemRepository.deleteAll(items);

		logger.info("All order items deleted successfully for orderId: {}", orderId);

		updateOrderTotal(order);
	}

	private void updateOrderTotal(Order order) {

		logger.info("Updating total amount for orderId: {}", order.getOrderId());

		List<OrderItem> items = orderItemRepository.findByOrderOrderId(order.getOrderId());

		double total = 0.0;

		for (OrderItem item : items) {
			total += item.getSubtotal();
		}

		order.setTotalAmount(total);

		orderRepository.save(order);

		logger.info("Order total updated successfully. orderId: {}, totalAmount: {}", order.getOrderId(), total);
	}

}
