package homework_9;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@WebServlet("/time")
public class ThymeleafController extends HttpServlet {
    private TemplateEngine engine;

    @Override
    public void init() throws ServletException {
        engine = new TemplateEngine();

        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=utf-8");

        Context context = new Context(req.getLocale(), Map.of("times", currentTime(req, resp)));

        engine.process("time", context, resp.getWriter());

        resp.getWriter().close();
    }
    private String currentTime(HttpServletRequest req, HttpServletResponse resp)  {
        String timeZone;
        if (req.getParameterMap().containsKey("timezone")) {
            timeZone = req.getParameter("timezone").replace(" ", "+");
            resp.addCookie(new Cookie("lastTimezone", timeZone));
        } else {
            timeZone = isThereACookie(req);
        }
        return ZonedDateTime.now(ZoneId.of(timeZone))
                .format(DateTimeFormatter.ofPattern("yyyy-dd-MM HH:mm:ss")) + " " + timeZone;
    }

    private String isThereACookie(HttpServletRequest req) {
        String timeZone = "UTC";
        Cookie[] cookies = req.getCookies();
        if (cookies == null) {
            return timeZone;
        } else {
            for (Cookie cookie : cookies) {
                 if (cookie.getName().equals("lastTimezone")) {
                     timeZone = cookie.getValue();
                     break;
                 }
            }
            return timeZone;
        }
    }
}
