package edu.lysak.railwaytickets.listener;

import edu.lysak.railwaytickets.filter.AnalyticFilter;
import edu.lysak.railwaytickets.model.SessionAnalytic;
import edu.lysak.railwaytickets.model.SessionUser;
import edu.lysak.railwaytickets.service.SessionAnalyticService;
import edu.lysak.railwaytickets.utils.ServiceLocator;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Session listener for analytical purposes: e.g suggesting user to buy ticket after search result,
 * getting conversion rate buying ticket
 *
 * @author Yuliia Lysak
 *
 * @see AnalyticFilter
 * @see SessionAnalytic
 */
public class AnalyticSessionListener implements HttpSessionListener {

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();

        SessionAnalytic sessionAnalytic = (SessionAnalytic) session.getAttribute("sessionAnalytic");
        SessionUser sessionUser = (SessionUser) session.getAttribute("sessionUser");
        if (sessionUser != null) {
            sessionAnalytic.setUserId(sessionUser.getId());
        }
        SessionAnalyticService sessionAnalyticService = ServiceLocator.getSessionAnalyticService();
        sessionAnalyticService.addNewRecord(sessionAnalytic);
    }
}
