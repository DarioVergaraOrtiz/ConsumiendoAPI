package ec.edu.uce.basicjpa;

import ec.edu.uce.basicjpa.Windows.MarsPhotoApp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import javax.swing.*;

/**
 * @author : Dario Vergara
 * @Topic: Consumo de API NASA
 */

@SpringBootApplication
public class MarsPhotoApplication {

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");

        ApplicationContext context = SpringApplication.run(MarsPhotoApplication.class, args);
        SwingUtilities.invokeLater(() -> {
            MarsPhotoApp app = context.getBean(MarsPhotoApp.class);
            app.setVisible(true);
        });
    }
}
