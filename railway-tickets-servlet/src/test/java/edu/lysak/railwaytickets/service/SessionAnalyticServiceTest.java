package edu.lysak.railwaytickets.service;

import edu.lysak.railwaytickets.model.SessionAnalytic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import edu.lysak.railwaytickets.repository.SessionAnalyticRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SessionAnalyticServiceTest {
    @Mock
    private SessionAnalyticRepository sessionAnalyticsRepository;

    @InjectMocks
    private SessionAnalyticService sessionAnalyticService;

    @Test
    @DisplayName("#addNewRecord(SessionAnalytic) should successfully add new record of session analytics")
    void addNewRecord_ShouldSuccessfullyAddNewSessionAnalytic() {
        SessionAnalytic sessionAnalytic = new SessionAnalytic();
        doNothing().when(sessionAnalyticsRepository).save(any());

        sessionAnalyticService.addNewRecord(sessionAnalytic);

        verify(sessionAnalyticsRepository).save(sessionAnalytic);
    }
}