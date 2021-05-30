package service;

import model.SessionAnalytic;
import repository.SessionAnalyticRepository;

public class SessionAnalyticService {
    private final SessionAnalyticRepository sessionAnalyticsRepository;

    public SessionAnalyticService(SessionAnalyticRepository sessionAnalyticsRepository) {
        this.sessionAnalyticsRepository = sessionAnalyticsRepository;
    }

    public void addNewRecord(SessionAnalytic sessionAnalytic) {
        sessionAnalyticsRepository.save(sessionAnalytic);
    }
}
