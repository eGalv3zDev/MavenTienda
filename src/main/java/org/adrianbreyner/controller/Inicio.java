package org.adrianbreyner.controller;

import java.sql.CallableStatement;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.adrianbreyner.db.Conexion;
import org.adrianbreyner.system.Main;

public class Inicio implements Initializable {

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtContraseña;

    private Main principal;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }
    
    @FXML
    private void PaginaPrincipal(){
        principal.PaginaPrincipal();
    }


    
    @FXML
    private void btnIniciarSesionAction() {
    String email = txtEmail.getText().trim();
    String contraseña = txtContraseña.getText().trim();

    if (email.isEmpty() || contraseña.isEmpty()) {
        mostrarAlerta("Debe llenar todos los campos.", Alert.AlertType.WARNING);
        return;
    }

    try {
        PreparedStatement enunciado = Conexion.getInstancia().getConexion()
            .prepareCall("call sp_verificarEmail(?, ?);");
        enunciado.setString(1, email);
        enunciado.setString(2, contraseña);

        ResultSet resultado = enunciado.executeQuery();

        if (resultado.next()) {
            int idUsuario = resultado.getInt("idUsuario");
            String rol = resultado.getString("rol");
            
            Main.setIdUsuarioActual(idUsuario);

            mostrarAlerta("Inicio de sesión exitoso", Alert.AlertType.INFORMATION);

            if (rol.equalsIgnoreCase("administrador")) {
                principal.Productos();
            } else if (rol.equalsIgnoreCase("cliente")) {
                principal.PaginaInicio();
            } else {
                mostrarAlerta("Rol no reconocido: " + rol, Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Email o contraseña incorrectos", Alert.AlertType.ERROR);
        }

    } catch (Exception e) {
        e.printStackTrace();
        mostrarAlerta("Error al iniciar sesión", Alert.AlertType.ERROR);
    }
}

    private void mostrarAlerta(String mensaje, Alert.AlertType tipo){
        Alert alerta = new Alert(tipo);
        alerta.setTitle("Inicio de Sesión");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
