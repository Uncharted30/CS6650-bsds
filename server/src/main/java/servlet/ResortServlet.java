package servlet;

import entity.Message;
import entity.SkierTotalVertical;
import com.fasterxml.jackson.databind.ObjectMapper;
import service.ResortService;
import service.interfaces.IResortService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name="ResortServlet")
public class ResortServlet extends HttpServlet {

    private static final IResortService resortService = ResortService.getResortService();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String CONTENT_TYPE = "application/json";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(CONTENT_TYPE);
        String urlPath = req.getPathInfo();

        if (urlPath == null || urlPath.isEmpty()) {
            handleDataNotFound(resp, "");
            return;
        }

        String[] urlParts = urlPath.split("/");

        if (urlParts.length == 3) {
            if ("day".equals(urlParts[1]) && "top10vert".equals(urlParts[2])) {
                String resort = req.getParameter("resort"), dayId = req.getParameter("dayID");
                if (resort != null && dayId != null) {
                    try {
                        List<SkierTotalVertical> res = resortService.getTop10SkierTotalDayVertical(resort,
                                    Integer.parseInt(dayId));
                        resp.setStatus(HttpServletResponse.SC_OK);
                        resp.getWriter().write("{'topTenSkiers':" + mapper.writeValueAsString(res) + "}");
                    } catch (SQLException e) {
                        handleInvalidInput(resp, e.getMessage());
                    }
                    return;
                }
            }
        }

        handleDataNotFound(resp, "");
    }

    private void handleDataNotFound(HttpServletResponse resp, String msg) throws IOException {
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        resp.getWriter().write(mapper.writeValueAsString(new Message("Data not found. " + msg)));
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
