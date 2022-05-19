package com.kacyper.rentmefrontend.client;

import com.kacyper.rentmefrontend.configuration.RentMeBackendConfiguration;
import com.kacyper.rentmefrontend.domain.Login;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class LoginClient {

    private final RestTemplate restTemplate;

    private final RentMeBackendConfiguration rentMeBackendConfiguration;

    public LoginClient(RestTemplate restTemplate, RentMeBackendConfiguration rentMeBackendConfiguration) {
        this.restTemplate = restTemplate;
        this.rentMeBackendConfiguration = rentMeBackendConfiguration;
    }

    public Boolean isLoginExisting (Login login) {
        URI url = UriComponentsBuilder.fromHttpUrl(rentMeBackendConfiguration.getLoginEndpoint() + "/isLoginUsed")
                .queryParam("email", login.getEmail())
                .queryParam("password", login.getPassword())
                .build()
                .encode()
                .toUri();
        return restTemplate.getForObject(url, Boolean.class);
    }
}
