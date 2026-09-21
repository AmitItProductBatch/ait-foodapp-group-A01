package com.ait.app.service;

import java.util.List;

import com.ait.app.dto.OrderRequest;
import com.ait.app.dto.OrderResponse;
import com.ait.app.dto.OrderSummaryResponse;

public interface OrderService {

    OrderResponse createOrder(int userId, OrderRequest request);

    OrderResponse getOrderById(int orderId);

    List<OrderSummaryResponse> getAllOrdersByUserId(int userId);
}