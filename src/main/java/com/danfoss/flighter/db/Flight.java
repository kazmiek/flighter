package com.danfoss.flighter.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Duration;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;


@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
@Table
@Entity
public class Flight {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;
  String number;
  LocalDateTime departureTime;
  LocalDateTime arrivalTime;
  Duration duration;
  @Enumerated(EnumType.STRING)
  FlightStatus status;

  // FIXME cascade has more sense and is useful for OneToMany relations. Should I add cascade for the presentation?.
  @ManyToOne//(cascade = {CascadeType.PERSIST})
  @JoinColumn(name="departure_airport_id", nullable=false)
  Airport departureAirport;

  @ManyToOne//(cascade = {CascadeType.PERSIST})
  @JoinColumn(name="arrival_airport_id", nullable=false)
  Airport arrivalAirport;

  @ManyToOne//(cascade = {CascadeType.PERSIST})
  @JoinColumn(name="airplane_id", nullable=false)
  Airplane airplane;

}
