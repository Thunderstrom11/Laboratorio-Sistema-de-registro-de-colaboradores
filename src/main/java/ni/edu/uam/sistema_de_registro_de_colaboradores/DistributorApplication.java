package ni.edu.uam.sistema_de_registro_de_colaboradores;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DistributorApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DistributorApplication.class.getResource("distributor-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 700);
        stage.setTitle("Sistema de Registro de Colaboradores");
        stage.setScene(scene);
        stage.show();
    }
}
