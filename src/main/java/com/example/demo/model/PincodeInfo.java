package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class PincodeInfo {
    @Id
    private String pincode;
    private double latitude;
    private double longitude;
    private String polygonJson;
}
