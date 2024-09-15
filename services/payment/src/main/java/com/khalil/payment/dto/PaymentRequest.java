package com.khalil.payment.dto;

import com.khalil.payment.entities.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        Integer id,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,

        String orderReference,
        Customer customer
) {
}
