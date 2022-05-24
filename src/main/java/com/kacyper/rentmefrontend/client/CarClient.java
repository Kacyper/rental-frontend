package com.kacyper.rentmefrontend.client;

import com.kacyper.rentmefrontend.configuration.RentMeBackendConfiguration;
import com.kacyper.rentmefrontend.domain.Car;
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
public class CarClient {

    private final RestTemplate restTemplate;

    private final RentMeBackendConfiguration rentMeBackendConfiguration;

    @Autowired
    public CarClient(RestTemplate restTemplate, RentMeBackendConfiguration rentMeBackendConfiguration) {
        this.restTemplate = restTemplate;
        this.rentMeBackendConfiguration = rentMeBackendConfiguration;
    }

    public List<Car> getCar() {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(rentMeBackendConfiguration.getCarEndpoint() + "/getCars")
                    .build()
                    .encode()
                    .toUri();
            Car[] answer = restTemplate.getForObject(url, Car[].class);
            return Arrays.asList(ofNullable(answer).orElse(new Car[0]));
        } catch (RestClientException e) {
            return new ArrayList<>();
        }
    }

    public void saveCar(Car car) {
        URI url = UriComponentsBuilder.fromHttpUrl(rentMeBackendConfiguration.getCarEndpoint() + "/addCar")
                .build()
                .encode()
                .toUri();
        restTemplate.postForObject(url, car, Car.class);
    }

    public void updateCar(Car car) {
        URI url = UriComponentsBuilder.fromHttpUrl(rentMeBackendConfiguration.getCarEndpoint() + "/modifyCar")
                .build()
                .encode()
                .toUri();
        restTemplate.put(url, car);
    }

    public void deleteCar(Long id) {
        URI url = UriComponentsBuilder.fromHttpUrl(rentMeBackendConfiguration.getCarEndpoint() + "/" + id)
                .build()
                .encode()
                .toUri();
        restTemplate.delete(url);
    }

}