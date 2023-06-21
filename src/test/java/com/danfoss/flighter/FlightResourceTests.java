package com.danfoss.flighter;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.danfoss.flighter.db.Flight;
import com.danfoss.flighter.db.FlightStatus;
import com.danfoss.flighter.logger.MemoryAppender;
import com.danfoss.flighter.model.AirplaneDto;
import com.danfoss.flighter.model.AirportDto;
import com.danfoss.flighter.model.FlightDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static ch.qos.logback.classic.Level.DEBUG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getILoggerFactory;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FlightResourceTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Container
    private static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0");

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.driverClassName", mySQLContainer::getDriverClassName);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("spring.flyway.enabled", () -> "true");
    }


    private MemoryAppender memoryAppender;


    @BeforeEach
    public void beforeEach() {
        memoryAppender = new MemoryAppender();
        memoryAppender.setContext((LoggerContext) getILoggerFactory());
        Logger logger = (Logger) getLogger("org.hibernate.SQL");
        logger.setLevel(DEBUG);
        logger.addAppender(memoryAppender);
        memoryAppender.start();
    }

    @Test
    void getFlightById_thenStatus200() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/flights/{id}", 1)
                .accept(APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        FlightDto flightResult = objectMapper.readValue(result.getResponse().getContentAsByteArray(), FlightDto.class);
        assertThat(flightResult.getAirplane()).isNotNull();
        assertThat(flightResult.getDepartureAirport().getCode()).isEqualTo("WAW");
        assertThat(flightResult.getArrivalAirport().getCode()).isEqualTo("POZ");
        assertThat(flightResult.getNumber()).isEqualTo("BA2490");
        assertThat(memoryAppender.getSize()).isEqualTo(1);
    }

    @Test
    void getFlightById_thenStatus404() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/flights/{id}", Integer.MAX_VALUE)
                .accept(APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(404);
        assertThat(result.getResponse().getContentAsString()).isEmpty();
        assertThat(memoryAppender.getSize()).isEqualTo(1);
    }

    @Test
    void getFlightsByAirplaneId_thenStatus200AndNotEmptyList() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/flights")
                .param("airplaneId", "1")
                .accept(APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        List<FlightDto> flightsResult = objectMapper.readValue(result.getResponse().getContentAsByteArray(), new TypeReference<>() {});
        assertThat(flightsResult).hasSize(4);
        assertThat(memoryAppender.getSize()).isEqualTo(1);
    }

    @Test
    void getFlightsByAirplaneId_thenStatus200AndEmptyList() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/flights")
                .param("airplaneId", "1000000000000")
                .accept(APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        List<FlightDto> flightsResult = objectMapper.readValue(result.getResponse().getContentAsByteArray(), new TypeReference<>() {});
        assertThat(flightsResult).isEmpty();
        assertThat(memoryAppender.getSize()).isEqualTo(1);
    }

    @Test
    void getFlightStatus_thenStatus200() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/flights/{id}/status", 1)
                .accept(APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        Flight flight = objectMapper.readValue(result.getResponse().getContentAsByteArray(), Flight.class);
        assertThat(flight.getStatus()).isEqualTo(FlightStatus.NEW);

        assertThat(memoryAppender.getSize()).isEqualTo(1);
    }

    @Test
    void getFlightStatus_thenStatus404() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/flights/{id}/status", Integer.MAX_VALUE)
                .accept(APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(404);
        assertThat(result.getResponse().getContentAsString()).isEmpty();

        assertThat(memoryAppender.getSize()).isEqualTo(1);
    }

    @Test
    void createFlight_thenStatus201() throws Exception {

        FlightDto flight = FlightDto.builder()
                .status(FlightStatus.NEW)
                .departureTime(LocalDateTime.now())
                .arrivalTime(LocalDateTime.now().plusHours(3))
                .duration(Duration.ofHours(3))
                .airplane(AirplaneDto.builder().id(1L).build())
                .departureAirport(AirportDto.builder().id(1L).build())
                .arrivalAirport(AirportDto.builder().id(2L).build())
                .build();

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/flights")
                .content(objectMapper.writeValueAsString(flight))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(201);
        assertThat(memoryAppender.getSize()).isEqualTo(1);
    }

}
