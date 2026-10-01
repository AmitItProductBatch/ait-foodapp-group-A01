package com.ait.app.service;

import com.ait.app.dto.GetPaymentDto;
import com.ait.app.dto.PaymentDto;
import com.ait.app.enums.PaymentStatus;

public interface PaymentService {
	
	GetPaymentDto makePayment(int orderId, PaymentDto dto);

	GetPaymentDto getPaymentById(int paymentId);

	GetPaymentDto getPaymentByOrder(int orderId);

	GetPaymentDto updatePaymentStatus(int paymentId, PaymentStatus paymentStatus);

	GetPaymentDto refundPayment(int paymentId);

}
