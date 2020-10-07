package resorts;

import entities.Message;
import entities.SkierVerticalTotal;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name="ResortServlet")
public class ResortServlet extends HttpServlet {

    private static final DayTop10VertHandler dayTop10VertHandler = new DayTop10VertHandler();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String CONTENT_TYPE = "application/json";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(CONTENT_TYPE);
        String urlPath = req.getPathInfo();

        if (urlPath == null || urlPath.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(mapper.writeValueAsString(new Message("Invalid input supplied")));
            return;
        }

        String[] urlParts = urlPath.split("/");

        if (urlParts.length == 3) {
            if ("day".equals(urlParts[1]) && "top10vert".equals(urlParts[2])) {
                String resort = req.getParameter("resort"), dayId = req.getParameter("dayID");
                if (resort != null && dayId != null) {
                    List<SkierVerticalTotal> res = dayTop10VertHandler.handleGetDayTop10Vert(resort, dayId);
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
}
