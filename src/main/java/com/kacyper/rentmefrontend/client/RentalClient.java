package com.kacyper.rentmefrontend.client;

import com.kacyper.rentmefrontend.configuration.RentMeBackendConfiguration;
import com.kacyper.rentmefrontend.domain.Rental;
import com.kacyper.rentmefrontend.domain.RentalsExtended;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Optional.ofNullable;

@Component
public class RentalClient {

    private final RestTemplate restTemplate;

    private final RentMeBackendConfiguration rentMeBackendConfiguration;

    @Autowired
    public RentalClient(RestTemplate restTemplate, RentMeBackendConfiguration rentMeBackendConfiguration) {
        this.restTemplate = restTemplate;
        this.rentMeBackendConfiguration = rentMeBackendConfiguration;
    }

    public List<RentalsExtended> getAllRentals() {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(rentMeBackendConfiguration.getRentalEndpoint() + "/getAllRentals")
                    .build()
                    .encode()
                    .toUri();
            RentalsExtended[] answer = restTemplate.getForObject(url, RentalsExtended[].class);
            return Arrays.asList(ofNullable(answer).orElse(new RentalsExtended[0]));
        } catch (RestClientException e) {
            return new ArrayList<>();
        }
    }

    public List<RentalsExtended> getRentalsByUsersId(Long userId) {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(rentMeBackendConfiguration.getRentalEndpoint() + "/byUserId/" + userId)
                    .build()
                    .encode()
                    .toUri();
            RentalsExtended[] answer = restTemplate.getForObject(url, RentalsExtended[].class);
            return Arrays.asList(ofNullable(answer).orElse(new RentalsExtended[0]));
        } catch (RestClientException e) {
            return new ArrayList<>();
        }
    }

    public void createRent(Rental rental) {
        URI url = UriComponentsBuilder.fromHttpUrl(rentMeBackendConfiguration.getRentalEndpoint() + "/createRent")
                .build()
                .encode()
                .toUri();
        restTemplate.postForObject(url, rental, Rental.class);
    }

    public void endRent(Long id) {
        URI url = UriComponentsBuilder.fromHttpUrl(rentMeBackendConfiguration.getRentalEndpoint() + "/" + id)
                .build()
                .encode()
                .toUri();
        restTemplate.delete(url);
    }

    public void modifyRental(Rental rental) {
        URI url = UriComponentsBuilder.fromHttpUrl(rentMeBackendConfiguration.getRentalEndpoint() + "/modifyRent")
                .build()
                .encode()
                .toUri();
        restTemplate.put(url, rental);
    }
}
