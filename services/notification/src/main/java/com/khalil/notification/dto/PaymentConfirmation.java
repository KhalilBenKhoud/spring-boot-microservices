package com.khalil.notification.dto;


import com.khalil.notification.entities.PaymentMethod;

import java.math.BigDecimal;

public record PaymentConfirmation
        (
           String orderReference,
           BigDecimal amount,
           PaymentMethod paymentMethod,
           String customerFirstName,

           String customerLastName,
           String customerEmail
                                  ) {
}
