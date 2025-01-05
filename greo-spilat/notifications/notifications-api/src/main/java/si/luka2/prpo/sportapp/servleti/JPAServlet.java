package si.luka2.prpo.sportapp.servleti;

import si.luka2.prpo.sportapp.beans.NotificationBean;
import si.luka2.prpo.sportapp.entities.Notification;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;

@WebServlet("/notifications-servlet")
public class JPAServlet extends HttpServlet {

    @Inject
    private NotificationBean notificationBean; // Bean for managing notifications

    private static final Logger log = Logger.getLogger(JPAServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter writer = resp.getWriter();

        log.info("📩 Fetching notifications...");

        // Example: Fetch notifications (you might need to add methods to NotificationBean)
        List<Notification> notifications = notificationBean.getAllNotifications();

        writer.append("<br/>Notifications:<br/><br/>");
        for (Notification notification : notifications) {
            writer.append(notification.toString()).append("<br/><br/>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String message = req.getParameter("message");
        String recipientType = req.getParameter("recipientType");

        if (message == null || recipientType == null) {
            resp.getWriter().append("❌ Missing required parameters: `message` and `recipientType`");
            return;
        }

        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setRecipientType(recipientType);

        notificationBean.addNotification(notification);

        log.info("✅ Notification added: " + message);

        resp.getWriter().append("✅ Notification successfully added: " + message);
    }
}
