package com.dotsline.payment_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class PaymentResponse {
    private boolean success;
    private String message;
    private String paymentIntentId;
    private String clientSecret;
}
