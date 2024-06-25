package ec.edu.uce.basicjpa.service;
import ec.edu.uce.basicjpa.models.MarsRoverPhoto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class MarsRoverPhotoService {

    private final RestTemplate restTemplate;
    private final String apiUrl;
    private final String apiKey;

    public MarsRoverPhotoService(RestTemplate restTemplate,
                                 @Value("${mars.rover.api.url}") String apiUrl,
                                 @Value("${mars.rover.api.key}") String apiKey) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
    }

    public MarsRoverPhoto[] getPhotos(String roverName, int sol, String camera) {
        URI uri = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .pathSegment("rovers", roverName, "photos")
                .queryParam("sol", sol)
                .queryParam("api_key", apiKey)
                .queryParam("camera", camera)
                .build()
                .toUri();

        return restTemplate.getForObject(uri, MarsRoverPhoto[].class);
    }
}

