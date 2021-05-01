package edu.lysak.railwaytickets.dto;

import edu.lysak.railwaytickets.model.Station;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SearchRouteDto {
    private Station departureStation;
    private Station arrivalStation;
    private LocalDate departureDate;
}
