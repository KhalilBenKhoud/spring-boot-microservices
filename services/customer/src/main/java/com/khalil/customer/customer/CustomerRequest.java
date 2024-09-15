package com.khalil.customer.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CustomerRequest(
        String id,
        @NotNull(message = "Customer first name is required")
        String firstname ,
        @NotNull(message = "Customer last name is required")

        String lastname ,
        @NotNull(message = "Customer first name is required")
        @Email(message = "email is not valid")
        String email ,
        Address address
) {


}
