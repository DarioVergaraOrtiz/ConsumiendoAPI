package ec.edu.uce.basicjpa.service;

import ec.edu.uce.basicjpa.models.MarsPhotoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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

    public List<MarsPhotoResponse.Photo> getMarsPhotosFilteredByCamera(String cameraName) {
        try {
            // Construir la URL completa con parámetros de cámara y sol
            String url = apiUrl + "/photos?camera=" + cameraName + "&sol=1000&api_key=" + apiKey;

            // Registro de la URL para verificación
            System.out.println("Realizando solicitud a: " + url);

            // Realizar la solicitud HTTP y obtener la respuesta
            MarsPhotoResponse photoResponse = restTemplate.getForObject(url, MarsPhotoResponse.class);

            // Obtener la lista de fotos de la respuesta
            List<MarsPhotoResponse.Photo> photos = photoResponse.getPhotos();

            // Manejar el caso donde no se encuentran fotos
            if (photos.isEmpty()) {
                System.out.println("No se encontraron fotos para la cámara: " + cameraName);
            } else {
                System.out.println("Se encontraron " + photos.size() + " fotos para la cámara: " + cameraName);
            }

            return photos;
        } catch (HttpClientErrorException.NotFound ex) {
            System.err.println("Error 404: Recurso no encontrado. Nombre de cámara: " + cameraName);
            ex.printStackTrace();
            return Collections.emptyList(); // o manejo de error apropiado
        } catch (Exception e) {
            System.err.println("Error al obtener fotos de Marte para la cámara " + cameraName + ": " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList(); // o manejo de error apropiado
        }
    }


    public List<String> getAvailableCameras() {
        // Método para obtener las cámaras disponibles, no utilizado directamente en este contexto
        return List.of("FHAZ", "RHAZ", "MAST", "CHEMCAM", "MAHLI", "MARDI", "NAVCAM", "PANCAM", "MINITES");
    }
}
