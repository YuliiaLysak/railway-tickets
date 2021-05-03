package edu.lysak.railwaytickets.service;

import edu.lysak.railwaytickets.dto.SearchRouteRequestDto;
import edu.lysak.railwaytickets.dto.SearchRouteResponseDto;
import edu.lysak.railwaytickets.model.Route;
import edu.lysak.railwaytickets.model.Station;
import edu.lysak.railwaytickets.repository.RouteRepository;
import edu.lysak.railwaytickets.repository.StationRepository;
import edu.lysak.railwaytickets.repository.TicketRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RouteService {
    private final RouteRepository routeRepository;
    private final StationRepository stationRepository;
    private final TicketRepository ticketRepository;

    public RouteService(
            RouteRepository routeRepository,
            StationRepository stationRepository,
            TicketRepository ticketRepository
    ) {
        this.routeRepository = routeRepository;
        this.stationRepository = stationRepository;
        this.ticketRepository = ticketRepository;
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

    public List<SearchRouteResponseDto> getAvailableRoutes(SearchRouteRequestDto searchRouteRequestDto) {
        String departureCity = searchRouteRequestDto.getDepartureStation().getCity();
        String arrivalCity = searchRouteRequestDto.getArrivalStation().getCity();

        LocalDateTime departureDateStart = searchRouteRequestDto.getDepartureDate().atStartOfDay();
        LocalDateTime departureDateEnd = departureDateStart.plusDays(1);
        List<Route> availableRoutes = routeRepository.findAvailableRoutesByCities(
                departureCity,
                arrivalCity,
                departureDateStart,
                departureDateEnd
        );

        return availableRoutes.stream()
                .filter(route -> getAvailableSeats(route) > 0)
                .map(this::createSearchResponse)
                .collect(Collectors.toList());
    }

    private SearchRouteResponseDto createSearchResponse(Route route) {
        SearchRouteResponseDto responseDto = new SearchRouteResponseDto();

        responseDto.setRouteId(route.getId());

        responseDto.setTrainName(route.getTrainName());

        String departureStationName = String.format("%s (%s)",
                route.getDepartureStation().getCity(), route.getDepartureStation().getName());
        responseDto.setDepartureStationName(departureStationName);

        responseDto.setDepartureDateTime(route.getDepartureTime());

        long seconds = Duration.between(route.getDepartureTime(), route.getArrivalTime()).toSeconds();
        String duration = String.format("%d:%02d", seconds / 3600, (seconds % 3600) / 60);
        responseDto.setDuration(duration);

        String arrivalStationName = String.format("%s (%s)",
                route.getArrivalStation().getCity(), route.getArrivalStation().getName());
        responseDto.setArrivalStationName(arrivalStationName);

        responseDto.setArrivalDateTime(route.getArrivalTime());

        responseDto.setTotalSeats(route.getTotalSeats());

        responseDto.setAvailableSeats(getAvailableSeats(route));

        responseDto.setPricePerSeat(route.getPricePerSeat());
        return responseDto;
    }

    private Station getStationByCityAndName(Station station) {
        return stationRepository.findByCityAndName(station.getCity(), station.getName());
    }

    public int getAvailableSeats(Route route) {
        int totalSeats = route.getTotalSeats();
        int purchasedSeats = ticketRepository.findPurchasedTickets(route.getId());
        int availableSeats = totalSeats - purchasedSeats;
        return Math.max(availableSeats, 0);
    }

    public Route findRouteById(Long routeId) {
        return routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalStateException(String.format(
                "Route with id = %d doesn't exist", routeId)));
    }
}
