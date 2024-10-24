package com.DistanceGenerator.DistanceGenerator.repository;

import com.DistanceGenerator.DistanceGenerator.entity.RouteInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<RouteInfo, Long> {
    Optional<RouteInfo> findByFromPincodeAndToPincode(String fromPincode, String toPincode);
}
