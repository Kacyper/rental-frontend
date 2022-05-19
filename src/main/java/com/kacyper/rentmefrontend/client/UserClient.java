package com.kacyper.rentmefrontend.client;

import com.kacyper.rentmefrontend.configuration.RentMeBackendConfiguration;
import com.kacyper.rentmefrontend.domain.User;
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
public class UserClient {

    private final RestTemplate restTemplate;

    private final RentMeBackendConfiguration rentMeBackendConfiguration;

    @Autowired
    public UserClient(RestTemplate restTemplate, RentMeBackendConfiguration rentMeBackendConfiguration) {
        this.restTemplate = restTemplate;
        this.rentMeBackendConfiguration = rentMeBackendConfiguration;
    }

    public void createUser(User user) {
        URI url = UriComponentsBuilder.fromHttpUrl(rentMeBackendConfiguration.getUserEndpoint())
                .build()
                .encode()
                .toUri();
        restTemplate.postForObject(url, user, User.class);
    }

    public Boolean doesUserExist(String email) {
        URI url = UriComponentsBuilder.fromHttpUrl(rentMeBackendConfiguration.getUserEndpoint() + "/isUserRegistered")
                .queryParam("email", email)
                .build()
                .encode()
                .toUri();
        return restTemplate.getForObject(url, Boolean.class);
    }

    public List<User> getAllUsers() {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(rentMeBackendConfiguration.getUserEndpoint())
                    .build()
                    .encode()
                    .toUri();
            User[] answer = restTemplate.getForObject(url, User[].class);
            return Arrays.asList(ofNullable(answer).orElse(new User[0]));
        } catch (RestClientException e) {
            return new ArrayList<>();
        }
    }

    public User getUserByEmail(String email) {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(rentMeBackendConfiguration.getUserEndpoint() + "/getByEmail/" + email)
                    .build()
                    .encode()
                    .toUri();
            User answer = restTemplate.getForObject(url, User.class);
            return ofNullable(answer).orElse(new User());
        } catch (RestClientException e) {
            return new User();
        }
    }

    public User getUserByPhoneNumber(int phoneNumber) {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(rentMeBackendConfiguration.getUserEndpoint() + "/getByPhoneNumber/" + phoneNumber)
                    .build()
                    .encode()
                    .toUri();
            User answer = restTemplate.getForObject(url, User.class);
            return ofNullable(answer).orElse(new User());
        } catch (RestClientException e) {
            return new User();
        }
    }

    public void deleteUser(Long id) {
        URI url = UriComponentsBuilder.fromHttpUrl(rentMeBackendConfiguration.getUserEndpoint() + "/deleteUser/" + id)
                .build()
                .encode()
                .toUri();
        restTemplate.delete(url);
    }

}
