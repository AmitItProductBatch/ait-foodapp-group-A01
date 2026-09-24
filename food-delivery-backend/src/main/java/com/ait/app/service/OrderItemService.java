package com.ait.app.service;

import java.util.List;
import com.ait.app.dto.OrderItemResponse;

public interface OrderItemService {

	OrderItemResponse addOrderItem(int orderId, long menuItemId, int quantity);

	OrderItemResponse getOrderItemById(int orderItemId);

	List<OrderItemResponse> getAllOrderItemsByOrderId(int orderId);

}
