package com.example.demo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class GoogleMapsClient {

    @Value("${google.maps.api.key}")
    private String apiKey;

    public Map<String, Object> getRoute(String fromPincode, String toPincode) {
        String url = String.format(
            "https://maps.googleapis.com/maps/api/directions/json?origin=%s&destination=%s&alternatives=true&key=%s",
            fromPincode, toPincode, apiKey
        );

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response == null || !"OK".equals(response.get("status"))) {
            throw new RuntimeException("Failed to fetch from Google Maps API");
        }

        return response;
    }

    // Extract city from start_address of first leg
    public String extractFromCity(Map<String, Object> response) {
        Map leg = getFirstLeg(response);
        String startAddress = (String) leg.get("start_address");
        return parseCityFromAddress(startAddress);
    }

    // Extract city from end_address of first leg
    public String extractToCity(Map<String, Object> response) {
        Map leg = getFirstLeg(response);
        String endAddress = (String) leg.get("end_address");
        return parseCityFromAddress(endAddress);
    }

    private String parseCityFromAddress(String address) {
        if (address == null || address.isBlank()) return "";

        // Simple heuristic: city is usually the first part before the first comma
        String[] parts = address.split(",");
        if (parts.length >= 1) {
            return parts[0].trim();
        }
        return address.trim();
    }

    public double extractDistanceKm(Map<String, Object> response) {
        Map leg = getFirstLeg(response);
        int meters = (int) ((Map) leg.get("distance")).get("value");
        return meters / 1000.0;
    }

    public double extractDurationMinutes(Map<String, Object> response) {
        Map leg = getFirstLeg(response);
        int seconds = (int) ((Map) leg.get("duration")).get("value");
        return seconds / 60.0;
    }

    public String extractPolyline(Map<String, Object> response) {
        Map route = ((List<Map>) response.get("routes")).get(0);
        return (String) ((Map) route.get("overview_polyline")).get("points");
    }

    private Map getFirstLeg(Map<String, Object> response) {
        Map route = ((List<Map>) response.get("routes")).get(0);
        return ((List<Map>) route.get("legs")).get(0);
    }

    public double extractFromLatitude(Map<String, Object> response) {
        Map leg = getFirstLeg(response);
        Map startLocation = (Map) leg.get("start_location");
        return (double) startLocation.get("lat");
    }

    public double extractFromLongitude(Map<String, Object> response) {
        Map leg = getFirstLeg(response);
        Map startLocation = (Map) leg.get("start_location");
        return (double) startLocation.get("lng");
    }

    public double extractToLatitude(Map<String, Object> response) {
        Map leg = getFirstLeg(response);
        Map endLocation = (Map) leg.get("end_location");
        return (double) endLocation.get("lat");
    }

    public double extractToLongitude(Map<String, Object> response) {
        Map leg = getFirstLeg(response);
        Map endLocation = (Map) leg.get("end_location");
        return (double) endLocation.get("lng");
    }
}
