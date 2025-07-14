package com.dotsline.payment_service.controller;

import com.dotsline.payment_service.DTO.PaymentRequest;
import com.dotsline.payment_service.DTO.PaymentResponse;
import com.dotsline.payment_service.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService stripePaymentService;

    @PostMapping("/pay")
    public PaymentResponse pay(@RequestBody PaymentRequest request) {
        return stripePaymentService.createPaymentIntent(request);
    }
}
