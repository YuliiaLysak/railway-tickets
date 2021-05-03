package edu.lysak.railwaytickets.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SearchRouteResponseDto {
    private Long routeId;
    private String trainName;
    private String departureStationName;
    private LocalDateTime departureDateTime;
    private String duration;
    private String arrivalStationName;
    private LocalDateTime arrivalDateTime;
    private Integer totalSeats;
    private Integer availableSeats;
    private Double pricePerSeat;
}