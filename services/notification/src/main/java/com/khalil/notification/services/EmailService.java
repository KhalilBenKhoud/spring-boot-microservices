package com.khalil.notification.services;

import com.khalil.notification.dto.PaymentConfirmation;
import com.khalil.notification.dto.Product;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {


    private final JavaMailSender mailSender ;
    private final SpringTemplateEngine templateEngine ;

    @Async
    public void sendPaymentSuccessEmail(
            String destinationEmail,
            String customerName,
            BigDecimal amount,
            String orderReference
    ) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage() ;
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,MimeMessageHelper.MULTIPART_MODE_RELATED, StandardCharsets.UTF_8.name()) ;
        messageHelper.setFrom("khalil.b.khoud1998@gmail.com");
        final String templateName = EmailTemplate.PAYMENT_CONFIRMATION.getTemplate();
        Map<String,Object> variables = new HashMap<>() ;
        variables.put("customerName",customerName) ;
        variables.put("amount",amount) ;
        variables.put("orderReference",orderReference) ;

        Context context = new Context() ;
        context.setVariables(variables);
        messageHelper.setSubject(EmailTemplate.PAYMENT_CONFIRMATION.getSubject());

        try {
            String htmlTemplate = templateEngine.process(templateName,context) ;
            messageHelper.setText(htmlTemplate,true);
            messageHelper.setTo(destinationEmail);
            mailSender.send(mimeMessage);
            log.info("Email successfully sent to {}",destinationEmail);
        }catch(MessagingException e) {
            log.warn("cannot send email to {}",destinationEmail);
        }

    }


    @Async
    public void sendOrderConfirmationEmail(
            String destinationEmail,
            String customerName,
            BigDecimal amount,
            String orderReference,
            List<Product> products
    ) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage() ;
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,MimeMessageHelper.MULTIPART_MODE_RELATED, StandardCharsets.UTF_8.name()) ;
        messageHelper.setFrom("khalil.b.khoud1998@gmail.com");
        final String templateName = EmailTemplate.ORDER_CONFIRMATION.getTemplate();
        Map<String,Object> variables = new HashMap<>() ;
        variables.put("customerName",customerName) ;
        variables.put("totalAmount",amount) ;
        variables.put("orderReference",orderReference) ;
        variables.put("products",products) ;

        Context context = new Context() ;
        context.setVariables(variables);
        messageHelper.setSubject(EmailTemplate.ORDER_CONFIRMATION.getSubject());

        try {
            String htmlTemplate = templateEngine.process(templateName,context) ;
            messageHelper.setText(htmlTemplate,true);
            messageHelper.setTo(destinationEmail);
            mailSender.send(mimeMessage);
            log.info("Email successfully sent to {}",destinationEmail);
        }catch(MessagingException e) {
            log.warn("cannot send email to {}",destinationEmail);
        }

    }

}
