package edu.lysak.railwaytickets.service;


import edu.lysak.railwaytickets.dto.PageableResponse;
import edu.lysak.railwaytickets.dto.RouteDto;
import edu.lysak.railwaytickets.dto.SearchRouteRequestDto;
import edu.lysak.railwaytickets.dto.SearchRouteResponseDto;
import edu.lysak.railwaytickets.exceptions.BusinessLogicException;
import edu.lysak.railwaytickets.exceptions.InputValidationException;
import edu.lysak.railwaytickets.model.Route;
import edu.lysak.railwaytickets.model.Station;
import org.apache.commons.lang3.StringUtils;
import edu.lysak.railwaytickets.repository.RouteRepository;
import edu.lysak.railwaytickets.repository.StationRepository;
import edu.lysak.railwaytickets.repository.TicketRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Used for processing operations with routes
 *
 * @author Yuliia Lysak
 */
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

    /**
     * Gets PageableResponse object of routes
     *
     * @see PageableResponse
     *
     * @param pageNo - number of current page
     * @param pageSize - size of a page
     *
     * @return PageableResponse object with route list, number of current page and total pages
     */
    public PageableResponse<Route> getAllRoutesPaginated(int pageNo, int pageSize) {
        List<Route> routes = routeRepository.findAllPaginated(pageNo - 1, pageSize);
        int routeCount = routeRepository.countRouteRecords();
        int totalPages = (int) Math.ceil((double) routeCount / pageSize);
        return new PageableResponse<>(
                Math.max(Math.min(pageNo, totalPages), 1),
                totalPages,
                routes
        );
    }

    public Route addNewRoute(RouteDto routeDto) {
        Route route = validateAndTransferInputData(routeDto, null);
        return routeRepository.save(route);
    }

    public void deleteRoute(Long routeId) {
        routeRepository.deleteById(routeId);
    }

    public void updateRoute(Long routeId, RouteDto routeDto) {
        Route routeFromDb = routeRepository.findById(routeId)
                .orElseThrow(() -> new InputValidationException("exception.route.notExist"));

        Route updatedRoute = validateAndTransferInputData(routeDto, routeFromDb);

        routeRepository.update(updatedRoute);
    }

    /**
     * Gets list of available routes
     *
     * @param searchRouteRequestDto - an object with search information
     *
     * @return List of objects with information about available routes
     *
     * @see SearchRouteResponseDto
     * @see SearchRouteRequestDto
     */
    public List<SearchRouteResponseDto> getAvailableRoutes(SearchRouteRequestDto searchRouteRequestDto) {
        List<Route> availableRoutes = routeRepository.findAvailableRoutesByCities(
                searchRouteRequestDto.getDepartureStation().getCity(),
                searchRouteRequestDto.getArrivalStation().getCity(),
                searchRouteRequestDto.getDepartureDate().atStartOfDay(),
                searchRouteRequestDto.getDepartureDate().atStartOfDay().plusDays(1)
        );

        return availableRoutes.stream()
                .filter(route -> getAvailableSeats(route) > 0)
                .map(this::createSearchResponse)
                .collect(Collectors.toList());
    }

    /**
     * Gets available seats for specific route
     *
     * @param route - a route for checking available seats
     *
     * @return number of available seats or 0
     */
    public int getAvailableSeats(Route route) {
        int totalSeats = route.getTotalSeats();
        int purchasedSeats = ticketRepository.findPurchasedTickets(route.getId());
        int availableSeats = totalSeats - purchasedSeats;
        return Math.max(availableSeats, 0);
    }

    public Route findRouteById(Long routeId) {
        return routeRepository.findById(routeId)
                .orElseThrow(() -> new InputValidationException("exception.route.notExist"));
    }

    public SearchRouteResponseDto getRouteResponseDto(Long routeId) {
        Route route = findRouteById(routeId);
        return createSearchResponse(route);
    }

    /**
     * Gets SearchRouteResponseDto of specific routes
     *
     * @param route - a route to get information from it
     *
     * @return SearchRouteResponseDto with information about specific route
     *
     * @see SearchRouteResponseDto
     */
    private SearchRouteResponseDto createSearchResponse(Route route) {
        String departureStationName = String.format("%s (%s)",
                route.getDepartureStation().getCity(), route.getDepartureStation().getName());
        String arrivalStationName = String.format("%s (%s)",
                route.getArrivalStation().getCity(), route.getArrivalStation().getName());
        long seconds = Duration.between(route.getDepartureTime(), route.getArrivalTime()).toSeconds();
        String duration = String.format("%d:%02d", seconds / 3600, (seconds % 3600) / 60);

        SearchRouteResponseDto responseDto = new SearchRouteResponseDto();
        responseDto.setRouteId(route.getId());
        responseDto.setTrainName(route.getTrainName());
        responseDto.setDepartureStationName(departureStationName);
        responseDto.setArrivalStationName(arrivalStationName);
        responseDto.setDepartureDateTime(route.getDepartureTime());
        responseDto.setDuration(duration);
        responseDto.setArrivalDateTime(route.getArrivalTime());
        responseDto.setTotalSeats(route.getTotalSeats());
        responseDto.setAvailableSeats(getAvailableSeats(route));
        responseDto.setPricePerSeat(route.getPricePerSeat());
        return responseDto;
    }

    /**
     * Validates information and transfer it from specific RouteDto to Route
     *
     * @param routeDto - a routeDto to get information from it
     * @param route - a route to transfer information to it
     *
     * @return Route object with validated information
     * @throws InputValidationException if departure or arrival station not present
     * @throws BusinessLogicException if departure or arrival station not found
     * @throws BusinessLogicException if departure and arrival station are the same
     * @throws InputValidationException if departure or arrival time not present
     * @throws InputValidationException if arrival time if before departure time
     * @throws InputValidationException if train name not present
     * @throws InputValidationException if total seats not present or negative
     * @throws InputValidationException if total seats less than number of purchased tickets
     * @throws InputValidationException if price per seat not present or negative
     *
     * @see RouteDto
     */
    private Route validateAndTransferInputData(RouteDto routeDto, Route route) {
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
        if (route != null) {
            int purchasedSeats = ticketRepository.findPurchasedTickets(route.getId());
            if (totalSeats < purchasedSeats) {
                throw new InputValidationException("exception.seats.lessThanPurchased");
            }
        }

        Double pricePerSeat = routeDto.getPricePerSeat();
        if (pricePerSeat == null || pricePerSeat <= 0) {
            throw new InputValidationException("exception.price.negative");
        }

        return Route.builder(route)
                .departureStation(departureStation)
                .arrivalStation(arrivalStation)
                .departureTime(departureTime)
                .arrivalTime(arrivalTime)
                .trainName(trainName)
                .totalSeats(totalSeats)
                .pricePerSeat(pricePerSeat)
                .build();
    }
}
