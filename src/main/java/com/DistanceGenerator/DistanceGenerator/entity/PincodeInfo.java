package com.DistanceGenerator.DistanceGenerator.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PincodeInfo {
    @Id
    private String pincode;
    private String latitude;
    private String longitude;

    @Lob
    private String polygon; // Store polygon information

    // Getters and Setters
}