package com.ait.app.serviceImpl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ait.app.dto.GetPaymentDto;
import com.ait.app.dto.PaymentDto;
import com.ait.app.enums.PaymentStatus;
import com.ait.app.exception.OrderServiceException;
import com.ait.app.exception.PaymentServiceException;
import com.ait.app.model.Order;
import com.ait.app.model.Payment;
import com.ait.app.repository.OrderRepository;
import com.ait.app.repository.PaymentRepository;
import com.ait.app.service.PaymentService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	PaymentRepository paymentRepository;

	@Autowired
	OrderRepository orderRepository;

	@PersistenceContext
	private EntityManager entityManager;

	private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

	@Override
	public GetPaymentDto makePayment(int orderId, PaymentDto dto) {

		logger.info("Making payment for orderId: {}", orderId);

		Optional<Order> optional = orderRepository.findById(orderId);

		if (optional.isEmpty()) {
			logger.error("Order not found for orderId: {}", orderId);

			throw new OrderServiceException("Order is not found for orderId :" + orderId, HttpStatus.NOT_FOUND);
		}

		Order order = optional.get();

		Payment payment = new Payment();

		payment.setAmount(order.getTotalAmount());
		payment.setTransactionId("TXN" + System.currentTimeMillis());
		payment.setPaymentMethod(dto.getPaymentMethod());
		payment.setPaymentStatus(PaymentStatus.PENDING);
		payment.setPaymentDate(LocalDateTime.now());
		payment.setOrder(order);

		Payment saved = paymentRepository.save(payment);

		logger.info("Payment created successfully. paymentId: {}, orderId: {}", saved.getPaymentId(), orderId);

		GetPaymentDto dto2 = new GetPaymentDto();

		dto2.setPaymentId(saved.getPaymentId());
		dto2.setAmount(saved.getAmount());
		dto2.setTransactionId(saved.getTransactionId());
		dto2.setPaymentMethod(saved.getPaymentMethod());
		dto2.setPaymentStatus(saved.getPaymentStatus());
		dto2.setPaymentDate(saved.getPaymentDate());
		dto2.setOrderId(saved.getOrder().getOrderId());

		return dto2;
	}

	@Override
	public GetPaymentDto getPaymentById(int paymentId) {

		logger.info("Fetching payment for paymentId: {}", paymentId);

		Optional<Payment> optional = paymentRepository.findById(paymentId);

		if (optional.isEmpty()) {

			logger.error("Payment not found for paymentId: {}", paymentId);

			throw new PaymentServiceException("Payment is not found for Payment id :" + paymentId,
					HttpStatus.NOT_FOUND);
		}

		Payment payment = optional.get();

		logger.info("Payment fetched successfully. paymentId: {}", paymentId);

		GetPaymentDto dto = new GetPaymentDto();

		dto.setPaymentId(payment.getPaymentId());
		dto.setAmount(payment.getAmount());
		dto.setTransactionId(payment.getTransactionId());
		dto.setPaymentMethod(payment.getPaymentMethod());
		dto.setPaymentStatus(payment.getPaymentStatus());
		dto.setPaymentDate(payment.getPaymentDate());
		dto.setOrderId(payment.getOrder().getOrderId());

		return dto;
	}

	@Override
	public GetPaymentDto getPaymentByOrder(int orderId) {

		logger.info("Fetching payment for orderId: {}", orderId);

		Optional<Payment> optional = paymentRepository.findByOrderOrderId(orderId);

		if (optional.isEmpty()) {

			logger.error("Payment not found for orderId: {}", orderId);

			throw new PaymentServiceException("Payment is not found for this order id : " + orderId,
					HttpStatus.NOT_FOUND);
		}

		Payment payment = optional.get();

		logger.info("Payment fetched successfully for orderId: {}", orderId);

		GetPaymentDto dto = new GetPaymentDto();

		dto.setPaymentId(payment.getPaymentId());
		dto.setAmount(payment.getAmount());
		dto.setTransactionId(payment.getTransactionId());
		dto.setPaymentMethod(payment.getPaymentMethod());
		dto.setPaymentStatus(payment.getPaymentStatus());
		dto.setPaymentDate(payment.getPaymentDate());
		dto.setOrderId(payment.getOrder().getOrderId());

		return dto;
	}

	@Override
	@Transactional
	public GetPaymentDto updatePaymentStatus(int paymentId, PaymentStatus paymentStatus) {

		logger.info("Updating payment status. paymentId: {}, status: {}", paymentId, paymentStatus);

		Payment payment = entityManager.find(Payment.class, paymentId);

		if (payment == null) {

			logger.error("Payment not found for paymentId: {}", paymentId);

			throw new PaymentServiceException("Payment is not found for Payment id : " + paymentId,
					HttpStatus.NOT_FOUND);
		}

		String sql = """
				UPDATE payments
				SET payment_status = :paymentStatus
				WHERE payment_id = :paymentId
				""";

		Query query = entityManager.createNativeQuery(sql);

		query.setParameter("paymentStatus", paymentStatus.name());

		query.setParameter("paymentId", paymentId);

		query.executeUpdate();

		entityManager.clear();

		Payment updatedPayment = entityManager.find(Payment.class, paymentId);

		if (updatedPayment == null) {

			logger.error("Failed to retrieve updated payment. paymentId: {}", paymentId);

			throw new PaymentServiceException("Failed to retrieve updated payment for Payment id : " + paymentId,
					HttpStatus.NOT_FOUND);
		}

		logger.info("Payment status updated successfully. paymentId: {}, status: {}", paymentId, paymentStatus);

		GetPaymentDto dto = new GetPaymentDto();

		dto.setPaymentId(updatedPayment.getPaymentId());
		dto.setAmount(updatedPayment.getAmount());
		dto.setTransactionId(updatedPayment.getTransactionId());
		dto.setPaymentMethod(updatedPayment.getPaymentMethod());
		dto.setPaymentStatus(updatedPayment.getPaymentStatus());
		dto.setPaymentDate(updatedPayment.getPaymentDate());
		dto.setOrderId(updatedPayment.getOrder().getOrderId());

		return dto;
	}

	@Override
	public GetPaymentDto refundPayment(int paymentId) {

		logger.info("Refund request received for paymentId: {}", paymentId);

		Optional<Payment> optional = paymentRepository.findById(paymentId);

		if (optional.isEmpty()) {

			logger.error("Payment not found for refund. paymentId: {}", paymentId);

			throw new PaymentServiceException("Payment is not found for Payment id : " + paymentId,
					HttpStatus.NOT_FOUND);
		}

		Payment payment = optional.get();

		if (payment.getPaymentStatus() != PaymentStatus.PAID) {

			logger.error("Payment cannot be refunded. paymentId: {}, currentStatus: {}", paymentId,
					payment.getPaymentStatus());

			throw new PaymentServiceException(
					"Payment cannot be refunded because current status is : " + payment.getPaymentStatus(),
					HttpStatus.BAD_REQUEST);
		}

		payment.setPaymentStatus(PaymentStatus.REFUNDED);

		Payment savePayment = paymentRepository.save(payment);

		logger.info("Payment refunded successfully. paymentId: {}", paymentId);

		GetPaymentDto dto = new GetPaymentDto();

		dto.setPaymentId(savePayment.getPaymentId());
		dto.setAmount(savePayment.getAmount());
		dto.setTransactionId(savePayment.getTransactionId());
		dto.setPaymentMethod(savePayment.getPaymentMethod());
		dto.setPaymentStatus(savePayment.getPaymentStatus());
		dto.setPaymentDate(savePayment.getPaymentDate());
		dto.setOrderId(savePayment.getOrder().getOrderId());

		return dto;
	}

}
