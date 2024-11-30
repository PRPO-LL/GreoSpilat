package si.luka2.prpo.sportapp.servleti;

//import si.luka2.prpo.sportapp.dtos.NakupovalniSeznamDto;
import si.luka2.prpo.sportapp.entitete.Uporabnik;
import si.luka2.prpo.sportapp.zrna.UporabnikiZrno;

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
    private UporabnikiZrno uporabnikiZrno; //uporabniško zrno se uporablja za interakcijo
    //z entitetetami Uporabnik


    private static final Logger log = Logger.getLogger(JPAServlet.class.getName());


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter writer = resp.getWriter();

        Long countUporabnikov = uporabnikiZrno.pridobiUporabnikeCount(null);

        List<Uporabnik> users = uporabnikiZrno.vrniUporabnike();


        // Display the user count and list each user's details
        writer.append("<br/>Uporabnikov je: " + countUporabnikov + "<br/><br/>");
        writer.append("<br/>Uporabniki:<br/><br/>");
        for (Uporabnik user : users) {
            writer.append(user.toString()).append("<br/><br/>");
        }

    }
}
