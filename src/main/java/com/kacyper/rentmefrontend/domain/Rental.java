package com.kacyper.rentmefrontend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class Rental {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("rentedFrom")
    private LocalDate rentedFrom;

    @JsonProperty("rentedTo")
    private LocalDate rentedTo;

    @JsonProperty("duration")
    private Long duration;

    @JsonProperty("carId")
    private Long carId;

    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("cost")
    private BigDecimal cost;
}
