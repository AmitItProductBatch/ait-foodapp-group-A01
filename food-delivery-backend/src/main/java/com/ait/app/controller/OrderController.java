package com.ait.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ait.app.dto.OrderRequest;
import com.ait.app.dto.OrderResponse;
import com.ait.app.dto.OrderSummaryResponse;
import com.ait.app.enums.OrderStatus;
import com.ait.app.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@PostMapping("/{userId}")
	public ResponseEntity<OrderResponse> createOrder(@PathVariable int userId, @RequestBody OrderRequest request) {

		OrderResponse response = orderService.createOrder(userId, request);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<OrderResponse> getOrderById(@PathVariable int orderId) {

		OrderResponse response = orderService.getOrderById(orderId);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<OrderSummaryResponse>> getAllOrdersByUserId(@PathVariable int userId) {

		List<OrderSummaryResponse> response = orderService.getAllOrdersByUserId(userId);

		return ResponseEntity.ok(response);
	}
	@PatchMapping("/{orderId}/status/{status}")
	public ResponseEntity updateOrderStatus(
	        @PathVariable int orderId,
	        @PathVariable OrderStatus status) {

	    OrderResponse response =
	            orderService.updateOrderStatus(orderId, status);

	    return new ResponseEntity("Order Status Updated",HttpStatus.OK);
	}
	 
	 @GetMapping("/status/{status}")
	    public ResponseEntity<List<OrderSummaryResponse>> getOrdersByStatus(
	            @PathVariable OrderStatus status) {

	        List<OrderSummaryResponse> response =
	                orderService.getOrdersByStatus(status);

	        return ResponseEntity.ok(response);
	    }
	 
	 @PatchMapping("/{orderId}/cancel")
	    public ResponseEntity cancelOrder(
	            @PathVariable int orderId) {

	        OrderResponse response =
	                orderService.cancelOrder(orderId);

	        return new ResponseEntity("Order Cancled",HttpStatus.OK);
	    }
	  @DeleteMapping("/{orderId}")
	    public ResponseEntity deleteOrder(
	            @PathVariable int orderId) {

	        orderService.deleteOrder(orderId);

	        return new ResponseEntity("Order  deleted",HttpStatus.OK);
	    }
}