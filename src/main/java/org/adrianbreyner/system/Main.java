package org.adrianbreyner.system;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import org.adrianbreyner.controller.PaginaInicioController;
import org.adrianbreyner.controller.RegistroController;

import java.net.URL; 
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.scene.Parent;

import javafx.scene.Scene;
import org.adrianbreyner.controller.CarritoController;
import org.adrianbreyner.controller.Inicio;
import org.adrianbreyner.controller.PaginaPrincipalController;
import org.adrianbreyner.controller.ProductoController;
import org.adrianbreyner.controller.TiendaProductosController;
import org.adrianbreyner.db.Conexion;

public class Main extends Application{
    private static String URL = "/"; 
    private Stage escenarioPrincipal;
    private Scene escena;
    
    private static int idUsuarioActual;
    
    public static void setIdUsuarioActual(int id) {
        idUsuarioActual = id;
    }

    public static int getIdUsuarioActual() {
        return idUsuarioActual;
    }
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage escenario) throws Exception {
        crearAdmin();
        this.escenarioPrincipal = escenario;
        PaginaPrincipal();
        escenario.show();
    }
    
    public static void crearAdmin(){
    try {
        PreparedStatement ps = Conexion.getInstancia().getConexion().prepareStatement(
            "select count(*) from Usuarios where usuario = 'admin'"
        );
        ResultSet resu = ps.executeQuery();
        if (resu.next() && resu.getInt(1) == 0) {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall("call sp_agregarUsuario(?,?,?,?,?,?,?);");
            enunciado.setString(1, "admin");
            enunciado.setString(2, "diaz");
            enunciado.setString(3, "admin@gmail.com");
            enunciado.setString(4, "admin123");
            enunciado.setString(5, "497871-4");
            enunciado.setString(6, "Sede Central");
            enunciado.setString(7, "administrador");
            enunciado.executeUpdate();
            System.out.println("Cuenta admin creada por defecto.");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
    public void PaginaInicio(){
        try {
            PaginaInicioController pi = (PaginaInicioController) cambiarEscena("view/PaginaInicio.fxml", 1280, 720);
            pi.setPrincipal(this);
        } catch (Exception ex) {
            System.out.println("Error al cargar la Página de Inicio.");
            ex.printStackTrace();
        }
    }
    
    public void PaginaPrincipal(){
        try {
            PaginaPrincipalController pp = (PaginaPrincipalController) cambiarEscena("view/PaginaPrincipal.fxml", 1280, 720);
            pp.setPrincipal(this);
        } catch (Exception ex) {
            System.out.println("Error al cargar la Página de Inicio.");
            ex.printStackTrace();
        }
    }
    
    public void Registro(){
        try {
            RegistroController rc = (RegistroController) cambiarEscena("view/Registro.fxml", 1280, 720);
            rc.setPrincipal(this);
        } catch (Exception ex) {
            System.out.println("Error al cargar la Página de Registro.");
            ex.printStackTrace();
        }
    }
    
    public void Productos(){
        try {
            ProductoController rc = (ProductoController) cambiarEscena("view/InicioView.fxml", 1280, 720);
            rc.setPrincipal(this);
        } catch (Exception ex) {
            System.out.println("Error al cargar la Página de Productos.");
            ex.printStackTrace();
        }
    }
    
    public void Tienda(){
        try {
            TiendaProductosController tp = (TiendaProductosController) cambiarEscena("view/TiendaProductos.fxml", 1280, 720);
            tp.setPrincipal(this);
            tp.setIdUsuario(idUsuarioActual);
        } catch (Exception ex) {
            System.out.println("Error al cargar la Página de Tienda.");
            ex.printStackTrace();
        }
    }
    
    public void Carrito(){
        try {
            CarritoController cc = (CarritoController) cambiarEscena("view/Carrito.fxml", 1280, 720);
            cc.setPrincipal(this);
        } catch (Exception ex) {
            System.out.println("Error al cargar la Página de Carrito.");
            ex.printStackTrace();
        }
    }
    
    public void IniciarSesion(){
        try {
            Inicio in = (Inicio) cambiarEscena("view/IniciarSesion.fxml", 1280, 720);
            in.setPrincipal(this);
        } catch (Exception ex) {
            System.out.println("Error al cargar la Página de Iniciar Sesion.");
            ex.printStackTrace();
        }
    }
    
    public Initializable cambiarEscena(String fxml, double ancho, double alto) throws Exception{
        Initializable interfazCargada = null;
        
        FXMLLoader cargadorFXML = new FXMLLoader();
        String fullFxmlPath = URL + fxml; 
        URL fxmlLocation = getClass().getResource(fullFxmlPath); 
                System.out.println("DEBUG: Intentando cargar FXML desde la URL: " + fullFxmlPath);
        
        if (fxmlLocation == null) {
            System.err.println("ERROR: getResource retornó null para la ruta: " + fullFxmlPath);
            throw new Exception("No se pudo encontrar el archivo FXML en la URL: " + fullFxmlPath);
        } else {
            System.out.println("DEBUG: URL para FXML obtenida exitosamente: " + fxmlLocation);
        }

        cargadorFXML.setLocation(fxmlLocation);
        Parent root = cargadorFXML.load();
        
        escena = new Scene(root, ancho, alto); 
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        
        interfazCargada = cargadorFXML.getController();
                
        return interfazCargada;
    }
}


