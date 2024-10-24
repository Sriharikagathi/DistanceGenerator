package com.DistanceGenerator.DistanceGenerator.repository;

import com.DistanceGenerator.DistanceGenerator.entity.PincodeInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PincodeInfoRepository extends JpaRepository<PincodeInfo, String> {
}