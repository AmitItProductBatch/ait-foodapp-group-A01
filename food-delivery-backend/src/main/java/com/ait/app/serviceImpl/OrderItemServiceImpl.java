package com.ait.app.serviceImpl;

import java.util.ArrayList;
import java.util.List;

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

@Service
public class OrderItemServiceImpl implements OrderItemService {

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private MenuItemRepository menuItemRepository;

	@Override
	public OrderItemResponse addOrderItem(int orderId, long menuItemId, int quantity) {

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new OrderServiceException("Order not found with id: " + orderId, HttpStatus.NOT_FOUND));

		MenuItem menuItem = menuItemRepository.findById(menuItemId)
				.orElseThrow(() -> new OrderServiceException("Menu item not found with id: " + menuItemId, HttpStatus.NOT_FOUND));

		if (quantity <= 0) {
			quantity = 1;
		}

		double unitPrice = menuItem.getFullPrice();
		double subtotal = unitPrice * quantity;

		OrderItem orderItem = new OrderItem(order, menuItem, quantity, unitPrice, subtotal);
		OrderItem savedItem = orderItemRepository.save(orderItem);

		return convertToResponse(savedItem);
	}

	@Override
	public OrderItemResponse getOrderItemById(int orderItemId) {

		OrderItem orderItem = orderItemRepository.findById(orderItemId)
				.orElseThrow(() -> new OrderServiceException("Order item not found with id: " + orderItemId, HttpStatus.NOT_FOUND));

		return convertToResponse(orderItem);
	}

	@Override
	public List<OrderItemResponse> getAllOrderItemsByOrderId(int orderId) {

		if (!orderRepository.existsById(orderId)) {
			throw new OrderServiceException("Order not found with id: " + orderId, HttpStatus.NOT_FOUND);
		}

		List<OrderItem> items = orderItemRepository.findByOrderOrderId(orderId);
		List<OrderItemResponse> responses = new ArrayList<>();

		for (OrderItem item : items) {
			responses.add(convertToResponse(item));
		}

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
}
