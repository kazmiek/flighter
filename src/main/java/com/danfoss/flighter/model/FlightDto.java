package com.danfoss.flighter.model;

import com.danfoss.flighter.db.FlightStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Duration;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class FlightDto {

    Long id;
    String number;
    LocalDateTime departureTime;
    LocalDateTime arrivalTime;
    Duration duration;
    FlightStatus status;
    AirportDto departureAirport;
    AirportDto arrivalAirport;
    AirplaneDto airplane;
}
