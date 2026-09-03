package ni.edu.uam.sistema_de_registro_de_colaboradores.utils;

import javafx.scene.control.Alert;

public class AlertUtils {

    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
