package com.kacyper.rentmefrontend.client;

import com.kacyper.rentmefrontend.api.VinApi;
import com.kacyper.rentmefrontend.api.VinDecoderDto;
import com.kacyper.rentmefrontend.configuration.RentMeBackendConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static java.util.Optional.ofNullable;

@Component
public class VinDecoderClient {

    private final RestTemplate restTemplate;
    private final RentMeBackendConfiguration rentMeBackendConfiguration;

    public VinDecoderClient(RestTemplate restTemplate, RentMeBackendConfiguration rentMeBackendConfiguration) {
        this.restTemplate = restTemplate;
        this.rentMeBackendConfiguration = rentMeBackendConfiguration;
    }

    public VinDecoderDto decodingVin(VinApi vinApi) {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(rentMeBackendConfiguration.getVindecoderApiEndpoint() + "/" + vinApi)
                    .build()
                    .encode()
                    .toUri();
            VinApi answer = restTemplate.getForObject(url, VinApi.class);
            return ofNullable(answer).orElse(new VinApi());
        } catch (RestClientException e) {
            return new VinApi();
        }
    }

}
