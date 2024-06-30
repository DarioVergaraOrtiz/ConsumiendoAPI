package ec.edu.uce.basicjpa.models;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MarsPhotoResponse {
    private List<Photo> photos;

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Photo {
        private Long id;
        private String sol;
        private Camera camera;
        private String img_src;
        private String earth_date;
        private Rover rover;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getSol() {
            return sol;
        }

        public void setSol(String sol) {
            this.sol = sol;
        }

        public Camera getCamera() {
            return camera;
        }

        public void setCamera(Camera camera) {
            this.camera = camera;
        }

        public String getImg_src() {
            return img_src;
        }

        public void setImg_src(String img_src) {
            this.img_src = img_src;
        }

        public Rover getRover() {
            return rover;
        }

        public void setRover(Rover rover) {
            this.rover = rover;
        }

        public String getEarth_date() {
            return earth_date;
        }

        public void setEarth_date(String earth_date) {
            this.earth_date = earth_date;
        }

        public static class Camera {
            private Long id;
            private String name;
            private Long rover_id;
            private String full_name;

            public Long getId() {
                return id;
            }

            public void setId(Long id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Long getRover_id() {
                return rover_id;
            }

            public void setRover_id(Long rover_id) {
                this.rover_id = rover_id;
            }

            public String getFull_name() {
                return full_name;
            }

            public void setFull_name(String full_name) {
                this.full_name = full_name;
            }
        }

        public static class Rover {
            private Long id;
            private String name;
            private String landing_date;
            private String launch_date;
            private String status;

            public Long getId() {
                return id;
            }

            public void setId(Long id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getLanding_date() {
                return landing_date;
            }

            public void setLanding_date(String landing_date) {
                this.landing_date = landing_date;
            }

            public String getLaunch_date() {
                return launch_date;
            }

            public void setLaunch_date(String launch_date) {
                this.launch_date = launch_date;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }
    }
}
