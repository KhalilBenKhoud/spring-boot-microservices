package com.khalil.product.config;

import java.util.Map;

public record ErrorResponse(
        Map<String,String> errors
) {
}
