package com.khalil.notification.kafka;


import com.khalil.notification.dao.NotificationRepository;
import com.khalil.notification.dto.OrderConfirmation;
import com.khalil.notification.dto.PaymentConfirmation;
import com.khalil.notification.entities.Notification;
import com.khalil.notification.entities.NotificationType;
import com.khalil.notification.services.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {
    private final NotificationRepository repository ;
    private final EmailService emailService ;

    @KafkaListener(topics = "payment-topic")
    public void consumePaymentSuccessNotification(PaymentConfirmation paymentConfirmation) throws MessagingException {
        log.info(String.format("Consuming the message from payment topic")) ;
        repository.save(Notification.builder()
                .type(NotificationType.PAYMENT_CONFIRMATION)
                .paymentConfirmation(paymentConfirmation)
                .build() ) ;

        //send email
        var customerName = paymentConfirmation.customerFirstName() + " " + paymentConfirmation.customerLastName() ;
        emailService.sendPaymentSuccessEmail(
                paymentConfirmation.customerEmail(),
                customerName,
                paymentConfirmation.amount(),
                paymentConfirmation.orderReference()
        );

    }

    @KafkaListener(topics = "order-topic")
    public void consumeOrderConfirmationsNotification(OrderConfirmation orderConfirmation) throws MessagingException {
        log.info(String.format("Consuming the message from order topic")) ;
        repository.save(Notification.builder()
                .type(NotificationType.ORDER_CONFIRMATION)
                .orderConfirmation(orderConfirmation)
                .build() ) ;

        //send email
        var customerName = orderConfirmation.customer().firstname() + " " + orderConfirmation.customer().lastname() ;
        emailService.sendOrderConfirmationEmail(
                orderConfirmation.customer().email(),
                customerName,
                orderConfirmation.totalAmount(),
                orderConfirmation.orderReference(),
                orderConfirmation.products()
        );
    }
}
