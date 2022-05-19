package com.kacyper.rentmefrontend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.kacyper.rentmefrontend.form.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class Car {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("vin")
    private String vin;

    @JsonProperty("carManufacture")
    private String carManufacture;

    @JsonProperty("model")
    private String model;

    @JsonProperty("productionYear")
    private int productionYear;

    @JsonProperty("fuel")
    private Fuel fuel;

    @JsonProperty("vehicleClass")
    private VehicleClass vehicleClass;

    @JsonProperty("id")
    private int mileage;

    @JsonProperty("dailyCost")
    private BigDecimal dailyCost;

    @JsonProperty("status")
    private Status status;
}
