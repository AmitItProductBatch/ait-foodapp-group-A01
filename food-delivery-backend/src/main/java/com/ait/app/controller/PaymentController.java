package com.ait.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ait.app.dto.PaymentDto;
import com.ait.app.enums.PaymentStatus;
import com.ait.app.service.PaymentService;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
	
	@Autowired
	PaymentService paymentService;
	
	@PostMapping("orderId/{orderId}")
	public ResponseEntity makePayment(@PathVariable int orderId,@RequestBody PaymentDto dto)
	{
		return new ResponseEntity(paymentService.makePayment(orderId, dto),HttpStatus.CREATED);
	}
	
	@GetMapping("paymentId/{paymentId}")
	public ResponseEntity getPaymentById(@PathVariable int paymentId)
	{
		return new ResponseEntity(paymentService.getPaymentById(paymentId),HttpStatus.OK);
	}
	
	@PatchMapping("paymentId/{paymentId}/paymentStatus")
	public ResponseEntity updatePaymentStatus(@PathVariable int paymentId,@RequestParam PaymentStatus paymentStatus)
	{
		return new ResponseEntity(paymentService.updatePaymentStatus(paymentId, paymentStatus),HttpStatus.OK);
	}
	
	@PutMapping("paymentId/{paymentId}/refund")
	public ResponseEntity refundPayment(@PathVariable int paymentId)
	{
		return new ResponseEntity(paymentService.refundPayment(paymentId),HttpStatus.OK);
	}	
	

}
