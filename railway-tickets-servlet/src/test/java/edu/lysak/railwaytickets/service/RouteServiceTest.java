package edu.lysak.railwaytickets.service;

import edu.lysak.railwaytickets.dto.PageableResponse;
import edu.lysak.railwaytickets.dto.RouteDto;
import edu.lysak.railwaytickets.dto.SearchRouteRequestDto;
import edu.lysak.railwaytickets.dto.SearchRouteResponseDto;
import edu.lysak.railwaytickets.exceptions.BusinessLogicException;
import edu.lysak.railwaytickets.exceptions.InputValidationException;
import edu.lysak.railwaytickets.model.Route;
import edu.lysak.railwaytickets.model.Station;
import edu.lysak.railwaytickets.repository.RouteRepository;
import edu.lysak.railwaytickets.repository.StationRepository;
import edu.lysak.railwaytickets.repository.TicketRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsArgAt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RouteServiceTest {

    @Mock
    private RouteRepository routeRepository;
    @Mock
    private StationRepository stationRepository;
    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private RouteService routeService;

    @Captor
    private ArgumentCaptor<Route> routeCaptor;

    @Test
    @DisplayName("#getAllRoutes() should return list of routes")
    public void getAllRoutes_ShouldReturnRouteList() {
        List<Route> expected = List.of(Route.builder().build());
        given(routeRepository.findAll()).willReturn(expected);

        List<Route> actual = routeService.getAllRoutes();

        verify(routeRepository).findAll();
        assertThat(actual).isSameAs(expected);
    }

    @Test
    @DisplayName("#getAllRoutesPaginated(int, int) should return all routes as pageable object")
    void getAllRoutesPaginated_ShouldReturnAllRoutesAsPageableObject() {
        List<Route> expected = List.of(Route.builder().build());
        given(routeRepository.findAllPaginated(anyInt(), anyInt())).willReturn(expected);
        int routeCount = 1;
        given(routeRepository.countRouteRecords()).willReturn(routeCount);

        int pageNo = 1;
        PageableResponse<Route> routesPaginated = routeService.getAllRoutesPaginated(pageNo, 10);

        verify(routeRepository).findAllPaginated(pageNo - 1, 10);
        verify(routeRepository).countRouteRecords();
        assertThat(routesPaginated.getContent()).isEqualTo(expected);
        assertThat(routesPaginated.getTotalPages()).isEqualTo(1);
        assertThat(routesPaginated.getCurrentPage()).isEqualTo(1);
    }

    @Test
    @DisplayName("#addNewRoute(RouteDto) should successfully add new route")
    public void addNewRoute_ShouldSuccessfullyAddNewRoute() {
        LocalDateTime departureTime = LocalDateTime.now();
        LocalDateTime arrivalTime = departureTime.plusHours(7);

        Station departureStation = createStation(1L, "Kyiv", "Kyiv-Pas");
        Station arrivalStation = createStation(2L, "Lviv", "Lviv-Pas");

        RouteDto routeDto = createRouteDtoWithStationsAndTime(
                1L,
                2L,
                departureTime,
                arrivalTime
        );
        routeDto.setTrainName("Hogwarts Express");
        routeDto.setTotalSeats(500);
        routeDto.setPricePerSeat(100.0);

        given(stationRepository.findById(any()))
                .willReturn(Optional.of(departureStation))
                .willReturn(Optional.of(arrivalStation));
        given(routeRepository.save(any())).will(returnsArgAt(0));

        routeService.addNewRoute(routeDto);

        verify(stationRepository).findById(1L);
        verify(stationRepository).findById(2L);

        verify(routeRepository).save(routeCaptor.capture());
        Route routeForSaving = routeCaptor.getValue();
        assertThat(routeForSaving.getDepartureStation()).isEqualTo(departureStation);
        assertThat(routeForSaving.getArrivalStation()).isEqualTo(arrivalStation);
        assertThat(routeForSaving.getDepartureTime()).isEqualTo(departureTime);
        assertThat(routeForSaving.getArrivalTime()).isEqualTo(arrivalTime);
        assertThat(routeForSaving.getTrainName()).isEqualTo("Hogwarts Express");
        assertThat(routeForSaving.getTotalSeats()).isEqualTo(500);
        assertThat(routeForSaving.getPricePerSeat()).isEqualTo(100.0);
    }

    // TODO - replace with @ParametrizedTest
    @Test
    @DisplayName("#addNewRoute(RouteDto) should throw InputValidationException if departureStation is empty")
    public void addNewRoute_ShouldThrowExceptionIfDepartureStationEmpty() {
        RouteDto routeDto = createRouteDtoWithStations(null, 2L);

        assertThatThrownBy(() -> routeService.addNewRoute(routeDto))
                .isInstanceOf(InputValidationException.class)
                .hasMessageContaining("exception.station.empty");

        verify(routeRepository, never()).save(any());
    }

    // TODO - replace with @ParametrizedTest
    @Test
    @DisplayName("#addNewRoute(RouteDto) should throw InputValidationException if arrivalStation is empty")
    public void addNewRoute_ShouldThrowExceptionIfArrivalStationEmpty() {
        RouteDto routeDto = createRouteDtoWithStations(1L, null);

        assertThatThrownBy(() -> routeService.addNewRoute(routeDto))
                .isInstanceOf(InputValidationException.class)
                .hasMessageContaining("exception.station.empty");

        verify(routeRepository, never()).save(any());
    }

    // TODO - replace with @ParametrizedTest
    @Test
    @DisplayName("#addNewRoute(RouteDto) should throw InputValidationException if departureStation and arrivalStation are empty")
    public void addNewRoute_ShouldThrowExceptionIfDepartureAndArrivalStationEmpty() {
        RouteDto routeDto = createRouteDtoWithStations(null, null);

        assertThatThrownBy(() -> routeService.addNewRoute(routeDto))
                .isInstanceOf(InputValidationException.class)
                .hasMessageContaining("exception.station.empty");

        verify(routeRepository, never()).save(any());
    }

    @Test
    @DisplayName("#addNewRoute(RouteDto) should throw BusinessLogicException if departureStation was not found in database")
    public void addNewRoute_ShouldThrowExceptionIfDepartureStationNotFoundInDatabase() {
        RouteDto routeDto = createRouteDtoWithStations(1L, 2L);
        given(stationRepository.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> routeService.addNewRoute(routeDto))
                .isInstanceOf(BusinessLogicException.class)
                .hasMessageContaining("exception.depStation.null");

        verify(stationRepository).findById(1L);
        verify(stationRepository, never()).findById(2L);
        verify(routeRepository, never()).save(any());
    }

    @Test
    @DisplayName("#addNewRoute(RouteDto) should throw BusinessLogicException if arrivalStation was not found in database")
    public void addNewRoute_ShouldThrowExceptionIfArrivalStationNotFoundInDatabase() {
        RouteDto routeDto = createRouteDtoWithStations(1L, 2L);
        given(stationRepository.findById(1L)).willReturn(Optional.of(new Station()));
        given(stationRepository.findById(2L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> routeService.addNewRoute(routeDto))
                .isInstanceOf(BusinessLogicException.class)
                .hasMessageContaining("exception.arrStation.null");

        verify(stationRepository).findById(1L);
        verify(stationRepository).findById(2L);
        verify(routeRepository, never()).save(any());
    }

    @Test
    @DisplayName("#addNewRoute(RouteDto) should throw BusinessLogicException if departureStation and arrivalStation are the same")
    public void addNewRoute_ShouldThrowExceptionIfDepartureStationEqualsArrivalStation() {
        RouteDto routeDto = createRouteDtoWithStations(1L, 1L);
        given(stationRepository.findById(1L)).willReturn(Optional.of(new Station()));

        assertThatThrownBy(() -> routeService.addNewRoute(routeDto))
                .isInstanceOf(BusinessLogicException.class)
                .hasMessageContaining("exception.station.same");

        verify(stationRepository, times(2)).findById(1L);
        verify(routeRepository, never()).save(any());
    }

    @Test
    @DisplayName("#addNewRoute(RouteDto) should throw InputValidationException if departureTime is empty")
    public void addNewRoute_ShouldThrowExceptionIfDepartureTimeEmpty() {
        Station departureStation = createStation(1L, "Kyiv", "Kyiv-Pas");
        Station arrivalStation = createStation(2L, "Lviv", "Lviv-Pas");
        RouteDto routeDto = createRouteDtoWithStationsAndTime(
                1L, 2L, null, LocalDateTime.now()
        );
        given(stationRepository.findById(1L)).willReturn(Optional.of(departureStation));
        given(stationRepository.findById(2L)).willReturn(Optional.of(arrivalStation));

        assertThatThrownBy(() -> routeService.addNewRoute(routeDto))
                .isInstanceOf(InputValidationException.class)
                .hasMessageContaining("exception.time.empty");

        verify(stationRepository).findById(1L);
        verify(stationRepository).findById(2L);
        verify(routeRepository, never()).save(any());
    }

    @Test
    @DisplayName("#addNewRoute(RouteDto) should throw InputValidationException if arrivalTime is empty")
    public void addNewRoute_ShouldThrowExceptionIfArrivalTimeEmpty() {
        Station departureStation = createStation(1L, "Kyiv", "Kyiv-Pas");
        Station arrivalStation = createStation(2L, "Lviv", "Lviv-Pas");
        RouteDto routeDto = createRouteDtoWithStationsAndTime(
                1L, 2L, LocalDateTime.now(), null
        );
        given(stationRepository.findById(1L)).willReturn(Optional.of(departureStation));
        given(stationRepository.findById(2L)).willReturn(Optional.of(arrivalStation));

        assertThatThrownBy(() -> routeService.addNewRoute(routeDto))
                .isInstanceOf(InputValidationException.class)
                .hasMessageContaining("exception.time.empty");

        verify(stationRepository).findById(1L);
        verify(stationRepository).findById(2L);
        verify(routeRepository, never()).save(any());
    }

    @Test
    @DisplayName("#addNewRoute(RouteDto) should throw InputValidationException if departureTime and arrivalTime are empty")
    public void addNewRoute_ShouldThrowExceptionIfDepartureAndArrivalTimeEmpty() {
        Station departureStation = createStation(1L, "Kyiv", "Kyiv-Pas");
        Station arrivalStation = createStation(2L, "Lviv", "Lviv-Pas");
        RouteDto routeDto = createRouteDtoWithStationsAndTime(
                1L, 2L, null, null
        );
        given(stationRepository.findById(1L)).willReturn(Optional.of(departureStation));
        given(stationRepository.findById(2L)).willReturn(Optional.of(arrivalStation));

        assertThatThrownBy(() -> routeService.addNewRoute(routeDto))
                .isInstanceOf(InputValidationException.class)
                .hasMessageContaining("exception.time.empty");

        verify(stationRepository).findById(1L);
        verify(stationRepository).findById(2L);
        verify(routeRepository, never()).save(any());
    }

    @Test
    @DisplayName("#addNewRoute(RouteDto) should throw InputValidationException if arrivalTime is before departureTime")
    public void addNewRoute_ShouldThrowExceptionIfArrivalTimeBeforeDepartureTime() {
        Station departureStation = createStation(1L, "Kyiv", "Kyiv-Pas");
        Station arrivalStation = createStation(2L, "Lviv", "Lviv-Pas");
        RouteDto routeDto = createRouteDtoWithStationsAndTime(
                1L, 2L, LocalDateTime.now(), LocalDateTime.now().minusHours(1)
        );
        given(stationRepository.findById(1L)).willReturn(Optional.of(departureStation));
        given(stationRepository.findById(2L)).willReturn(Optional.of(arrivalStation));

        assertThatThrownBy(() -> routeService.addNewRoute(routeDto))
                .isInstanceOf(InputValidationException.class)
                .hasMessageContaining("exception.time.before");

        verify(stationRepository).findById(1L);
        verify(stationRepository).findById(2L);
        verify(routeRepository, never()).save(any());
    }

    @Test
    @DisplayName("#addNewRoute(RouteDto) should throw InputValidationException if departureTime and arrivalTime are the same")
    public void addNewRoute_ShouldThrowExceptionIfDepartureAndArrivalTimeSame() {
        Station departureStation = createStation(1L, "Kyiv", "Kyiv-Pas");
        Station arrivalStation = createStation(2L, "Lviv", "Lviv-Pas");
        LocalDateTime time = LocalDateTime.now();
        RouteDto routeDto = createRouteDtoWithStationsAndTime(
                1L, 2L, time, time
        );
        given(stationRepository.findById(1L)).willReturn(Optional.of(departureStation));
        given(stationRepository.findById(2L)).willReturn(Optional.of(arrivalStation));

        assertThatThrownBy(() -> routeService.addNewRoute(routeDto))
                .isInstanceOf(InputValidationException.class)
                .hasMessageContaining("exception.time.before");

        verify(stationRepository).findById(1L);
        verify(stationRepository).findById(2L);
        verify(routeRepository, never()).save(any());
    }

    @Test
    @DisplayName("#addNewRoute(RouteDto) should throw InputValidationException if trainName is empty")
    public void addNewRoute_ShouldThrowExceptionIfTrainNameEmpty() {
        Station departureStation = createStation(1L, "Kyiv", "Kyiv-Pas");
        Station arrivalStation = createStation(2L, "Lviv", "Lviv-Pas");
        LocalDateTime time = LocalDateTime.now();
        RouteDto routeDto = createRouteDtoWithStationsAndTime(
                1L, 2L, time, time.plusHours(5)
        );
        routeDto.setTrainName(null);
        given(stationRepository.findById(1L)).willReturn(Optional.of(departureStation));
        given(stationRepository.findById(2L)).willReturn(Optional.of(arrivalStation));

        assertThatThrownBy(() -> routeService.addNewRoute(routeDto))
                .isInstanceOf(InputValidationException.class)
                .hasMessageContaining("exception.train.empty");

        verify(stationRepository).findById(1L);
        verify(stationRepository).findById(2L);
        verify(routeRepository, never()).save(any());
    }

    @Test
    @DisplayName("#addNewRoute(RouteDto) should throw InputValidationException if totalSeats is empty")
    public void addNewRoute_ShouldThrowExceptionIfTotalSeatsEmpty() {
        Station departureStation = createStation(1L, "Kyiv", "Kyiv-Pas");
        Station arrivalStation = createStation(2L, "Lviv", "Lviv-Pas");
        LocalDateTime time = LocalDateTime.now();
        RouteDto routeDto = createRouteDtoWithStationsAndTime(
                1L, 2L, time, time.plusHours(5)
        );
        routeDto.setTrainName("Hogwarts Express");
        routeDto.setTotalSeats(null);
        given(stationRepository.findById(1L)).willReturn(Optional.of(departureStation));
        given(stationRepository.findById(2L)).willReturn(Optional.of(arrivalStation));

        assertThatThrownBy(() -> routeService.addNewRoute(routeDto))
                .isInstanceOf(InputValidationException.class)
                .hasMessageContaining("exception.seats.negative");

        verify(stationRepository).findById(1L);
        verify(stationRepository).findById(2L);
        verify(routeRepository, never()).save(any());
    }

    @Test
    @DisplayName("#addNewRoute(RouteDto) should throw InputValidationException if totalSeats is less than 0")
    public void addNewRoute_ShouldThrowExceptionIfTotalSeatsLessThanZero() {
        Station departureStation = createStation(1L, "Kyiv", "Kyiv-Pas");
        Station arrivalStation = createStation(2L, "Lviv", "Lviv-Pas");
        LocalDateTime time = LocalDateTime.now();
        RouteDto routeDto = createRouteDtoWithStationsAndTime(
                1L, 2L, time, time.plusHours(5)
        );
        routeDto.setTrainName("Hogwarts Express");
        routeDto.setTotalSeats(-200);
        given(stationRepository.findById(1L)).willReturn(Optional.of(departureStation));
        given(stationRepository.findById(2L)).willReturn(Optional.of(arrivalStation));

        assertThatThrownBy(() -> routeService.addNewRoute(routeDto))
                .isInstanceOf(InputValidationException.class)
                .hasMessageContaining("exception.seats.negative");

        verify(stationRepository).findById(1L);
        verify(stationRepository).findById(2L);
        verify(routeRepository, never()).save(any());
    }

    @Test
    @DisplayName("#addNewRoute(RouteDto) should throw InputValidationException if totalSeats is 0")
    public void addNewRoute_ShouldThrowExceptionIfTotalSeatsZero() {
        Station departureStation = createStation(1L, "Kyiv", "Kyiv-Pas");
        Station arrivalStation = createStation(2L, "Lviv", "Lviv-Pas");
        LocalDateTime time = LocalDateTime.now();
        RouteDto routeDto = createRouteDtoWithStationsAndTime(
                1L, 2L, time, time.plusHours(5)
        );
        routeDto.setTrainName("Hogwarts Express");
        routeDto.setTotalSeats(0);
        given(stationRepository.findById(1L)).willReturn(Optional.of(departureStation));
        given(stationRepository.findById(2L)).willReturn(Optional.of(arrivalStation));

        assertThatThrownBy(() -> routeService.addNewRoute(routeDto))
                .isInstanceOf(InputValidationException.class)
                .hasMessageContaining("exception.seats.negative");

        verify(stationRepository).findById(1L);
        verify(stationRepository).findById(2L);
        verify(routeRepository, never()).save(any());
    }

    @Test
    @DisplayName("#addNewRoute(RouteDto) should throw InputValidationException if pricePerSeat is empty")
    public void addNewRoute_ShouldThrowExceptionIfPricePerSeatEmpty() {
        Station departureStation = createStation(1L, "Kyiv", "Kyiv-Pas");
        Station arrivalStation = createStation(2L, "Lviv", "Lviv-Pas");
        LocalDateTime time = LocalDateTime.now();
        RouteDto routeDto = createRouteDtoWithStationsAndTime(
                1L, 2L, time, time.plusHours(5)
        );
        routeDto.setTrainName("Hogwarts Express");
        routeDto.setTotalSeats(200);
        routeDto.setPricePerSeat(null);
        given(stationRepository.findById(1L)).willReturn(Optional.of(departureStation));
        given(stationRepository.findById(2L)).willReturn(Optional.of(arrivalStation));

        assertThatThrownBy(() -> routeService.addNewRoute(routeDto))
                .isInstanceOf(InputValidationException.class)
                .hasMessageContaining("exception.price.negative");

        verify(stationRepository).findById(1L);
        verify(stationRepository).findById(2L);
        verify(routeRepository, never()).save(any());
    }

    @Test
    @DisplayName("#addNewRoute(RouteDto) should throw InputValidationException if pricePerSeat is less than 0")
    public void addNewRoute_ShouldThrowExceptionIfPricePerSeatLessThanZero() {
        Station departureStation = createStation(1L, "Kyiv", "Kyiv-Pas");
        Station arrivalStation = createStation(2L, "Lviv", "Lviv-Pas");
        LocalDateTime time = LocalDateTime.now();
        RouteDto routeDto = createRouteDtoWithStationsAndTime(
                1L, 2L, time, time.plusHours(5)
        );
        routeDto.setTrainName("Hogwarts Express");
        routeDto.setTotalSeats(200);
        routeDto.setPricePerSeat(-200.0);
        given(stationRepository.findById(1L)).willReturn(Optional.of(departureStation));
        given(stationRepository.findById(2L)).willReturn(Optional.of(arrivalStation));

        assertThatThrownBy(() -> routeService.addNewRoute(routeDto))
                .isInstanceOf(InputValidationException.class)
                .hasMessageContaining("exception.price.negative");

        verify(stationRepository).findById(1L);
        verify(stationRepository).findById(2L);
        verify(routeRepository, never()).save(any());
    }

    @Test
    @DisplayName("#addNewRoute(RouteDto) should throw InputValidationException if pricePerSeat is 0")
    public void addNewRoute_ShouldThrowExceptionIfPricePerSeatZero() {
        Station departureStation = createStation(1L, "Kyiv", "Kyiv-Pas");
        Station arrivalStation = createStation(2L, "Lviv", "Lviv-Pas");
        LocalDateTime time = LocalDateTime.now();
        RouteDto routeDto = createRouteDtoWithStationsAndTime(
                1L, 2L, time, time.plusHours(5)
        );
        routeDto.setTrainName("Hogwarts Express");
        routeDto.setTotalSeats(200);
        routeDto.setPricePerSeat(0.0);
        given(stationRepository.findById(1L)).willReturn(Optional.of(departureStation));
        given(stationRepository.findById(2L)).willReturn(Optional.of(arrivalStation));

        assertThatThrownBy(() -> routeService.addNewRoute(routeDto))
                .isInstanceOf(InputValidationException.class)
                .hasMessageContaining("exception.price.negative");

        verify(stationRepository).findById(1L);
        verify(stationRepository).findById(2L);
        verify(routeRepository, never()).save(any());
    }

    @Test
    @DisplayName("#deleteRoute(Long) should successfully delete route by id")
    public void deleteRoute_ShouldSuccessfullyDeleteRoute() {
        Long routeId = 1L;
        doNothing().when(routeRepository).deleteById(any());

        routeService.deleteRoute(routeId);

        verify(routeRepository).deleteById(routeId);
    }

    @Test
    @DisplayName("#updateRoute(Long, RouteDto) should successfully update Route")
    public void updateRoute_ShouldSuccessfullyUpdateRoute() {
        RouteDto routeDto = createStubRouteDto();
        Station departureStation = createStation(routeDto.getDepartureStationId(), "Kyiv", "Kyiv-Pas");
        Station arrivalStation = createStation(routeDto.getArrivalStationId(), "Edinburgh", "Hogwarts");
        Route route = createRoute(1L, departureStation, arrivalStation);
        given(routeRepository.findById(any())).willReturn(Optional.of(route));
        given(stationRepository.findById(routeDto.getDepartureStationId())).willReturn(Optional.of(departureStation));
        given(stationRepository.findById(routeDto.getArrivalStationId())).willReturn(Optional.of(arrivalStation));
        given(ticketRepository.findPurchasedTickets(any())).willReturn(100);
        doNothing().when(routeRepository).update(any());

        routeService.updateRoute(route.getId(), routeDto);

        verify(routeRepository).findById(route.getId());
        verify(stationRepository).findById(routeDto.getDepartureStationId());
        verify(stationRepository).findById(routeDto.getArrivalStationId());
        verify(ticketRepository).findPurchasedTickets(route.getId());
        verify(routeRepository).update(any());
    }

    @Test
    @DisplayName("#updateRoute(Long, RouteDto) should throw InputValidationException if totalSeats less than purchased tickets")
    public void updateRoute_ShouldThrowExceptionIfTotalSeatsLessThanPurchasedTickets() {
        RouteDto routeDto = createStubRouteDto();
        Station departureStation = createStation(routeDto.getDepartureStationId(), "Kyiv", "Kyiv-Pas");
        Station arrivalStation = createStation(routeDto.getArrivalStationId(), "Edinburgh", "Hogwarts");
        Route route = createRoute(1L, departureStation, arrivalStation);
        given(routeRepository.findById(any())).willReturn(Optional.of(route));
        given(stationRepository.findById(routeDto.getDepartureStationId())).willReturn(Optional.of(departureStation));
        given(stationRepository.findById(routeDto.getArrivalStationId())).willReturn(Optional.of(arrivalStation));
        given(ticketRepository.findPurchasedTickets(any())).willReturn(1000);

        assertThatThrownBy(() -> routeService.updateRoute(route.getId(), routeDto))
                .isInstanceOf(InputValidationException.class)
                .hasMessageContaining("exception.seats.lessThanPurchased");

        verify(routeRepository).findById(route.getId());
        verify(stationRepository).findById(routeDto.getDepartureStationId());
        verify(stationRepository).findById(routeDto.getArrivalStationId());
        verify(ticketRepository).findPurchasedTickets(route.getId());
        verify(routeRepository, never()).update(any());
    }

    @Test
    @DisplayName("#getAvailableRoutes(SearchRouteRequestDto) should return list of available routes")
    public void getAvailableRoutes_ShouldReturnListOfAvailableRoutes() {
        SearchRouteRequestDto searchRouteRequestDto = createStubSearchRouteRequestDto();
        Route route = createRoute(
                1L,
                searchRouteRequestDto.getDepartureStation(),
                searchRouteRequestDto.getArrivalStation()
        );
        given(routeRepository.findAvailableRoutesByCities(any(), any(), any(), any())).willReturn(List.of(route));
        given(ticketRepository.findPurchasedTickets(any())).willReturn(0);

        routeService.getAvailableRoutes(searchRouteRequestDto);

        verify(routeRepository).findAvailableRoutesByCities(
                searchRouteRequestDto.getDepartureStation().getCity(),
                searchRouteRequestDto.getArrivalStation().getCity(),
                searchRouteRequestDto.getDepartureDate().atStartOfDay(),
                searchRouteRequestDto.getDepartureDate().atStartOfDay().plusDays(1)
        );
        verify(ticketRepository, times(2)).findPurchasedTickets(any());
    }

    @Test
    @DisplayName("#getAvailableSeats(Route) should return more than 0 if there are available seats")
    public void getAvailableSeats_ShouldReturnMoreThanZeroIfSeatsAvailable() {
        Route route = Route.builder()
                .id(1L)
                .totalSeats(50)
                .build();
        given(ticketRepository.findPurchasedTickets(any())).willReturn(20);

        int availableSeats = routeService.getAvailableSeats(route);

        assertThat(availableSeats > 0).isTrue();
        verify(ticketRepository).findPurchasedTickets(route.getId());
    }

    @Test
    @DisplayName("#getAvailableSeats(Route) should return 0 or less if there are not available seats")
    public void getAvailableSeats_ShouldReturnZeroOrLessIfSeatsAreNotAvailable() {
        Route route = Route.builder()
                .id(1L)
                .totalSeats(50)
                .build();
        given(ticketRepository.findPurchasedTickets(any())).willReturn(50);

        int availableSeats = routeService.getAvailableSeats(route);

        assertThat(availableSeats > 0).isFalse();
        verify(ticketRepository).findPurchasedTickets(route.getId());
    }

    @Test
    @DisplayName("#findRouteById(Long) should successfully find route")
    public void findRouteById_ShouldSuccessfullyFindRoute() {
        Route route = Route.builder()
                .id(1L)
                .build();
        given(routeRepository.findById(any())).willReturn(Optional.of(route));

        routeService.findRouteById(1L);

        verify(routeRepository).findById(any());
    }

    @Test
    @DisplayName("#findRouteById(Long) should throw InputValidationException if route doesn't exist")
    public void findRouteById_ShouldThrowExceptionIfRouteDoesntExist() {
        given(routeRepository.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> routeService.findRouteById(1L))
                .isInstanceOf(InputValidationException.class)
                .hasMessageContaining("exception.route.notExist");

        verify(routeRepository).findById(any());
    }

    @Test
    @DisplayName("#getRouteResponseDto(Long) should return instance of SearchRouteResponseDto")
    public void getRouteResponseDto_ShouldReturnInstanceOfSearchRouteResponseDto() {
        Station departureStation = createStation(1L, "Kyiv", "Kyiv-Pas");
        Station arrivalStation = createStation(2L, "Edinburgh", "Hogwarts");
        Route route = createRoute(1L, departureStation, arrivalStation);
        given(routeRepository.findById(any())).willReturn(Optional.of(route));
        given(ticketRepository.findPurchasedTickets(any())).willReturn(0);

        SearchRouteResponseDto result = routeService.getRouteResponseDto(route.getId());

        verify(routeRepository).findById(route.getId());
        verify(ticketRepository).findPurchasedTickets(route.getId());
        assertThat(result).isNotNull();
        assertThat(route.getId()).isEqualTo(result.getRouteId());
        assertThat(route.getTrainName()).isEqualTo(result.getTrainName());
        assertThat(result.getDepartureStationName().contains(route.getDepartureStation().getCity())).isTrue();
        assertThat(result.getDepartureStationName().contains(route.getDepartureStation().getName())).isTrue();
        assertThat(result.getArrivalStationName().contains(route.getArrivalStation().getCity())).isTrue();
        assertThat(result.getArrivalStationName().contains(route.getArrivalStation().getName())).isTrue();
        assertThat(route.getDepartureTime()).isEqualTo(result.getDepartureDateTime());
        assertThat(route.getArrivalTime()).isEqualTo(result.getArrivalDateTime());
        assertThat(route.getTotalSeats()).isEqualTo(result.getTotalSeats());
        assertThat(route.getPricePerSeat()).isEqualTo(result.getPricePerSeat());
    }

    private static Station createStation(Long id, String city, String name) {
        Station station = new Station();
        station.setId(id);
        station.setCity(city);
        station.setName(name);
        return station;
    }

    private static RouteDto createRouteDtoWithStations(Long departureStationId, Long arrivalStationId) {
        RouteDto routeDto = new RouteDto();
        routeDto.setDepartureStationId(departureStationId);
        routeDto.setArrivalStationId(arrivalStationId);
        return routeDto;
    }

    private static RouteDto createRouteDtoWithStationsAndTime(
            Long departureStationId,
            Long arrivalStationId,
            LocalDateTime departureTime,
            LocalDateTime arrivalTime
    ) {
        RouteDto routeDto = createRouteDtoWithStations(departureStationId, arrivalStationId);
        routeDto.setDepartureTime(departureTime);
        routeDto.setArrivalTime(arrivalTime);
        return routeDto;
    }

    private static RouteDto createStubRouteDto() {
        RouteDto routeDto = createRouteDtoWithStationsAndTime(
                1L,
                2L,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(7)
        );
        routeDto.setTrainName("Hogwarts Express");
        routeDto.setTotalSeats(500);
        routeDto.setPricePerSeat(200.0);
        return routeDto;
    }

    private static SearchRouteRequestDto createStubSearchRouteRequestDto() {
        Station departureStation = createStation(null, "Kyiv", "Kyiv-Pas");
        Station arrivalStation = createStation(null, "Edinburgh", "Hogwarts");
        SearchRouteRequestDto searchRouteRequestDto = new SearchRouteRequestDto();
        searchRouteRequestDto.setDepartureStation(departureStation);
        searchRouteRequestDto.setArrivalStation(arrivalStation);
        searchRouteRequestDto.setDepartureDate(LocalDate.now());
        return searchRouteRequestDto;
    }

    private static Route createRoute(Long id, Station departureStation, Station arrivalStation) {
        return Route.builder()
                .id(id)
                .departureStation(departureStation)
                .arrivalStation(arrivalStation)
                .departureTime(LocalDateTime.now())
                .arrivalTime(LocalDateTime.now().plusDays(3))
                .trainName("The Hogwarts Express")
                .totalSeats(500)
                .pricePerSeat(100.0)
                .build();
    }
}
