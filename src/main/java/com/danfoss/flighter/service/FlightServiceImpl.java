package com.danfoss.flighter.service;

import com.danfoss.flighter.db.FlightRepository;
import com.danfoss.flighter.db.FlightStatusView;
import com.danfoss.flighter.mapper.Mapper;
import com.danfoss.flighter.model.FlightDto;
import lombok.AllArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.danfoss.flighter.db.FlightStatus.*;

@Service
@AllArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final Mapper mapper = Mappers.getMapper(Mapper.class);

    @Override
    public FlightDto create(FlightDto flight) {
        return mapper.map(flightRepository.save(mapper.map(flight)));
    }

    @Override
    public Optional<FlightDto> findById(Long flightId) {
        return flightRepository.findById(flightId).map(mapper::map);
    }

    @Override
    public List<FlightDto> findBy(String airportCode, LocalDate date) {
        return flightRepository.findFlightsByDepartureAirport_CodeAndDepartureTimeBetween(
                airportCode, date.atStartOfDay(), LocalDateTime.of(date, LocalTime.MAX))
                .stream().map(mapper::map).toList();
    }

    @Override
    public List<FlightDto> findByAirplaneId(Long airplaneId) {
        return flightRepository.findFlightsByAirplane_Id(airplaneId)
                .stream().map(mapper::map).toList();
    }

    @Override
    public FlightDto update(Long id, FlightDto flight) {
        //TODO think about optimistic locking in case concurrent changes on entity.
        return mapper.map(flightRepository.save(mapper.map(id, flight)));
    }

    @Override
    public void delete(Long flightId) {
        //TODO discuss exception handling in case non-existing entity.
        flightRepository.findById(flightId).map(flight -> {
            flight.setStatus(DELETED);
            return flight;
        }).ifPresent(flightRepository::save);
    }

    @Override
    public Optional<FlightStatusView> getStatus(Long id) {
        return flightRepository.getFlightStatusById(id);
    }
}
