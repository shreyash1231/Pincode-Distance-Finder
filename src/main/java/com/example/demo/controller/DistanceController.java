package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.RouteInfo;
import com.example.demo.service.DistanceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DistanceController {

    @Autowired
    private DistanceService distanceService;

    @GetMapping("/distance")
    public ResponseEntity<?> getAllRoutes(
            @RequestParam String from,
            @RequestParam String to) {
        try {
            if (from == null || to == null || from.isBlank() || to.isBlank()) {
                throw new IllegalArgumentException("Both 'from' and 'to' parameters are required and cannot be blank.");
            }

            List<RouteInfo> routes = distanceService.getAllDistances(from, to);
            return ResponseEntity.ok(routes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid input: " + e.getMessage());
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("A required resource was null: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

}
