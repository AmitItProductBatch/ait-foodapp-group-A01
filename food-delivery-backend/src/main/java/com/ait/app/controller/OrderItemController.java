package com.ait.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ait.app.dto.OrderItemRequest;
import com.ait.app.dto.OrderItemResponse;
import com.ait.app.service.OrderItemService;

@RestController
@RequestMapping("/api/orderItem")
public class OrderItemController {

	@Autowired
	private OrderItemService orderItemService;

	@PostMapping("/{orderId}/items/{menuItemId}")
	public ResponseEntity<OrderItemResponse> addOrderItem(@PathVariable int orderId, @PathVariable long menuItemId,
			@RequestBody OrderItemRequest request) {

		OrderItemResponse response = orderItemService.addOrderItem(orderId, menuItemId, request.getQuantity());

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/{orderItemId}/item")
	public ResponseEntity<OrderItemResponse> getOrderItemById(@PathVariable int orderItemId) {

		OrderItemResponse response = orderItemService.getOrderItemById(orderItemId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{orderId}/getAll")
	public ResponseEntity<List<OrderItemResponse>> getAllOrderItemsByOrderId(@PathVariable int orderId) {

		List<OrderItemResponse> response = orderItemService.getAllOrderItemsByOrderId(orderId);
		return ResponseEntity.ok(response);
	}

	@PatchMapping("/order/{orderId}/item/{orderItemId}")
	public ResponseEntity updateOrderItemQuantity(@PathVariable int orderId,
			@PathVariable int orderItemId, @RequestBody OrderItemRequest request) {

		OrderItemResponse response = orderItemService.updateOrderItemQuantity(orderId, orderItemId,
				request.getQuantity());

		return new ResponseEntity("quntity updated by :"+request.getQuantity(),HttpStatus.CREATED);
	}

	@DeleteMapping("/{orderItemId}")
	public ResponseEntity<String> deleteOrderItem(@PathVariable int orderItemId) {
		orderItemService.deleteOrderItem(orderItemId);
		return new ResponseEntity<>("Order Item deleted successfully", HttpStatus.OK);
	}

	@DeleteMapping("/order/{orderId}")
	public ResponseEntity<String> deleteAllOrderItems(@PathVariable int orderId) {
		orderItemService.deleteAllOrderItems(orderId);
		return new ResponseEntity<>("All order items deleted successfully", HttpStatus.OK);
	}

}
