package skiers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Message;
import entities.NewLiftRide;
import entities.SkierResortVerticalDay;
import entities.SkierResortVerticalTotal;
import utils.RequestBodyParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SkierServlet")
public class SkierServlet extends HttpServlet {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final SkierLiftRideHandler skierLiftRideHandler = new SkierLiftRideHandler();
    private static final SkierResortVerticalHandler skierResortVerticalHandler =
            new SkierResortVerticalHandler();
    private static final String CONTENT_TYPE = "application/json";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("POST!");
        resp.setContentType(CONTENT_TYPE);
        if (!validatePath(req, resp)) return;

        String urlPath = req.getPathInfo();
        String[] urlParts = urlPath.split("/");
        if (urlParts.length == 2) {
            if ("liftrides".equals(urlParts[1])) {
                String body = RequestBodyParser.parse(req);
                NewLiftRide newLiftRide = null;
                try {
                    newLiftRide = mapper.readValue(body, NewLiftRide.class);
                } catch (JsonProcessingException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write(mapper.writeValueAsString(new Message("Invalid input supplied")));
                    return;
                }

                if (newLiftRide.getDayID() == null
                        || newLiftRide.getLiftID() == null
                        || newLiftRide.getResortID() == null
                        || newLiftRide.getSkierID() == null
                        || newLiftRide.getTime() == null) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write(mapper.writeValueAsString(new Message("Invalid input supplied")));
                }

                boolean res = skierLiftRideHandler.addNewLiftRide(newLiftRide);
                if (res) {
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write(mapper.writeValueAsString(new Message("Data not " +
                            "found")));
                }
                return;
            }
        }

        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().write(mapper.writeValueAsString(new Message("Invalid input supplied")));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(CONTENT_TYPE);
        if (!validatePath(req, resp)) return;

        String urlPath = req.getPathInfo();
        String[] urlParts = urlPath.split("/");

        if (urlParts.length == 3) {
            if ("vertical".equals(urlParts[2])) {
                String skierId = urlParts[1], resort = req.getParameter("resort");
                if (!skierId.isEmpty() && resort != null) {
                    SkierResortVerticalTotal res =
                            skierResortVerticalHandler.handleGetSkierResortVerticalTotal(resort,
                                    skierId);
                    if (res == null) {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        resp.getWriter().write(mapper.writeValueAsString(new Message("Data not " +
                                "found")));
                        return;
                    }
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getWriter().write(mapper.writeValueAsString(res));
                    return;
                }
            }
        }

        if (urlParts.length == 6) {
            if ("days".equals(urlParts[2]) && "skiers".equals(urlParts[4])) {
                String resortId = urlParts[1], dayId = urlParts[3], skierId = urlParts[5];
                if (!resortId.isEmpty() && !dayId.isEmpty() && !skierId.isEmpty()) {
                    SkierResortVerticalDay res =
                            skierResortVerticalHandler.handleGetSkierResortVerticalDay(resortId,
                                    skierId, dayId);
                    if (res == null) {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        resp.getWriter().write(mapper.writeValueAsString(new Message("Data not found")));
                        return;
                    }
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getWriter().write(mapper.writeValueAsString(res));
                    return;
                }
            }
        }

        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().write(mapper.writeValueAsString(new Message("Invalid input supplied")));
    }

    private boolean validatePath(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String urlPath = req.getPathInfo();

        if (urlPath == null || urlPath.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(mapper.writeValueAsString(new Message("Invalid input supplied")));
            return false;
        }

        return true;
    }
}
