package si.luka2.prpo.sportapp.beans;

import si.luka2.prpo.sportapp.entities.Notification;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class NotificationBean {

    // Temporary storage for notifications (replace with persistence later)
    private final List<Notification> notifications = new ArrayList<>();

    public List<Notification> getAllNotifications() {
        return notifications;
    }

    public Notification getNotification(int id) {
        return notifications.stream()
                .filter(notification -> notification.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Notification addNotification(Notification notification) {
        notification.setId(notifications.size() + 1); // Mock ID assignment
        notifications.add(notification);
        return notification;
    }

    public int addBulkNotifications(List<Notification> notificationsToAdd) {
        for (Notification notification : notificationsToAdd) {
            addNotification(notification);
        }
        return notificationsToAdd.size();
    }

    public boolean deleteNotification(int id) {
        return notifications.removeIf(notification -> notification.getId() == id);
    }

    public int retryFailedNotifications() {
        // Placeholder logic for retrying failed notifications
        System.out.println("Retrying failed notifications...");
        return 0;
    }

    public List<Notification> getUserNotifications(int userId) {
        List<Notification> userNotifications = new ArrayList<>();
        for (Notification notification : notifications) {
            if (notification.getRecipientId().equals(userId)) {
                userNotifications.add(notification);
            }
        }
        return userNotifications;
    }
}
