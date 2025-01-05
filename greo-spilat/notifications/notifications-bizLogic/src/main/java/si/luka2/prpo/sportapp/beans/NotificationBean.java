package si.luka2.prpo.sportapp.beans;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import si.luka2.prpo.sportapp.entities.Notification;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class NotificationBean {

    private static final Logger logger = Logger.getLogger(NotificationBean.class.getName());

    @PersistenceContext(unitName = "notifications-jpa")
    private EntityManager em;

    @PostConstruct
    private void init() {
        logger.info("Inicializacija zrna " + NotificationBean.class.getSimpleName());
        // Add resource initialization logic here if needed
    }

    @PreDestroy
    private void destroy() {
        logger.info("Deinicializacija zrna " + NotificationBean.class.getSimpleName());
        // Add resource cleanup logic here if needed
    }
    /**
     * Fetch all notifications from the database.
     */
    public List<Notification> getAllNotifications() {
        return em.createQuery("SELECT n FROM Notification n", Notification.class).getResultList();
    }

    /**
     * Fetch a single notification by ID.
     */
    public Notification getNotification(int id) {
        return em.find(Notification.class, id);
    }

    /**
     * Add a new notification to the database.
     */
    @Transactional
    public Notification addNotification(Notification notification) {
        notification.setCreatedAt(java.time.LocalDateTime.now());
        notification.setStatus("PENDING");
        em.persist(notification);
        logger.info("Notification added: " + notification.getMessage());
        return notification;
    }

    /**
     * Delete a notification by ID.
     */
    @Transactional
    public boolean deleteNotification(int id) {
        Notification notification = em.find(Notification.class, id);
        if (notification != null) {
            em.remove(notification);
            return true;
        }
        return false;
    }

    /**
     * Fetch all notifications for a specific user.
     */
    public List<Notification> getUserNotifications(int userId) {
        return em.createQuery("SELECT n FROM Notification n WHERE n.recipientId = :userId", Notification.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Transactional
    public int addBulkNotifications(List<Notification> notificationsToAdd) {
        if (notificationsToAdd == null || notificationsToAdd.isEmpty()) {
            logger.warning("No notifications to add in bulk.");
            return 0;
        }

        int count = 0;
        try {
            for (Notification notification : notificationsToAdd) {
                notification.setCreatedAt(java.time.LocalDateTime.now());
                notification.setStatus("PENDING");
                em.persist(notification);
                logger.info("Bulk Notification added: " + notification.getMessage());
                count++;
            }
            em.flush(); // Ensures all changes are pushed to the database
        } catch (Exception e) {
            logger.severe("Error adding bulk notifications: " + e.getMessage());
            throw e; // Rethrow the exception to rollback the transaction
        }
        return count;
    }

    public int retryFailedNotifications() {
        // Placeholder logic for retrying failed notifications
        System.out.println("Retrying failed notifications...");
        return 0;
    }
}


