package com.khalil.order.services;

import com.khalil.order.dto.OrderLineRequest;
import com.khalil.order.dto.OrderLineResponse;
import com.khalil.order.entities.Order;
import com.khalil.order.entities.OrderLine;
import org.springframework.stereotype.Service;

@Service
public class OrderLineMapper {
    public OrderLine toOrderLine(OrderLineRequest request) {
        return OrderLine.builder()
                .id(request.id())
                .quantity(request.quantity())
                .order(
                        Order.builder()
                                .id(request.orderId())
                                .build()
                )
                .productId(request.productId())
                .build() ;
    }

    public OrderLineResponse toOrderLineResponse(OrderLine orderLine) {
        return new OrderLineResponse(orderLine.getId(), orderLine.getQuantity()) ;
    }
}
