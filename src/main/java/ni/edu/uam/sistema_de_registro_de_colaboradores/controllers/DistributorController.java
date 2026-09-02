package ni.edu.uam.sistema_de_registro_de_colaboradores.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import ni.edu.uam.sistema_de_registro_de_colaboradores.models.Collaborator;
import ni.edu.uam.sistema_de_registro_de_colaboradores.utils.AlertUtils;

import java.time.LocalDate;

public class DistributorController {

    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private ComboBox<String> cmbPosition;
    @FXML
    private ListView<String> lstWorkArea;
    @FXML
    private DatePicker dpHireDate;
    @FXML
    private RadioButton rbFullTime;
    @FXML
    private RadioButton rbPartTime;
    @FXML
    private CheckBox chkMedicalInsurance;
    @FXML
    private CheckBox chkPaidVacation;
    @FXML
    private CheckBox chkBonus;
    @FXML
    private TableView<Collaborator> tvCollaborators;
    @FXML
    private TableColumn<Collaborator, String> colFullName;
    @FXML
    private TableColumn<Collaborator, String> colPosition;
    @FXML
    private TableColumn<Collaborator, String> colWorkArea;
    @FXML
    private TableColumn<Collaborator, String> colHireDate;
    @FXML
    private TableColumn<Collaborator, String> colContractType;
    @FXML
    private TableColumn<Collaborator, String> colBenefits;

    private ObservableList<Collaborator> collaborators = FXCollections.observableArrayList();

    @FXML
    protected void initialize() {
        cmbPosition.getItems().addAll("Vendedor", "Bodeguero", "Contador", "Administrador");
        lstWorkArea.getItems().addAll("Ventas", "Bodega", "Administracion", "Logistica");

        colFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colPosition.setCellValueFactory(new PropertyValueFactory<>("position"));
        colWorkArea.setCellValueFactory(new PropertyValueFactory<>("workArea"));
        colHireDate.setCellValueFactory(new PropertyValueFactory<>("hireDateText"));
        colContractType.setCellValueFactory(new PropertyValueFactory<>("contractType"));
        colBenefits.setCellValueFactory(new PropertyValueFactory<>("benefits"));

        tvCollaborators.setItems(collaborators);
    }

    @FXML
    private void saveCollaborator(ActionEvent event) {
        String error = validateForm();
        if (error != null) {
            AlertUtils.showAlert("Datos invalidos", error);
            return;
        }
        Collaborator collaborator = new Collaborator(
                txtFirstName.getText(),
                txtLastName.getText(),
                txtUsername.getText(),
                pfPassword.getText(),
                cmbPosition.getValue(),
                lstWorkArea.getSelectionModel().getSelectedItem(),
                dpHireDate.getValue(),
                selectedContractType(),
                selectedBenefits()
        );
        collaborators.add(collaborator);
        clearForm();
    }

    @FXML
    private void updateCollaborator(ActionEvent event) {
        Collaborator selected = tvCollaborators.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showAlert("Sin seleccion", "Seleccione un colaborador de la tabla");
            return;
        }
        String error = validateForm();
        if (error != null) {
            AlertUtils.showAlert("Datos invalidos", error);
            return;
        }
        selected.setFirstName(txtFirstName.getText());
        selected.setLastName(txtLastName.getText());
        selected.setUsername(txtUsername.getText());
        selected.setPassword(pfPassword.getText());
        selected.setPosition(cmbPosition.getValue());
        selected.setWorkArea(lstWorkArea.getSelectionModel().getSelectedItem());
        selected.setHireDate(dpHireDate.getValue());
        selected.setContractType(selectedContractType());
        selected.setBenefits(selectedBenefits());
        tvCollaborators.refresh();
        clearForm();
    }

    @FXML
    private void deleteCollaborator(ActionEvent event) {
        Collaborator selected = tvCollaborators.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showAlert("Sin seleccion", "Seleccione un colaborador de la tabla");
            return;
        }
        collaborators.remove(selected);
        clearForm();
    }

    @FXML
    private void clearFields(ActionEvent event) {
        clearForm();
    }

    @FXML
    private void tableClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            fillForm();
        }
    }

    @FXML
    private void keyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            saveCollaborator(null);
        }
        if (event.getCode() == KeyCode.ESCAPE) {
            clearForm();
        }
    }

    @FXML
    private void editCollaborator(ActionEvent event) {
        fillForm();
    }

    @FXML
    private void menuNew(ActionEvent event) {
        clearForm();
    }

    @FXML
    private void menuExit(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void menuAbout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Acerca de");
        alert.setHeaderText(null);
        alert.setContentText("Sistema de Registro de Colaboradores - Distribuidora El Gueguense");
        alert.showAndWait();
    }

    private void fillForm() {
        Collaborator selected = tvCollaborators.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        txtFirstName.setText(selected.getFirstName());
        txtLastName.setText(selected.getLastName());
        txtUsername.setText(selected.getUsername());
        pfPassword.setText(selected.getPassword());
        cmbPosition.setValue(selected.getPosition());
        lstWorkArea.getSelectionModel().select(selected.getWorkArea());
        dpHireDate.setValue(selected.getHireDate());
        if ("Tiempo completo".equals(selected.getContractType())) {
            rbFullTime.setSelected(true);
        } else if ("Medio tiempo".equals(selected.getContractType())) {
            rbPartTime.setSelected(true);
        }
        chkMedicalInsurance.setSelected(selected.getBenefits().contains("Seguro medico"));
        chkPaidVacation.setSelected(selected.getBenefits().contains("Vacaciones pagadas"));
        chkBonus.setSelected(selected.getBenefits().contains("Bono"));
    }

    private String validateForm() {
        if (txtFirstName.getText().isEmpty() || txtLastName.getText().isEmpty()
                || txtUsername.getText().isEmpty() || pfPassword.getText().isEmpty()
                || cmbPosition.getValue() == null
                || lstWorkArea.getSelectionModel().getSelectedItem() == null
                || dpHireDate.getValue() == null || selectedContractType() == null) {
            return "Ningun campo puede quedar vacio";
        }
        if (txtUsername.getText().length() < 5) {
            return "El usuario debe tener al menos 5 caracteres";
        }
        if (pfPassword.getText().length() < 8) {
            return "La contrasena debe tener al menos 8 caracteres";
        }
        if (dpHireDate.getValue().isAfter(LocalDate.now())) {
            return "La fecha de contratacion no puede ser posterior a la actual";
        }
        if (selectedBenefits().isEmpty()) {
            return "Debe seleccionar al menos un beneficio";
        }
        return null;
    }

    private String selectedContractType() {
        if (rbFullTime.isSelected()) {
            return "Tiempo completo";
        }
        if (rbPartTime.isSelected()) {
            return "Medio tiempo";
        }
        return null;
    }

    private String selectedBenefits() {
        String benefits = "";
        if (chkMedicalInsurance.isSelected()) {
            benefits = benefits + "Seguro medico, ";
        }
        if (chkPaidVacation.isSelected()) {
            benefits = benefits + "Vacaciones pagadas, ";
        }
        if (chkBonus.isSelected()) {
            benefits = benefits + "Bono, ";
        }
        if (benefits.length() > 0) {
            benefits = benefits.substring(0, benefits.length() - 2);
        }
        return benefits;
    }

    private void clearForm() {
        txtFirstName.clear();
        txtLastName.clear();
        txtUsername.clear();
        pfPassword.clear();
        cmbPosition.setValue(null);
        lstWorkArea.getSelectionModel().clearSelection();
        dpHireDate.setValue(null);
        rbFullTime.setSelected(false);
        rbPartTime.setSelected(false);
        chkMedicalInsurance.setSelected(false);
        chkPaidVacation.setSelected(false);
        chkBonus.setSelected(false);
    }
}
