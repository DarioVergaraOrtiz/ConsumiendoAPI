package ec.edu.uce.basicjpa.Windows;

import ec.edu.uce.basicjpa.models.MarsPhotoResponse;
import ec.edu.uce.basicjpa.service.NasaApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

// Importaciones omitidas por brevedad

@SpringBootApplication
public class MarsPhotoApp extends JFrame {

    private JComboBox<String> cameraComboBox;
    private JButton searchButton;
    private JPanel photoPanel;

    private final NasaApiService nasaApiService;

    @Autowired
    public MarsPhotoApp(NasaApiService nasaApiService) {
        this.nasaApiService = nasaApiService;
        initialize();
    }

    private void initialize() {
        setTitle("Mars Rover Photos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        cameraComboBox = new JComboBox<>();
        searchButton = new JButton("Search");

        topPanel.add(new JLabel("Camera:"));
        // Obtener la lista de cámaras disponibles del servicio (no se utiliza directamente en esta versión)
        List<String> availableCameras = nasaApiService.getAvailableCameras();
        for (String camera : availableCameras) {
            cameraComboBox.addItem(camera);
        }

        topPanel.add(cameraComboBox);
        topPanel.add(searchButton);

        add(topPanel, BorderLayout.NORTH);

        photoPanel = new JPanel();
        photoPanel.setLayout(new BorderLayout());
        add(new JScrollPane(photoPanel), BorderLayout.CENTER);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchPhotos();
            }
        });
    }


    private void searchPhotos() {
        String selectedCamera = (String) cameraComboBox.getSelectedItem();
        List<MarsPhotoResponse.Photo> photos = nasaApiService.getMarsPhotosFilteredByCamera(selectedCamera);
        displayPhotos(photos);
    }

    private void displayPhotos(List<MarsPhotoResponse.Photo> photos) {
        DefaultListModel<String> photoUrls = new DefaultListModel<>();

        for (MarsPhotoResponse.Photo photo : photos) {
            photoUrls.addElement(photo.getImg_src());
        }

        JList<String> photoList = new JList<>(photoUrls);
        photoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        photoList.setVisibleRowCount(10);

        // Agregar un listener para abrir la URL seleccionada en un navegador
        photoList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Doble clic
                    String selectedUrl = photoList.getSelectedValue();
                    if (selectedUrl != null && Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().browse(new URI(selectedUrl));
                        } catch (IOException | URISyntaxException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });

        photoPanel.removeAll();
        photoPanel.add(new JScrollPane(photoList));

        photoPanel.revalidate();
        photoPanel.repaint();
    }



    public static void main(String[] args) {
        SpringApplication.run(MarsPhotoApp.class, args);
    }
}

