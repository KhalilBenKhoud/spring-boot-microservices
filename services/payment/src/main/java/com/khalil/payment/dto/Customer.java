package com.khalil.payment.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record Customer(
       @NotNull
        String id,
       @NotNull
        String firstname,

        String lastname,
        @NotNull
        @Email
        String email

) {
}
