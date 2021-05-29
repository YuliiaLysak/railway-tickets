package service;

import exceptions.BusinessLogicException;
import model.Route;
import model.Ticket;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.TicketRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private RouteService routeService;

    @InjectMocks
    private TicketService ticketService;

    @Captor
    private ArgumentCaptor<Ticket> ticketCaptor;

    @Test
    @DisplayName("#buyTicket(Long, Long) should successfully save ticket")
    void buyTicket_shouldSuccessfullySaveTicket() {
        Route route = Route.builder().id(5L).build();
        given(routeService.findRouteById(any())).willReturn(route);
        given(routeService.getAvailableSeats(any())).willReturn(1);
        given(ticketRepository.save(any())).will(invocation -> invocation.getArgument(0));

        ticketService.buyTicket(1L, 5L);

        verify(routeService).findRouteById(5L);
        verify(routeService).getAvailableSeats(route);
        verify(ticketRepository).save(ticketCaptor.capture());

        Ticket ticketForSaving = ticketCaptor.getValue();
        assertThat(ticketForSaving.getPurchaseDate()).isBetween(
                LocalDateTime.now().minusSeconds(1), LocalDateTime.now()
        );
        assertThat(ticketForSaving.getUserId()).isEqualTo(1L);
        assertThat(ticketForSaving.getRouteId()).isEqualTo(5L);
    }

    @Test
    @DisplayName("#buyTicket(Long, Long) should throw BusinessLogicException if route doesn't have any available seat")
    void buyTicket_shouldThrowExceptionWhenSaveTicketWithoutAvailableSeats() {
        Route route = Route.builder().id(5L).build();
        given(routeService.findRouteById(any())).willReturn(route);
        given(routeService.getAvailableSeats(any())).willReturn(0);

        assertThatThrownBy(() -> ticketService.buyTicket(1L, 5L))
                .isInstanceOf(BusinessLogicException.class)
                .hasMessageContaining("exception.ticket.unavailable");

        verify(ticketRepository, never()).save(any(Ticket.class));
    }
}
