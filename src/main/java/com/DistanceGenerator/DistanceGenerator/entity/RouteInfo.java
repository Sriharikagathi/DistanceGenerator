package com.DistanceGenerator.DistanceGenerator.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fromPincode;
    private String toPincode;
    private String distance;
    private String duration;

    @Lob
    private String routeDetails;

}

