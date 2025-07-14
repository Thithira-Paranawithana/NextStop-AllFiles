package com.dotsline.payment_service.service;

import com.dotsline.payment_service.DTO.PaymentRequest;
import com.dotsline.payment_service.DTO.PaymentResponse;
import com.dotsline.payment_service.Entity.Payment;
import com.dotsline.payment_service.Repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Value("${stripe.api.secret}")
    private String stripeSecretKey;

    private final PaymentRepository paymentRepository;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    public PaymentResponse createPaymentIntent(PaymentRequest request) {
        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(request.getAmount().multiply(BigDecimal.valueOf(100)).longValue()) // cents (paisa for LKR)
                    .setCurrency("lkr")
                    .setPaymentMethod(request.getPaymentMethodId())
                    .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.MANUAL)
                    .setConfirm(true)
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);

            // Save payment record
            Payment payment = new Payment();
            payment.setUserId(request.getUserId());
            payment.setScheduleId(request.getScheduleId());
            payment.setTravelDate(Date.valueOf(request.getTravelDate()));
            payment.setAmount(request.getAmount());
            payment.setPaymentMethod(request.getPaymentMethodId());
            payment.setStatus(intent.getStatus());
            payment.setStripePaymentIntentId(intent.getId());
            payment.setCreatedAt(LocalDateTime.now());
            paymentRepository.save(payment);

            boolean success = "succeeded".equals(intent.getStatus());

            return new PaymentResponse(
                    success,
                    success ? "Payment successful" : "Payment requires further action",
                    intent.getId(),
                    intent.getClientSecret()
            );
        } catch (Exception e) {
            return new PaymentResponse(false, "Payment failed: " + e.getMessage(), null, null);
        }
    }
}


