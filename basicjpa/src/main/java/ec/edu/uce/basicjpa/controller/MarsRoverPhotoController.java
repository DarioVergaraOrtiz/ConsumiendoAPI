package ec.edu.uce.basicjpa.controller;

import ec.edu.uce.basicjpa.models.MarsRoverPhoto;
import ec.edu.uce.basicjpa.service.MarsRoverPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mars-photos")
public class MarsRoverPhotoController {

    private final MarsRoverPhotoService photoService;

    @Autowired
    public MarsRoverPhotoController(MarsRoverPhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping("/rovers/{roverName}/photos")
    public MarsRoverPhoto[] getPhotos(
            @PathVariable String roverName,
            @RequestParam int sol,
            @RequestParam(required = false) String camera) {

        return photoService.getPhotos(roverName, sol, camera);
    }
}
