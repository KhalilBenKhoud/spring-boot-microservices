package com.khalil.order.services;

import com.khalil.order.config.PaymentClient;
import com.khalil.order.dao.OrderRepository;
import com.khalil.order.dto.*;
import com.khalil.order.entities.Order;
import com.khalil.order.kafka.OrderProducer;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerClient customerClient ;
    private final ProductCLient productCLient ;
    private final OrderRepository orderRepository ;
    private final OrderMapper mapper ;
    private final OrderLineService orderLineService ;
    private final OrderProducer orderProducer ;
    private final PaymentClient paymentClient ;
    public Integer createOrder(OrderRequest request) {
       var customer = customerClient.findCustomerById(request.customerId()).orElseThrow(() -> new RuntimeException(("no customr exists with the provided id"))) ;
      var purchasedProducts =  productCLient.purchaseProducts(request.products()) ;

       var order = orderRepository.save(mapper.toOrder(request)) ;
        for(PurchaseRequest purchaseRequest : request.products()) {
           orderLineService.saveOrderLine(new OrderLineRequest(
                   null,
                   order.getId(),
                   purchaseRequest.productId(),
                   purchaseRequest.quantity()
           )) ;
        }
        var paymentRequest = new PaymentRequest(
                request.amount(),
                request.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer
        ) ;
        paymentClient.requestOrderPayment(paymentRequest) ;
        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        request.reference(),
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchasedProducts
                )
        );
        return order.getId() ;
    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream()
                .map(mapper::fromOrder)
                .collect(Collectors.toList()) ;
    }

    public OrderResponse findById(Integer orderId) {
        return orderRepository.findById(orderId)
                .map(mapper::fromOrder)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No order found with id :",orderId))) ;
    }
}
