package org.adrianbreyner.controller;

import java.net.URL;
import java.sql.CallableStatement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.adrianbreyner.db.Conexion;
import org.adrianbreyner.model.Usuarios;
import org.adrianbreyner.system.Main;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class RegistroController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    private enum acciones {
        AGREGAR, NINGUNA
    };
    acciones tipoAccion = acciones.NINGUNA;
    private Main principal;
    private Usuarios modeloUsuario;
    @FXML
    private TextField txtIdUsuario, txtUsuario, txtApellido, txtEmail, txtNit, txtDireccion;
    @FXML
    private PasswordField txtContraseña;

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }

    public Main getPrincipal() {
        return principal;
    }
    
    @FXML
    private void PaginaPrincipal(){
        principal.PaginaPrincipal();
    }

    public Usuarios obtenerModeloUsuario() {
        int idUsuario;
        String usuario = txtUsuario.getText();
        String apellido = txtApellido.getText();
        if (txtIdUsuario.getText().isEmpty()) {
            idUsuario = 1;
        } else {
            idUsuario = Integer.parseInt(txtIdUsuario.getText());
        }
        String email = txtEmail.getText();
        String contraseña = txtContraseña.getText();
        String nit = txtNit.getText();
        String direccion = txtDireccion.getText();
        Usuarios usuarios = new Usuarios(idUsuario, apellido, usuario, email, contraseña, nit, direccion);
        return usuarios;
    }

    public void agregarUsuario() {
        modeloUsuario = obtenerModeloUsuario();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().
                    prepareCall("call sp_agregarUsuario(?,?,?,?,?,?,?);");
            enunciado.setString(1, modeloUsuario.getUsuario());
            enunciado.setString(2, modeloUsuario.getApellido());
            enunciado.setString(3, modeloUsuario.getEmail());
            enunciado.setString(4, modeloUsuario.getContraseña());
            enunciado.setString(5, modeloUsuario.getNit());
            enunciado.setString(6, modeloUsuario.getDireccion());
            enunciado.setString(7, "cliente");
            enunciado.execute();
        } catch (Exception e) {
            System.out.println("Error al agregar...");
            e.printStackTrace();
        }
    }

    @FXML
    private void btnRegistrarAction() {
        if (txtUsuario.getText().isEmpty() || txtApellido.getText().isEmpty() || txtEmail.getText().isEmpty() 
            || txtContraseña.getText().isEmpty() || txtNit.getText().isEmpty() 
            || txtDireccion.getText().isEmpty()) {

            Alert alerta = new Alert(AlertType.WARNING);
            alerta.setTitle("Campos obligatorios");
            alerta.setHeaderText(null);
            alerta.setContentText("Por favor, llene todos los campos obligatorios.");
            alerta.showAndWait();

        } else {
            agregarUsuario();

            Alert alerta = new Alert(AlertType.INFORMATION);
            alerta.setTitle("Registro exitoso");
            alerta.setHeaderText(null);
            alerta.setContentText("Usuario registrado correctamente.");
            alerta.showAndWait();

            principal.IniciarSesion();
        }
    }

}
