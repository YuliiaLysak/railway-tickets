package edu.lysak.railwaytickets.service;

import edu.lysak.railwaytickets.exceptions.BusinessLogicException;
import edu.lysak.railwaytickets.exceptions.InputValidationException;
import edu.lysak.railwaytickets.model.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import edu.lysak.railwaytickets.repository.StationRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StationServiceTest {
    @Mock
    private StationRepository stationRepository;

    @InjectMocks
    private StationService stationService;

    @Test
    @DisplayName("#getAllStations() should return all stations")
    void getAllStations_ShouldReturnAllStations() {
        List<Station> expected = List.of(new Station());
        given(stationRepository.findAll()).willReturn(expected);

        List<Station> actual = stationService.getAllStations();

        verify(stationRepository).findAll();
        assertThat(expected).isEqualTo(actual);
    }

    @Test
    @DisplayName("#addNewStation(Station) should successfully add new station")
    void addNewStation_ShouldSuccessfullyAddNewStation() {
        Station station = createStation(null, "Hogwarts", "King's Cross Station, Platform 9¾");
        given(stationRepository.findByCityAndName(any(), any())).willReturn(null);
        given(stationRepository.save(any())).will(invocation -> invocation.getArgument(0));

        stationService.addNewStation(station);

        verify(stationRepository).findByCityAndName("Hogwarts", "King's Cross Station, Platform 9¾");
        verify(stationRepository).save(station);
    }

    @Test
    @DisplayName("#addNewStation(Station) should throw BusinessLogicException if this station already exists")
    void addNewStation_ShouldThrowExceptionIfStationAlreadyExist() {
        Station station = createStation(null, "Hogwarts", "King's Cross Station, Platform 9¾");
        given(stationRepository.findByCityAndName(any(), any())).willReturn(station);

        assertThatThrownBy(() -> stationService.updateStation(1L, station))
                .isInstanceOf(BusinessLogicException.class)
                .hasMessageContaining("exception.station.exist");

        verify(stationRepository).findByCityAndName("Hogwarts", "King's Cross Station, Platform 9¾");
        verify(stationRepository, never())
                .updateStationById(any(), any(), any());
    }

    @Test
    @DisplayName("#deleteStation(Long) should successfully delete station by id")
    void deleteStation_ShouldSuccessfullyDeleteStationById() {
        Long stationId = 1L;
        doNothing().when(stationRepository).deleteById(any());

        stationService.deleteStation(stationId);

        verify(stationRepository).deleteById(stationId);
    }

    @Test
    @DisplayName("#updateStation(Long, Station) should successfully update station")
    void updateStation_ShouldSuccessfullyUpdateStation() {
        Station station = createStation(1L, "Hogwarts", "King's Cross Station, Platform 9¾");
        given(stationRepository.findByCityAndName(any(), any())).willReturn(null);
        doNothing().when(stationRepository).updateStationById(any(), any(), any());

        stationService.updateStation(1L, station);

        verify(stationRepository).findByCityAndName("Hogwarts", "King's Cross Station, Platform 9¾");
        verify(stationRepository).updateStationById(1L, "Hogwarts", "King's Cross Station, Platform 9¾");
    }

    @Test
    @DisplayName("#updateStation(Long, Station) should throw InputValidationException if station city is null")
    void updateStation_ShouldThrowExceptionIfStationCityIsNull() {
        Station station = createStation(1L, null, "King's Cross Station, Platform 9¾");
        given(stationRepository.findByCityAndName(any(), any())).willReturn(null);

        assertThatThrownBy(() -> stationService.updateStation(1L, station))
                .isInstanceOf(InputValidationException.class)
                .hasMessageContaining("exception.stationCity.empty");

        verify(stationRepository).findByCityAndName(null, "King's Cross Station, Platform 9¾");
        verify(stationRepository, never())
                .updateStationById(any(Long.class), any(String.class), any(String.class));
    }

    @Test
    @DisplayName("#updateStation(Long, Station) should throw InputValidationException if station city is empty")
    void updateStation_ShouldThrowExceptionIfStationCityIsEmpty() {
        Station station = createStation(1L, "", "King's Cross Station, Platform 9¾");
        given(stationRepository.findByCityAndName(any(), any())).willReturn(null);

        assertThatThrownBy(() -> stationService.updateStation(1L, station))
                .isInstanceOf(InputValidationException.class)
                .hasMessageContaining("exception.stationCity.empty");

        verify(stationRepository).findByCityAndName("", "King's Cross Station, Platform 9¾");
        verify(stationRepository, never())
                .updateStationById(any(Long.class), any(String.class), any(String.class));
    }

    @Test
    @DisplayName("#updateStation(Long, Station) should throw InputValidationException if station name is null")
    void updateStation_ShouldThrowExceptionIfStationNameIsNull() {
        Station station = createStation(1L, "Hogwarts", null);
        given(stationRepository.findByCityAndName(any(), any())).willReturn(null);

        assertThatThrownBy(() -> stationService.updateStation(1L, station))
                .isInstanceOf(InputValidationException.class)
                .hasMessageContaining("exception.stationName.empty");

        verify(stationRepository).findByCityAndName("Hogwarts", null);
        verify(stationRepository, never())
                .updateStationById(any(Long.class), any(String.class), any(String.class));
    }

    @Test
    @DisplayName("#updateStation(Long, Station) should throw InputValidationException if station name is empty")
    void updateStation_ShouldThrowExceptionIfStationNameIsEmpty() {
        Station station = createStation(1L, "Hogwarts", "");
        given(stationRepository.findByCityAndName(any(), any())).willReturn(null);

        assertThatThrownBy(() -> stationService.updateStation(1L, station))
                .isInstanceOf(InputValidationException.class)
                .hasMessageContaining("exception.stationName.empty");

        verify(stationRepository).findByCityAndName("Hogwarts", "");
        verify(stationRepository, never())
                .updateStationById(any(Long.class), any(String.class), any(String.class));
    }

    private static Station createStation(Long id, String city, String name) {
        Station station = new Station();
        station.setId(id);
        station.setCity(city);
        station.setName(name);
        return station;
    }
}
