package org.adrianbreyner.db;
import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 *
 * @author Informatica
 */

public class Conexion {
    private static Conexion instancia;
    private Connection conexion;
    private static final String URL ="jdbc:mysql://127.0.0.1:3306/ventaProductos?user=false";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    
    public Conexion(){
    }
    
    public void conectar(){
        try {
            Class.forName(DRIVER).newInstance();
            conexion = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexion establecida con exito!");
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException e) {
            System.out.println("Error al conectar..."); 
        }
    }
    
    public static Conexion getInstancia(){
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }
    
   public Connection getConexion(){
       try {
           if (conexion == null || conexion.isClosed()) {
               conectar();
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
       return conexion;
   }
}
