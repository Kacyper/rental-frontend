package com.kacyper.rentmefrontend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@Builder
@AllArgsConstructor
@ToString
public class RentalsExtended {

    private Long id;
    private LocalDate rentedFrom;
    private LocalDate rentedTo;
    private BigDecimal rentalCost;
    private Long carId;
    private String carManufacture;
    private String carModel;
    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private int userPhoneNumber;
}
