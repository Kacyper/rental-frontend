package com.kacyper.rentmefrontend.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VinDecoderBodyDto {
    @JsonProperty("Make")
    private String make;

    @JsonProperty("Model")
    private String model;

    @JsonProperty("ModelYear")
    private String productionYear;

    @JsonProperty("FuelTypePrimary")
    private String fuelType;

    @JsonProperty("PlantCity")
    private String plantCity;

    @JsonProperty("ABS")
    private String abs;

    @JsonProperty("BodyClass")
    private String bodyClass;

    @JsonProperty("Manufacturer")
    private String carCompany;

    @JsonProperty("VehicleType")
    private String vehicleType;
}
