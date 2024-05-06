package services;

import models.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationService {
    private List<Notification> notifications;

    public NotificationService() {
        this.notifications = new ArrayList<>();
    }

    public void addNotification(Notification notification) {
        notifications.add(notification);
    }

    public List<Notification> getNotifications() {
        return notifications;
    }
}
