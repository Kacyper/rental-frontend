package com.kacyper.rentmefrontend.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class RentMeBackendConfiguration {

    @Value("${car.api.endpoint}")
    private String carEndpoint;

    @Value("${rental.api.endpoint}")
    private String rentalEndpoint;

    @Value("${user.api.endpoint}")
    private String userEndpoint;

    @Value("${vindecoder.api.endpoint}")
    private String vindecoderApiEndpoint;

    @Value("${login.api.endpoint}")
    private String loginEndpoint;
}
