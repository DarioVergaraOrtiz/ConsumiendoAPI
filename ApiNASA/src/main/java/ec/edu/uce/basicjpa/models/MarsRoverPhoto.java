package ec.edu.uce.basicjpa.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MarsRoverPhoto {

    private String id;
    private String img_src;
    private String earth_date;
    private String cameraName; // Nombre de la cámara

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg_src() {
        return img_src;
    }

    public void setImg_src(String img_src) {
        if (img_src != null && !img_src.startsWith("https")) {
            img_src = img_src.replace("http", "https");
        }
        this.img_src = img_src;
    }

    public String getEarth_date() {
        return earth_date;
    }

    public void setEarth_date(String earth_date) {
        this.earth_date = earth_date;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }
}
