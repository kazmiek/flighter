package com.danfoss.flighter.service;

import com.danfoss.flighter.db.FlightStatusView;
import com.danfoss.flighter.model.FlightDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FlightService {
    FlightDto create(FlightDto e);

    Optional<FlightDto> findById(Long id);

    List<FlightDto> findBy(String airportCode, LocalDate date);

    List<FlightDto> findByAirplaneId(Long airplaneId);

    FlightDto update(Long id, FlightDto flight);

    void delete(Long id);

    Optional<FlightStatusView> getStatus(Long id);
}
