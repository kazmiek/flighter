package com.danfoss.flighter.mapper;

import com.danfoss.flighter.db.Flight;
import com.danfoss.flighter.model.FlightDto;
import org.mapstruct.Mapping;

@org.mapstruct.Mapper
public interface Mapper {
    @Mapping(target = "id", source = "id")
    Flight map(Long id, FlightDto input);

    Flight map( FlightDto input);

    FlightDto map(Flight input);

}
