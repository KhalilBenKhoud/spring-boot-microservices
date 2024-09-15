package com.khalil.payment.services;

import com.khalil.payment.config.NotificationProducer;
import com.khalil.payment.dao.PaymentRepository;
import com.khalil.payment.dto.PaymentNotificationRequest;
import com.khalil.payment.dto.PaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository repository ;
    private final PaymentMapper mapper ;
    private final NotificationProducer notificationProducer ;
    public Integer createPayment(PaymentRequest request) {
        var payment = repository.save(mapper.toPayment(request)) ;
        notificationProducer.sendNotification(
                new PaymentNotificationRequest(
                        request.orderReference(),
                        request.amount(),
                        request.paymentMethod(),
                        request.customer().firstname(),
                        request.customer().lastname(),
                        request.customer().email()
                )
        );
        return payment.getId() ;
    }
}
