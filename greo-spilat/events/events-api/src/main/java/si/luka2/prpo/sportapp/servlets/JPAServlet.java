package si.luka2.prpo.sportapp.servlets;

import si.luka2.prpo.sportapp.entitete.Event;
import si.luka2.prpo.sportapp.zrna.EventsZrno;

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

@WebServlet("/servlet")
public class JPAServlet extends HttpServlet {
    @Inject
    private EventsZrno eventsZrno;

    private static final Logger log = Logger.getLogger(JPAServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter writer = resp.getWriter();

        Long countEventov = eventsZrno.getEventsCount(null);

        List<Event> events = eventsZrno.getEvents();


        // Display the user count and list each user's details
        writer.append("<br/>Uporabnikov je: " + countEventov + "<br/><br/>");
        writer.append("<br/>Uporabniki:<br/><br/>");
        for (Event event : events) {
            writer.append(event.toString()).append("<br/><br/>");
        }

    }
}
