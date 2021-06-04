package edu.lysak.railwaytickets.dto;

import java.time.LocalDateTime;

public class RouteDto {
    private Long departureStationId;
    private Long arrivalStationId;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String trainName;
    private Integer totalSeats;
    private Double pricePerSeat;

    public Long getDepartureStationId() {
        return departureStationId;
    }

    public void setDepartureStationId(Long departureStationId) {
        this.departureStationId = departureStationId;
    }

    public Long getArrivalStationId() {
        return arrivalStationId;
    }

    public void setArrivalStationId(Long arrivalStationId) {
        this.arrivalStationId = arrivalStationId;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }

    public Double getPricePerSeat() {
        return pricePerSeat;
    }

    public void setPricePerSeat(Double pricePerSeat) {
        this.pricePerSeat = pricePerSeat;
    }
}
