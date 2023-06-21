package com.kazmiek.flighter.model;

import com.kazmiek.flighter.db.FlightStatus;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(type = "string", format = "duration",
            description = "Duration defined by ISO 8601. Format PnDTnHnMn.nS. Example: \"P2DT3H4M\"  -- parses as \"2 days, 3 hours and 4 minutes\"")
    Duration duration;
    FlightStatus status;
    AirportDto departureAirport;
    AirportDto arrivalAirport;
    AirplaneDto airplane;
}
