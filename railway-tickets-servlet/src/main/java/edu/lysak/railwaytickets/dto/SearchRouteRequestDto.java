package edu.lysak.railwaytickets.dto;

import edu.lysak.railwaytickets.model.Station;

import java.time.LocalDate;

public class SearchRouteRequestDto {
    private Station departureStation;
    private Station arrivalStation;
    private LocalDate departureDate;

    public Station getDepartureStation() {
        return departureStation;
    }

    public void setDepartureStation(Station departureStation) {
        this.departureStation = departureStation;
    }

    public Station getArrivalStation() {
        return arrivalStation;
    }

    public void setArrivalStation(Station arrivalStation) {
        this.arrivalStation = arrivalStation;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }
}
