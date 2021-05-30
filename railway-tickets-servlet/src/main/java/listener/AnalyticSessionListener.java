package listener;

import model.SessionAnalytic;
import model.SessionUser;
import service.SessionAnalyticService;
import utils.ServiceLocator;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Listener for analytical purposes: e.g suggesting user to buy ticket after search result,
 * getting conversion rate buying ticket
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
