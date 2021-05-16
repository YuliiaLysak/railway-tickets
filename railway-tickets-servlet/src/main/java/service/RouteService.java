package service;


import dto.RouteDto;
import dto.SearchRouteRequestDto;
import dto.SearchRouteResponseDto;
import exceptions.BusinessLogicException;
import exceptions.InputValidationException;
import model.Route;
import model.Station;
import org.apache.commons.lang3.StringUtils;
import repository.RouteRepository;
import repository.StationRepository;
import repository.TicketRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public Route addNewRoute(RouteDto routeDto) {
        Route route = new Route();
        validateAndTransferInputData(routeDto, route);
        return routeRepository.save(route);
    }

    public void deleteRoute(Long routeId) {
        try {
            routeRepository.deleteById(routeId);
//            TODO - check exception for constraint in database
        } catch (Exception e) {
            throw new BusinessLogicException(
                    "exception.route.delete"
            );
        }
    }

    public void updateRoute(Long routeId, RouteDto routeDto) {
        Route updatedRoute = routeRepository.findById(routeId)
                .orElseThrow(() -> new InputValidationException(String.format(
//                        TODO - add this line with id to message
                        "Route with id = %d doesn't exist", routeId)));

        validateAndTransferInputData(routeDto, updatedRoute);

        routeRepository.update(updatedRoute);
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

    private void validateAndTransferInputData(RouteDto routeDto, Route route) {
        if (routeDto.getDepartureStationId() == null
                || routeDto.getArrivalStationId() == null) {
            throw new InputValidationException("exception.station.empty");
        }

        Station departureStation = stationRepository.findById(routeDto.getDepartureStationId())
                .orElseThrow(() -> new BusinessLogicException("exception.depStation.null"));
        Station arrivalStation = stationRepository.findById(routeDto.getArrivalStationId())
                .orElseThrow(() -> new BusinessLogicException("exception.arrStation.null"));
        if (Objects.equals(departureStation, arrivalStation)) {
            throw new BusinessLogicException("exception.station.same");
        }

        LocalDateTime departureTime = routeDto.getDepartureTime();
        LocalDateTime arrivalTime = routeDto.getArrivalTime();
        if (departureTime == null || arrivalTime == null) {
            throw new InputValidationException("exception.time.empty");
        }

        if (arrivalTime.isBefore(departureTime) || arrivalTime.isEqual(departureTime)) {
            throw new InputValidationException("exception.time.before");
        }

        String trainName = routeDto.getTrainName();
        if (StringUtils.isEmpty(trainName)) {
            throw new InputValidationException("exception.train.empty");
        }

        Integer totalSeats = routeDto.getTotalSeats();
        if (totalSeats == null || totalSeats <= 0) {
            throw new InputValidationException("exception.seats.negative");
        }

        Double pricePerSeat = routeDto.getPricePerSeat();
        if (pricePerSeat == null || pricePerSeat <= 0) {
            throw new InputValidationException("exception.price.negative");
        }

        route.setDepartureStation(departureStation);
        route.setArrivalStation(arrivalStation);
        route.setDepartureTime(departureTime);
        route.setArrivalTime(arrivalTime);
        route.setTrainName(trainName);
        route.setTotalSeats(totalSeats);
        route.setPricePerSeat(pricePerSeat);
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

    public int getAvailableSeats(Route route) {
        int totalSeats = route.getTotalSeats();
        int purchasedSeats = ticketRepository.findPurchasedTickets(route.getId());
        int availableSeats = totalSeats - purchasedSeats;
        return Math.max(availableSeats, 0);
    }

    public Route findRouteById(Long routeId) {
        return routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalStateException(String.format(
//                        TODO - add this line with id to message
                        "Route with id = %d doesn't exist", routeId)));
    }

    public SearchRouteResponseDto getRouteResponseDto(Long routeId) {
        Route route = findRouteById(routeId);
        return createSearchResponse(route);
    }
}