package com.ElectricityAutomationInitiative.repository;

import com.ElectricityAutomationInitiative.entity.Notification;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NotificationRepository extends CrudRepository<Notification, Long> {
    // You can define custom query methods if needed
     List<Notification>findByArea(String area);
}

