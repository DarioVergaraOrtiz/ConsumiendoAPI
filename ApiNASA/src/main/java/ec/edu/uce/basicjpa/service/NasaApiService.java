package ec.edu.uce.basicjpa.service;

import ec.edu.uce.basicjpa.models.MarsPhotoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class NasaApiService {

    @Value("${mars.rover.api.key}")
    private String apiKey;

    @Value("${mars.rover.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public NasaApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<MarsPhotoResponse.Photo> getMarsPhotosFilteredByRoverAndCamera(String roverName, String cameraName) {
        try {
            String url = apiUrl + "/rovers/" + roverName + "/photos?camera=" + cameraName + "&sol=1000&api_key=" + apiKey;
            System.out.println("Realizando solicitud a: " + url);

            MarsPhotoResponse photoResponse = restTemplate.getForObject(url, MarsPhotoResponse.class);
            List<MarsPhotoResponse.Photo> photos = photoResponse.getPhotos();

            if (photos.isEmpty()) {
                System.out.println("No se encontraron fotos para el rover: " + roverName + ", cámara: " + cameraName);
            } else {
                System.out.println("Se encontraron " + photos.size() + " fotos para el rover: " + roverName + ", cámara: " + cameraName);
            }

            return photos;
        } catch (HttpClientErrorException.NotFound ex) {
            System.err.println("Error 404: Recurso no encontrado. Rover: " + roverName + ", Cámara: " + cameraName);
            ex.printStackTrace();
            return Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Error al obtener fotos de Marte para el rover " + roverName + " y la cámara " + cameraName + ": " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
