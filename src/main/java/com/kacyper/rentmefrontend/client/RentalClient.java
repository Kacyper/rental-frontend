package com.kacyper.rentmefrontend.client;

import com.kacyper.rentmefrontend.configuration.RentMeBackendConfiguration;
import com.kacyper.rentmefrontend.domain.Rental;
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

    public List<Rental> getAllRentals() {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(rentMeBackendConfiguration.getRentalEndpoint())
                    .build()
                    .encode()
                    .toUri();
            Rental[] answer = restTemplate.getForObject(url, Rental[].class);
            return Arrays.asList(ofNullable(answer).orElse(new Rental[0]));
        } catch (RestClientException e) {
            return new ArrayList<>();
        }
    }

    public List<Rental> getRentalsByUsersId(Long userId) {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(rentMeBackendConfiguration.getRentalEndpoint() + "/byUserId/" + userId)
                    .build()
                    .encode()
                    .toUri();
            Rental[] answer = restTemplate.getForObject(url, Rental[].class);
            return Arrays.asList(ofNullable(answer).orElse(new Rental[0]));
        } catch (RestClientException e) {
            return new ArrayList<>();
        }
    }

    public void createRent(Rental rental) {
        URI url = UriComponentsBuilder.fromHttpUrl(rentMeBackendConfiguration.getRentalEndpoint())
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
}
