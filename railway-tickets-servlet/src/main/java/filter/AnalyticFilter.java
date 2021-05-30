package filter;

import model.SessionAnalytic;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

public class AnalyticFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain chain
    ) throws ServletException, IOException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpSession session = request.getSession();
        SessionAnalytic sessionAnalytic = (SessionAnalytic) session.getAttribute("sessionAnalytic");
        if (sessionAnalytic == null) {
            sessionAnalytic = getSessionAnalytic(session);
            session.setAttribute("sessionAnalytic", sessionAnalytic);
        }

        String url = request.getHttpServletMapping().getPattern();
        if ("/api/routes/search".equals(url)) {
            sessionAnalytic.incrementSearchRouteRequestCount();
        } else if ("/api/tickets/*".equals(url)) {
            sessionAnalytic.incrementBuyTicketRequestCount();
        }

        chain.doFilter(request, response);
    }

    private SessionAnalytic getSessionAnalytic(HttpSession session) {
        SessionAnalytic sessionAnalytic = new SessionAnalytic();

        LocalDateTime creationTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(session.getCreationTime()),
                TimeZone.getDefault().toZoneId()
        );
        LocalDateTime lastAccessedTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(session.getLastAccessedTime()),
                TimeZone.getDefault().toZoneId()
        );
        sessionAnalytic.setSessionId(session.getId());
        sessionAnalytic.setCreationTime(creationTime);
        sessionAnalytic.setLastAccessedTime(lastAccessedTime);
        return sessionAnalytic;
    }
}
