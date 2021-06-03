package service;

import model.SessionAnalytic;
import repository.SessionAnalyticRepository;

/**
 * Used for processing operations with session analytic
 *
 * @author Yuliia Lysak
 */
public class SessionAnalyticService {
    private final SessionAnalyticRepository sessionAnalyticsRepository;

    public SessionAnalyticService(SessionAnalyticRepository sessionAnalyticsRepository) {
        this.sessionAnalyticsRepository = sessionAnalyticsRepository;
    }

    public void addNewRecord(SessionAnalytic sessionAnalytic) {
        sessionAnalyticsRepository.save(sessionAnalytic);
    }
}
