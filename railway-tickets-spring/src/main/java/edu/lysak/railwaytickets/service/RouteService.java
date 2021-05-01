package edu.lysak.railwaytickets.service;

import edu.lysak.railwaytickets.dto.SearchRouteDto;
import edu.lysak.railwaytickets.model.Route;
import edu.lysak.railwaytickets.model.Station;
import edu.lysak.railwaytickets.repository.RouteRepository;
import edu.lysak.railwaytickets.repository.StationRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RouteService {
    private final RouteRepository routeRepository;
    private final StationRepository stationRepository;

    public RouteService(RouteRepository routeRepository, StationRepository stationRepository) {
        this.routeRepository = routeRepository;
        this.stationRepository = stationRepository;
    }

    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    public void addNewRoute(Route route) {
        Station departureStationFromDb = getStationByCityAndName(route.getDepartureStation());
        Station arrivalStationFromDb = getStationByCityAndName(route.getArrivalStation());

        route.setDepartureStation(departureStationFromDb);
        route.setArrivalStation(arrivalStationFromDb);
        routeRepository.save(route);
    }

    public void deleteRoute(Long routeId) {
        routeRepository.deleteById(routeId);
    }

    @Transactional
    public void updateRoute(Long routeId, Route route) {
        Route updatedRoute = routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalStateException(String.format(
                        "Route with id = %d doesn't exist", routeId)));

        Station departureStationFromDb = getStationByCityAndName(route.getDepartureStation());
        Station arrivalStationFromDb = getStationByCityAndName(route.getArrivalStation());

        if (departureStationFromDb != null) {
            updatedRoute.setDepartureStation(departureStationFromDb);
        }

        if (arrivalStationFromDb != null) {
            updatedRoute.setArrivalStation(arrivalStationFromDb);
        }

        LocalDateTime departureTime = route.getDepartureTime();
        LocalDateTime arrivalTime = route.getArrivalTime();
        if (departureTime != null && arrivalTime != null && arrivalTime.isAfter(departureTime)) {
            updatedRoute.setDepartureTime(departureTime);
            updatedRoute.setArrivalTime(arrivalTime);
        }

        String trainName = route.getTrainName();
        if (trainName != null && !trainName.isEmpty()) {
            updatedRoute.setTrainName(trainName);
        }

        Integer totalSeats = route.getTotalSeats();
        if (totalSeats != null && totalSeats > 0) {
            updatedRoute.setTotalSeats(totalSeats);
        }

        Double pricePerSeat = route.getPricePerSeat();
        if (pricePerSeat != null && pricePerSeat > 0) {
            updatedRoute.setPricePerSeat(pricePerSeat);
        }

        routeRepository.save(updatedRoute);
    }

    // TODO - add check for available seats and not null stations
    public List<Route> getAvailableRoutes(SearchRouteDto searchRouteDto) {
        Station departureStationFromDb = getStationByCityAndName(searchRouteDto.getDepartureStation());
        Station arrivalStationFromDb = getStationByCityAndName(searchRouteDto.getArrivalStation());

        return routeRepository.findAvailableRoutes(
                departureStationFromDb,
                arrivalStationFromDb,
                searchRouteDto.getDepartureDate().atStartOfDay()
        );
    }

    private Station getStationByCityAndName(Station station) {
        return stationRepository.findByCityAndName(station.getCity(), station.getName());
    }
}
