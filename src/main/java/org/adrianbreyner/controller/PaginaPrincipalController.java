package org.adrianbreyner.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.adrianbreyner.system.Main;

public class PaginaPrincipalController implements Initializable {
    private Main principal;
    
    public void setPrincipal(Main principal) {
        this.principal = principal;
    }
    
    public Main getPrincipal() {
        return principal;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }   
    
    @FXML
    private void IniciarSesion(){
        principal.IniciarSesion();
    }
    
    @FXML
    private void Registrarse(){
        principal.Registro();
    }
    
    @FXML
    private void PaginaPrincipal(){
        principal.PaginaPrincipal();
    }
    
}
