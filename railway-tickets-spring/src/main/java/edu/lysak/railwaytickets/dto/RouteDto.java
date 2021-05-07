package edu.lysak.railwaytickets.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RouteDto {
    private Long departureStationId;
    private Long arrivalStationId;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String trainName;
    private Integer totalSeats;
    private Double pricePerSeat;
}
