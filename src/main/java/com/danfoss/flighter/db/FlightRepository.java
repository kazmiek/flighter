package com.danfoss.flighter.db;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    @EntityGraph(attributePaths = {"departureAirport", "arrivalAirport", "airplane"})
    List<Flight> findFlightsByDepartureAirport_CodeAndDepartureTimeBetween(String airportCode, LocalDateTime startTime, LocalDateTime endTime);

    @EntityGraph(attributePaths = {"departureAirport", "arrivalAirport", "airplane"})
    List<Flight> findFlightsByAirplane_Id(Long airplaneId);

    Optional<FlightStatusView> getFlightStatusById(Long id);
}
