package com.ait.app.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ait.app.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

	List<OrderItem> findByOrderOrderId(int orderId);
	

}
