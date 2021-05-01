package edu.lysak.railwaytickets.service;

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
        Station departureStation = route.getDepartureStation();
        Station departureStationFromDb = stationRepository.findByCityAndName(
                departureStation.getCity(), departureStation.getName()
        );

        Station arrivalStation = route.getArrivalStation();
        Station arrivalStationFromDb = stationRepository.findByCityAndName(
                arrivalStation.getCity(), arrivalStation.getName()
        );

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

        Station departureStation = route.getDepartureStation();
        Station departureStationFromDb = stationRepository.findByCityAndName(
                departureStation.getCity(), departureStation.getName()
        );
        if (departureStationFromDb != null) {
            updatedRoute.setDepartureStation(departureStationFromDb);
        }

        Station arrivalStation = route.getArrivalStation();
        Station arrivalStationFromDb = stationRepository.findByCityAndName(
                arrivalStation.getCity(), arrivalStation.getName()
        );
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
}
