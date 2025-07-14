package com.dotsline.payment_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    private Integer userId;
    private Integer scheduleId;
    private String travelDate; // "YYYY-MM-DD"
    private BigDecimal amount;
    private String paymentMethodId; // Stripe PaymentMethod ID from frontend
    private String currency = "lkr";
}
