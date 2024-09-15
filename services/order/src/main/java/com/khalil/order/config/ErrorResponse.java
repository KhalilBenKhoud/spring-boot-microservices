package com.khalil.order.config;

import java.util.Map;

public record ErrorResponse(
        Map<String,String> errors
) {
}
