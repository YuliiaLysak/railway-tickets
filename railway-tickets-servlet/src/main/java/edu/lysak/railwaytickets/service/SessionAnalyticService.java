package edu.lysak.railwaytickets.service;

import edu.lysak.railwaytickets.model.SessionAnalytic;
import edu.lysak.railwaytickets.repository.SessionAnalyticRepository;

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
