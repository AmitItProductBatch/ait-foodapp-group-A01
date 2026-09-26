package com.ait.app.service;

import java.util.List;

import com.ait.app.dto.OrderRequest;
import com.ait.app.dto.OrderResponse;
import com.ait.app.dto.OrderSummaryResponse;
import com.ait.app.enums.OrderStatus;


public interface OrderService {

    OrderResponse createOrder(int userId, OrderRequest request);

    OrderResponse getOrderById(int orderId);

    List<OrderSummaryResponse> getAllOrdersByUserId(int userId);
    
    OrderResponse updateOrderStatus(int orderId, OrderStatus status);

    List<OrderSummaryResponse> getOrdersByStatus(OrderStatus status);

    OrderResponse cancelOrder(int orderId);

    void deleteOrder(int orderId);
}