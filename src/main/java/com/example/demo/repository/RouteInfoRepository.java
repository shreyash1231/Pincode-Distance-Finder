package com.example.demo.repository;

import com.example.demo.model.RouteInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RouteInfoRepository extends JpaRepository<RouteInfo, Long> {
    Optional<RouteInfo> findByFromPincodeAndToPincode(String fromPincode, String toPincode);
    List<RouteInfo> findAllByFromPincodeAndToPincode(String fromPincode, String toPincode);
}
