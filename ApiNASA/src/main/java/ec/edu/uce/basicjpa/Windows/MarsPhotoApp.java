package ec.edu.uce.basicjpa.Windows;

import ec.edu.uce.basicjpa.models.MarsPhotoResponse;
import ec.edu.uce.basicjpa.service.NasaApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.*;

@SpringBootApplication
public class MarsPhotoApp extends JFrame {

    private JComboBox<String> roverComboBox;
    private JComboBox<String> cameraComboBox;
    private JButton searchButton;
    private JPanel photoPanel;
    private JLabel messageLabel;

    private final NasaApiService nasaApiService;
    private  ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Autowired
    public MarsPhotoApp(NasaApiService nasaApiService) {
        this.nasaApiService = nasaApiService;
        initialize();
    }

    private void initialize() {
        setTitle("Mars Rover Photos Aplication");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        roverComboBox = new JComboBox<>(new String[]{"curiosity"}); // Solo Curiosity
        cameraComboBox = new JComboBox<>();
        searchButton = new JButton("Buscar");
        messageLabel = new JLabel("Observaciones");

        topPanel.add(new JLabel("Rover:"));
        topPanel.add(roverComboBox);
        topPanel.add(new JLabel("Camera:"));
        topPanel.add(cameraComboBox);
        topPanel.add(searchButton);
        topPanel.add(messageLabel);

        add(topPanel, BorderLayout.NORTH);

        photoPanel = new JPanel();
        photoPanel.setLayout(new GridLayout(0, 3, 10, 10));
        add(new JScrollPane(photoPanel), BorderLayout.CENTER);

        roverComboBox.addActionListener(e -> updateCameras());
        searchButton.addActionListener(e -> searchPhotos());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                executorService.shutdownNow();
                try {
                    if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                        System.err.println("El ejecutor no terminó en el tiempo especificado.");
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                super.windowClosing(e);
            }
        });

        updateCameras(); // Inicializa las cámaras para el rover seleccionado por defecto
    }

    private void updateCameras() {
        String selectedRover = (String) roverComboBox.getSelectedItem();
        List<String> availableCameras = List.of("FHAZ", "RHAZ", "MAST", "CHEMCAM", "MAHLI", "MARDI", "NAVCAM"); // Cámaras disponibles para Curiosity

        cameraComboBox.removeAllItems();
        availableCameras.forEach(cameraComboBox::addItem);
    }

    private void searchPhotos() {
        String selectedRover = (String) roverComboBox.getSelectedItem();
        String selectedCamera = (String) cameraComboBox.getSelectedItem();

        // Detener las tareas en ejecución
        executorService.shutdownNow();
        executorService = Executors.newFixedThreadPool(10); // Reiniciar el ExecutorService

        // Limpiar el panel de fotos antes de cargar nuevas imágenes
        photoPanel.removeAll();
        photoPanel.revalidate();
        photoPanel.repaint();

        List<MarsPhotoResponse.Photo> photos = nasaApiService.getMarsPhotosFilteredByRoverAndCamera(selectedRover, selectedCamera);
        displayPhotos(photos, selectedRover, selectedCamera);

        // Actualizar el mensaje
        messageLabel.setText( photos.size() + " fotos encontradas para el rover: " + selectedRover + ", cámara: " + selectedCamera);
    }

    private void displayPhotos(List<MarsPhotoResponse.Photo> photos, String selectedRover, String selectedCamera) {
        photoPanel.removeAll();

        photos.forEach(photo -> executorService.submit(() -> {
            try {
                JLabel photoLabel = createPhotoLabel(photo);
                if (photoLabel != null) {
                    SwingUtilities.invokeLater(() -> {
                        photoPanel.add(photoLabel);
                        photoPanel.revalidate();
                        photoPanel.repaint();
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }




    private JLabel createPhotoLabel(MarsPhotoResponse.Photo photo) throws IOException {
        String imageUrl = photo.getImg_src().startsWith("http://") ? photo.getImg_src().replace("http://", "https://") : photo.getImg_src();
        URL url = new URL(imageUrl);
        ImageIcon imageIcon = new ImageIcon(ImageIO.read(url).getScaledInstance(200, 200, Image.SCALE_SMOOTH));
        JLabel photoLabel = new JLabel(imageIcon);
        photoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openUrlInBrowser(photo.getImg_src());
                }
            }
        });
        return photoLabel;
    }

    private void openUrlInBrowser(String url) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

}
