package com.example.demo.service;

import com.example.demo.model.RouteInfo;
import com.example.demo.repository.RouteInfoRepository;
import com.example.demo.util.GoogleMapsClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DistanceService {

    @Autowired
    private RouteInfoRepository routeRepo;

    @Autowired
    private GoogleMapsClient googleMapsClient;

    @Cacheable(value = "routes", key = "#from + '-' + #to")
    public List<RouteInfo> getAllDistances(String from, String to) {
        List<RouteInfo> existingRoutes = routeRepo.findAllByFromPincodeAndToPincode(from, to);
        return existingRoutes.isEmpty() ? fetchAndSaveRoutes(from, to) : existingRoutes;
    }

    private List<RouteInfo> fetchAndSaveRoutes(String from, String to) {
        Map<String, Object> response = googleMapsClient.getRoute(from, to);

        // Extract cities from Google Maps response
        String fromCity = googleMapsClient.extractFromCity(response);
        String toCity = googleMapsClient.extractToCity(response);

        List<Map> routes = (List<Map>) response.get("routes");
        List<RouteInfo> routeInfos = new ArrayList<>();

        for (Map route : routes) {
            Map leg = ((List<Map>) route.get("legs")).get(0);

            double distanceKm = ((Integer) ((Map) leg.get("distance")).get("value")) / 1000.0;
            double durationMin = ((Integer) ((Map) leg.get("duration")).get("value")) / 60.0;
            String polyline = (String) ((Map) route.get("overview_polyline")).get("points");

            Map start = (Map) leg.get("start_location");
            Map end = (Map) leg.get("end_location");

            double fromLat = (Double) start.get("lat");
            double fromLng = (Double) start.get("lng");
            double toLat = (Double) end.get("lat");
            double toLng = (Double) end.get("lng");

            RouteInfo routeInfo = new RouteInfo(null, from, to, fromCity, toCity,
                    distanceKm, durationMin, polyline,
                    fromLat, fromLng, toLat, toLng);

            routeRepo.save(routeInfo);
            routeInfos.add(routeInfo);
        }

        return routeInfos;
    }
}
