package com.example.demo.controller;

import com.example.demo.model.RouteInfo;
import com.example.demo.service.DistanceService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DistanceController.class)
public class DistanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DistanceService distanceService;

    @Test
    void shouldReturnMultipleRoutes() throws Exception {
        RouteInfo route1 = new RouteInfo(
            1L, "411014", "400001",
            "Pune", "Mumbai",  // city names here
            320.0, 280.0,
            "polyline1", 18.5, 73.8, 19.0, 72.8
        );

        RouteInfo route2 = new RouteInfo(
            2L, "411014", "400001",
            "Pune", "Mumbai",  // city names here
            325.0, 285.0,
            "polyline2", 18.5, 73.8, 19.0, 72.8
        );

        when(distanceService.getAllDistances("411014", "400001")).thenReturn(List.of(route1, route2));

        mockMvc.perform(get("/api/distance")
                        .param("from", "411014")
                        .param("to", "400001")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].fromCity").value("Pune"))
                .andExpect(jsonPath("$[0].toCity").value("Mumbai"))
                .andExpect(jsonPath("$[0].distanceKm").value(320.0))
                .andExpect(jsonPath("$[1].routePolyline").value("polyline2"));
    }
}
