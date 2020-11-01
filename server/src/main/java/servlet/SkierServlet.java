package servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.*;
import service.LiftRideService;
import service.SkierService;
import utils.RequestBodyParser;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "SkierServlet")
public class SkierServlet extends HttpServlet {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final SkierService skierService = SkierService.getSkierService();
    private static final LiftRideService liftRideService = new LiftRideService();
    private static final String CONTENT_TYPE = "application/json";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType(CONTENT_TYPE);
        if (!validatePath(req, resp)) return;

        String urlPath = req.getPathInfo();
        String[] urlParts = urlPath.split("/");
        if (urlParts.length == 2) {
            if ("liftrides".equals(urlParts[1])) {
                try {
                    String body = RequestBodyParser.parse(req);
                    LiftRide liftRide = mapper.readValue(body, LiftRide.class);
                    if (liftRide.getDayID() == null
                            || liftRide.getLiftID() == null
                            || liftRide.getResortID() == null
                            || liftRide.getSkierID() == null
                            || liftRide.getTime() == null) {
                        handleInvalidInput(resp, "");
                        return;
                    }
                    liftRideService.addNewLiftRide(liftRide);
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                } catch (JsonProcessingException | SQLException e) {
                    handleInvalidInput(resp, e.getMessage());
                }
                return;
            }
        }

        handleDataNotFound(resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(CONTENT_TYPE);
        if (!validatePath(req, resp)) return;

        String urlPath = req.getPathInfo();
        String[] urlParts = urlPath.split("/");

        if (urlParts.length == 3) {
            if ("vertical".equals(urlParts[2])) {
                String skierId = urlParts[1], resort = req.getParameter("resort");
                if (!skierId.isEmpty() && resort != null) {
                    try {
                        SkierResortVertical res = skierService.handleGetSkierResortVerticalTotal(resort,
                                Integer.parseInt(skierId));
                        handleSuccess(resp, res);
                    } catch (SQLException | NumberFormatException e) {
                        handleInvalidInput(resp, e.getMessage());
                    }
                    return;
                }
            }
        }

        if (urlParts.length == 6) {
            if ("days".equals(urlParts[2]) && "skiers".equals(urlParts[4])) {
                String resortId = urlParts[1], dayId = urlParts[3], skierId = urlParts[5];
                if (!resortId.isEmpty() && !dayId.isEmpty() && !skierId.isEmpty()) {
                    try {
                        SkierResortVertical res = skierService.handleGetSkierResortVerticalDay(resortId,
                                Integer.parseInt(skierId), Integer.parseInt(dayId));
                        handleSuccess(resp, res);
                    } catch (SQLException | NumberFormatException e) {
                        handleInvalidInput(resp, e.getMessage());
                    }
                    return;
                }
            }
        }

        handleInvalidInput(resp, "");
    }

    private boolean validatePath(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String urlPath = req.getPathInfo();

        if (urlPath == null || urlPath.isEmpty()) {
            handleDataNotFound(resp);
            return false;
        }

        return true;
    }

    private void handleDataNotFound(HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        resp.getWriter().write(mapper.writeValueAsString(new Message("Data not found.")));
    }

    private void handleInvalidInput(HttpServletResponse resp, String msg) throws IOException {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().write(mapper.writeValueAsString(new Message("Invalid input supplied. " + msg)));
    }

    private <T> void handleSuccess(HttpServletResponse resp, T res) throws IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(mapper.writeValueAsString(res));
    }
}
