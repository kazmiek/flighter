package com.kazmiek.flighter.resource;

import com.kazmiek.flighter.db.FlightStatusView;
import com.kazmiek.flighter.model.FlightDto;
import com.kazmiek.flighter.service.FlightService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("flights")
@AllArgsConstructor
public class FlightResource {

    private final FlightService flightService;

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    @ResponseBody
    public FlightDto create(@RequestBody FlightDto flight) {
        return flightService.create(flight);
    }

    @RequestMapping(value = "/{id}", method = PUT)
    @ResponseStatus(OK)
    @ResponseBody
    public FlightDto update(@PathVariable("id") Long id, @RequestBody FlightDto flight) {
        return flightService.update(id, flight);
    }

    @RequestMapping(value = "/{id}", method = DELETE)
    @ResponseStatus(NO_CONTENT)
    @ResponseBody
    public void delete(@PathVariable("id") Long id) {
        flightService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = GET)
    @ResponseBody
    public FlightDto findById(@PathVariable("id") Long id) {
        return flightService.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }

    @RequestMapping(params = {"airportCode", "departureDate"}, method = GET)
    @ResponseBody
    public List<FlightDto> findBy(@RequestParam("airportCode") String airportCode, @RequestParam("departureDate") LocalDate departureDate) {
        return flightService.findBy(airportCode, departureDate);
    }

    @RequestMapping(params = {"airplaneId"}, method = GET)
    @ResponseBody
    public List<FlightDto> findBy(@RequestParam("airplaneId") Long airplaneId) {
        return flightService.findByAirplaneId(airplaneId);
    }

    @RequestMapping(value = "/{id}/status", method = GET)
    @ResponseBody
    public FlightStatusView getStatus(@PathVariable("id") Long id) {
        return flightService.getStatus(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }
}
