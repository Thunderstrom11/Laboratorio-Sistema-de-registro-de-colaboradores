package ni.edu.uam.sistema_de_registro_de_colaboradores.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import ni.edu.uam.sistema_de_registro_de_colaboradores.models.Collaborator;
import ni.edu.uam.sistema_de_registro_de_colaboradores.utils.AlertUtils;

import java.time.LocalDate;

public class DistributorController {

    @FXML
    private TextField txtName;
    @FXML
    private TextField txtSurname;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField pwfPassword;
    @FXML
    private ComboBox<String> cbCharge;
    @FXML
    private DatePicker dpContractdate;
    @FXML
    private ListView<String> lstvContracttype;
    @FXML
    private RadioButton rbtnNormalcontract;
    @FXML
    private RadioButton rbtnAnnormalcontract;
    @FXML
    private CheckBox chbxVacations;
    @FXML
    private CheckBox chxbSaturdaysoff;
    @FXML
    private CheckBox chxbExit6pm;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnClean;
    @FXML
    private Button btnDelete;
    @FXML
    private TableView<Collaborator> tvUsers;
    @FXML
    private TableColumn<Collaborator, String> colFullname;
    @FXML
    private TableColumn<Collaborator, String> colCharge;
    @FXML
    private TableColumn<Collaborator, String> colArea;
    @FXML
    private TableColumn<Collaborator, String> colContractdate;
    @FXML
    private TableColumn<Collaborator, String> colContracttype;
    @FXML
    private TableColumn<Collaborator, String> colBenefits;

    private ObservableList<Collaborator> collaborators = FXCollections.observableArrayList();

    @FXML
    protected void initialize() {
        cbCharge.getItems().addAll("Vendedor", "Bodeguero", "Contador", "Administrador");
        lstvContracttype.getItems().addAll("Ventas", "Bodega", "Administracion", "Logistica");

        colFullname.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colCharge.setCellValueFactory(new PropertyValueFactory<>("position"));
        colArea.setCellValueFactory(new PropertyValueFactory<>("workArea"));
        colContractdate.setCellValueFactory(new PropertyValueFactory<>("hireDateText"));
        colContracttype.setCellValueFactory(new PropertyValueFactory<>("contractType"));
        colBenefits.setCellValueFactory(new PropertyValueFactory<>("benefits"));

        tvUsers.setItems(collaborators);

        btnSave.setOnAction(this::saveCollaborator);
        btnUpdate.setOnAction(this::updateCollaborator);
        btnClean.setOnAction(e -> clearForm());
        btnDelete.setOnAction(this::deleteCollaborator);
        tvUsers.setOnMouseClicked(this::tableClick);

        MenuItem editItem = new MenuItem("Editar");
        editItem.setOnAction(this::editCollaborator);
        MenuItem deleteItem = new MenuItem("Eliminar");
        deleteItem.setOnAction(this::deleteCollaborator);
        tvUsers.setContextMenu(new ContextMenu(editItem, deleteItem));

        Platform.runLater(this::wireMenusAndKeys);
    }

    private void wireMenusAndKeys() {
        Scene scene = btnSave.getScene();
        VBox root = (VBox) scene.getRoot();
        MenuBar menuBar = (MenuBar) root.getChildren().get(1);
        menuBar.getMenus().get(0).getItems().get(0).setOnAction(e -> clearForm());
        menuBar.getMenus().get(1).getItems().get(0).setOnAction(e -> Platform.exit());
        menuBar.getMenus().get(2).getItems().get(0).setOnAction(e -> showAbout());
        scene.setOnKeyPressed(this::keyPressed);
    }

    private void saveCollaborator(ActionEvent event) {
        String error = validateForm();
        if (error != null) {
            AlertUtils.showAlert("Datos invalidos", error);
            return;
        }
        Collaborator collaborator = new Collaborator(
                txtName.getText(),
                txtSurname.getText(),
                txtUsername.getText(),
                pwfPassword.getText(),
                cbCharge.getValue(),
                lstvContracttype.getSelectionModel().getSelectedItem(),
                dpContractdate.getValue(),
                selectedContractType(),
                selectedBenefits()
        );
        collaborators.add(collaborator);
        clearForm();
    }

    private void updateCollaborator(ActionEvent event) {
        Collaborator selected = tvUsers.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showAlert("Sin seleccion", "Seleccione un colaborador de la tabla");
            return;
        }
        String error = validateForm();
        if (error != null) {
            AlertUtils.showAlert("Datos invalidos", error);
            return;
        }
        selected.setFirstName(txtName.getText());
        selected.setLastName(txtSurname.getText());
        selected.setUsername(txtUsername.getText());
        selected.setPassword(pwfPassword.getText());
        selected.setPosition(cbCharge.getValue());
        selected.setWorkArea(lstvContracttype.getSelectionModel().getSelectedItem());
        selected.setHireDate(dpContractdate.getValue());
        selected.setContractType(selectedContractType());
        selected.setBenefits(selectedBenefits());
        tvUsers.refresh();
        clearForm();
    }

    private void deleteCollaborator(ActionEvent event) {
        Collaborator selected = tvUsers.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showAlert("Sin seleccion", "Seleccione un colaborador de la tabla");
            return;
        }
        collaborators.remove(selected);
        clearForm();
    }

    private void tableClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            fillForm();
        }
    }

    private void keyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            saveCollaborator(null);
        }
        if (event.getCode() == KeyCode.ESCAPE) {
            clearForm();
        }
    }

    private void editCollaborator(ActionEvent event) {
        fillForm();
    }

    private void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Acerca de");
        alert.setHeaderText(null);
        alert.setContentText("Sistema de Registro de Colaboradores - Distribuidora El Gueguense");
        alert.showAndWait();
    }

    private void fillForm() {
        Collaborator selected = tvUsers.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        txtName.setText(selected.getFirstName());
        txtSurname.setText(selected.getLastName());
        txtUsername.setText(selected.getUsername());
        pwfPassword.setText(selected.getPassword());
        cbCharge.setValue(selected.getPosition());
        lstvContracttype.getSelectionModel().select(selected.getWorkArea());
        dpContractdate.setValue(selected.getHireDate());
        if ("Normal".equals(selected.getContractType())) {
            rbtnNormalcontract.setSelected(true);
        } else if ("Quebrado".equals(selected.getContractType())) {
            rbtnAnnormalcontract.setSelected(true);
        }
        chbxVacations.setSelected(selected.getBenefits().contains("Vacaciones"));
        chxbSaturdaysoff.setSelected(selected.getBenefits().contains("Sabados libres"));
        chxbExit6pm.setSelected(selected.getBenefits().contains("Salida 6:00 PM"));
    }

    private String validateForm() {
        if (txtName.getText().isEmpty() || txtSurname.getText().isEmpty()
                || txtUsername.getText().isEmpty() || pwfPassword.getText().isEmpty()
                || cbCharge.getValue() == null
                || lstvContracttype.getSelectionModel().getSelectedItem() == null
                || dpContractdate.getValue() == null || selectedContractType() == null) {
            return "Ningun campo puede quedar vacio";
        }
        if (txtUsername.getText().length() < 5) {
            return "El usuario debe tener al menos 5 caracteres";
        }
        if (pwfPassword.getText().length() < 8) {
            return "La contrasena debe tener al menos 8 caracteres";
        }
        if (dpContractdate.getValue().isAfter(LocalDate.now())) {
            return "La fecha de contratacion no puede ser posterior a la actual";
        }
        if (selectedBenefits().isEmpty()) {
            return "Debe seleccionar al menos un beneficio";
        }
        return null;
    }

    private String selectedContractType() {
        if (rbtnNormalcontract.isSelected()) {
            return "Normal";
        }
        if (rbtnAnnormalcontract.isSelected()) {
            return "Quebrado";
        }
        return null;
    }

    private String selectedBenefits() {
        String benefits = "";
        if (chbxVacations.isSelected()) {
            benefits = benefits + "Vacaciones, ";
        }
        if (chxbSaturdaysoff.isSelected()) {
            benefits = benefits + "Sabados libres, ";
        }
        if (chxbExit6pm.isSelected()) {
            benefits = benefits + "Salida 6:00 PM, ";
        }
        if (benefits.length() > 0) {
            benefits = benefits.substring(0, benefits.length() - 2);
        }
        return benefits;
    }

    private void clearForm() {
        txtName.clear();
        txtSurname.clear();
        txtUsername.clear();
        pwfPassword.clear();
        cbCharge.setValue(null);
        lstvContracttype.getSelectionModel().clearSelection();
        dpContractdate.setValue(null);
        rbtnNormalcontract.setSelected(false);
        rbtnAnnormalcontract.setSelected(false);
        chbxVacations.setSelected(false);
        chxbSaturdaysoff.setSelected(false);
        chxbExit6pm.setSelected(false);
    }
}
