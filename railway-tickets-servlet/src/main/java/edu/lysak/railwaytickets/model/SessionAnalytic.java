package edu.lysak.railwaytickets.model;

import java.time.LocalDateTime;

public class SessionAnalytic {
    private Long analyticId;
    private String sessionId;
    private Long userId;
    private LocalDateTime creationTime;
    private LocalDateTime lastAccessedTime;
    private int searchRouteRequestCount;
    private int buyTicketRequestCount;

    public void incrementSearchRouteRequestCount() {
        searchRouteRequestCount++;
    }

    public void incrementBuyTicketRequestCount() {
        buyTicketRequestCount++;
    }

    public Long getAnalyticId() {
        return analyticId;
    }

    public void setAnalyticId(Long analyticId) {
        this.analyticId = analyticId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public LocalDateTime getLastAccessedTime() {
        return lastAccessedTime;
    }

    public void setLastAccessedTime(LocalDateTime lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    public int getSearchRouteRequestCount() {
        return searchRouteRequestCount;
    }

    public void setSearchRouteRequestCount(int searchRouteRequestCount) {
        this.searchRouteRequestCount = searchRouteRequestCount;
    }

    public int getBuyTicketRequestCount() {
        return buyTicketRequestCount;
    }

    public void setBuyTicketRequestCount(int buyTicketRequestCount) {
        this.buyTicketRequestCount = buyTicketRequestCount;
    }
}
