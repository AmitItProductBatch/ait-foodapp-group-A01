package com.ait.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ait.app.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
	
	Optional<Payment> findByOrderOrderId(int orderId);

	@Query("""
			SELECT p
			FROM Payment p
			WHERE p.order.orderId = :orderId
			""")
	Optional<Payment> findPaymentByOrderId(@Param("orderId") int orderId);


}
