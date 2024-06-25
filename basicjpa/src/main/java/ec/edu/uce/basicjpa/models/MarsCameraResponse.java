package ec.edu.uce.basicjpa.models;
import java.util.List;

public class MarsCameraResponse {

    private List<Camera> cameras;

    public List<Camera> getCameras() {
        return cameras;
    }

    public void setCameras(List<Camera> cameras) {
        this.cameras = cameras;
    }

    public static class Camera {
        private long id;
        private String name;
        private long rover_id;
        private String full_name;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getRover_id() {
            return rover_id;
        }

        public void setRover_id(long rover_id) {
            this.rover_id = rover_id;
        }

        public String getFull_name() {
            return full_name;
        }

        public void setFull_name(String full_name) {
            this.full_name = full_name;
        }
    }
}
