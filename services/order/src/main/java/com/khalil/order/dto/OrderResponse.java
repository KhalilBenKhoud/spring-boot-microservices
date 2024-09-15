package com.khalil.order.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khalil.order.entities.PaymentMethod;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record OrderResponse(
        Integer id,
        String reference,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        String customerId
) {

}