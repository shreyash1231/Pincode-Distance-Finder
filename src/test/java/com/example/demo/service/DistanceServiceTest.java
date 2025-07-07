package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.model.RouteInfo;
import com.example.demo.repository.RouteInfoRepository;
import com.example.demo.util.GoogleMapsClient;

@ExtendWith(MockitoExtension.class)
class DistanceServiceTest {

    @Mock
    RouteInfoRepository repo;

    @Mock
    GoogleMapsClient googleMapsClient;

    @InjectMocks
    DistanceService service;

    @Test
    void testGetAllDistancesFromRepository() {
        RouteInfo expected = new RouteInfo(
            1L, "411014", "400001",
            "Pune", "Mumbai",  // city names here
            312.3, 250.5,
            "abc123", 18.5, 73.8, 19.0, 72.8
        );
        when(repo.findAllByFromPincodeAndToPincode("411014", "400001")).thenReturn(List.of(expected));

        List<RouteInfo> result = service.getAllDistances("411014", "400001");

        assertEquals(1, result.size());
        assertEquals("411014", result.get(0).getFromPincode());
        assertEquals("Pune", result.get(0).getFromCity());
        assertEquals(312.3, result.get(0).getDistanceKm());
    }
}
