package org.adrianbreyner.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.adrianbreyner.system.Main;

/**
 *
 * @author brey
 */
public class PaginaInicioController implements Initializable {
    private Main principal;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    
    public void setPrincipal(Main principal) {
        this.principal = principal;
    }
    
    public Main getPrincipal() {
        return principal;
    }

    @FXML
    private void CerrarSesion() {
        principal.PaginaPrincipal();
    }
    
    @FXML
    private void Tienda() {
        principal.Tienda();
    }
    
    @FXML
    private void Carrito() {
        principal.Carrito();
    }

}
