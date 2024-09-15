package com.khalil.notification.dao;

import com.khalil.notification.dto.PaymentConfirmation;
import com.khalil.notification.entities.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification,String> {
}
