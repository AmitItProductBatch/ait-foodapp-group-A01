package com.ait.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ait.app.enums.OrderStatus;
import com.ait.app.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByUserUserId(int userId);
    

    List<Order> findByOrderStatus(OrderStatus orderStatus);
    

}