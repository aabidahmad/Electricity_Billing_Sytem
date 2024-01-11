package com.ElectricityAutomationInitiative.service.Impl;

import com.ElectricityAutomationInitiative.entity.Notification;
import com.ElectricityAutomationInitiative.repository.NotificationRepository;
import com.ElectricityAutomationInitiative.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }
}
