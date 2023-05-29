package homework_9;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@WebFilter(urlPatterns = "/time2")
public class TimezoneValidateFilter2 extends HttpFilter {
    ArrayList<String> timeZones = new ArrayList<>(Arrays.asList(
            "UTC-12", "UTC-10", "UTC-9", "UTC-8", "UTC-7",
            "UTC-6", "UTC-5", "UTC-4", "UTC-3", "UTC-2",
            "UTC-1", "UTC 1", "UTC 2", "UTC 3", "UTC 4",
            "UTC 5", "UTC 6", "UTC 7", "UTC 8", "UTC 9",
            "UTC 10", "UTC 11", "UTC 12", "UTC 13", "UTC 14"));

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String timezone = req.getParameter("timezone");

        if (timezone == null || timeZones.contains(timezone)) {
            chain.doFilter(req, res);
        } else  {
            res.setStatus(400);
            res.setContentType("text/html; charset=utf-8");
            res.getWriter().write("Invalid timezone");
            res.getWriter().close();
        }
    }
}
