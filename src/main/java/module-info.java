module ni.edu.uam.sistema_de_registro_de_colaboradores {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;


    opens ni.edu.uam.sistema_de_registro_de_colaboradores.controllers to javafx.fxml;
    opens ni.edu.uam.sistema_de_registro_de_colaboradores.models to javafx.base;
    exports ni.edu.uam.sistema_de_registro_de_colaboradores;
}