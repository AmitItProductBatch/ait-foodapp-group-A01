package com.ait.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ait.app.dto.OrderItemResponse;
import com.ait.app.service.OrderItemService;

@RestController
@RequestMapping("/api/orders")
public class OrderItemController {

	@Autowired
	private OrderItemService orderItemService;

	@PostMapping("/{orderId}/items/{menuItemId}")
	public ResponseEntity<OrderItemResponse> addOrderItem(
			@PathVariable int orderId,
			@PathVariable long menuItemId,
			@RequestParam(defaultValue = "1") int quantity) {

		OrderItemResponse response = orderItemService.addOrderItem(orderId, menuItemId, quantity);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/{orderItemId}/items")
	public ResponseEntity<OrderItemResponse> getOrderItemById(@PathVariable int orderItemId) {

		OrderItemResponse response = orderItemService.getOrderItemById(orderItemId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{orderId}/getAll")
	public ResponseEntity<List<OrderItemResponse>> getAllOrderItemsByOrderId(@PathVariable int orderId) {

		List<OrderItemResponse> response = orderItemService.getAllOrderItemsByOrderId(orderId);
		return ResponseEntity.ok(response);
	}
}
