package org.adrianbreyner.controller;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.adrianbreyner.db.Conexion;
import org.adrianbreyner.model.Productos;
import org.adrianbreyner.system.Main;

public class TiendaProductosController implements Initializable {
    private int idUsuario;
    private Main principal;
    @FXML
    private TextField txtID, txtBuscar, txtProducto, txtDescripcion, txtStock, txtPrecio, txtCantidad;

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    public void setPrincipal(Main principal) {
        this.principal = principal;
    }

    public Main getPrincipal() {
        return principal;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumna();
        cargarTablaProductos();
        tablaProductos.setOnMouseClicked(eventHandler -> cargarProductosEnTextField());
        //Ajusta el ancho de las columnas de la tabla de Clientes
        colIDProducto.prefWidthProperty().bind(tablaProductos.widthProperty().multiply(0.08));
        colProducto.prefWidthProperty().bind(tablaProductos.widthProperty().multiply(0.20));
        colDescripcion.prefWidthProperty().bind(tablaProductos.widthProperty().multiply(0.18));
        colStock.prefWidthProperty().bind(tablaProductos.widthProperty().multiply(0.12));
        colPrecio.prefWidthProperty().bind(tablaProductos.widthProperty().multiply(0.22));
    }

    @FXML
    private TableView<Productos> tablaProductos;

    private Productos modeloProductos;

    private enum EstadoFormulario {
        AGREGAR, ELIMINAR, ACTUALIZAR, NINGUNA
    };
    EstadoFormulario tipoDeAccion = EstadoFormulario.NINGUNA;

    @FXML
    private TableColumn colIDProducto, colProducto, colDescripcion, colStock, colPrecio;

    private ObservableList<Productos> listaProductos;

    public void configurarColumna() {
        colIDProducto.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("idProducto"));
        colProducto.setCellValueFactory(new PropertyValueFactory<Productos, String>("producto"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Productos, String>("descripcion"));
        colStock.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("stock"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precio"));
    }

    public void cargarTablaProductos() {
        listaProductos = FXCollections.observableArrayList(listarProductos());
        tablaProductos.setItems(listaProductos);
        tablaProductos.getSelectionModel().selectFirst();
    }

    public void cargarProductosEnTextField() {
        Productos ProductoSeleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        txtID.setText(String.valueOf(ProductoSeleccionado.getIdProducto()));
        txtProducto.setText(ProductoSeleccionado.getProducto());
        txtDescripcion.setText(ProductoSeleccionado.getDescripcion());
        txtStock.setText(String.valueOf(ProductoSeleccionado.getStock()));
        txtPrecio.setText(String.valueOf(ProductoSeleccionado.getPrecio()));
    }

    public ArrayList<Productos> listarProductos() {
        ArrayList<Productos> productos = new ArrayList();

        try {
            Connection conexion = (Connection) Conexion.getInstancia().getConexion();
            String sql = "call sp_listarProductos()";
            CallableStatement enunciado = conexion.prepareCall(sql);
            ResultSet resultado = enunciado.executeQuery();
            while (resultado.next()) {
                productos.add(new Productos(resultado.getInt(1),
                        resultado.getString(2),
                        resultado.getString(3),
                        resultado.getInt(4),
                        resultado.getDouble(5)));
            }
        } catch (SQLException ex) {
            System.out.println("Error al cargar MySQL, los Productos" + ex.getSQLState());
            ex.printStackTrace();
        }
        return productos;
    }

    public Productos obtenerModeloProductos() {
        int idProducto = 0;
        int stock = 0;
        double precio = 0.0;

        try {
            idProducto = Integer.parseInt(txtID.getText().trim()); 
        } catch (Exception e) {
            System.out.println("");
        }
        
        try {
            stock = Integer.parseInt(txtStock.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Stock inválido.");
        }

        try {
            precio = Double.parseDouble(txtPrecio.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Precio inválido.");
        }

        String producto = txtProducto.getText();
        String descripcion = txtDescripcion.getText();

        return new Productos(idProducto, producto, descripcion, stock, precio);
    }
    
    public void agregarProductoAlCarrito(int idProducto, int cantidad) {
    try {
        Connection conn = Conexion.getInstancia().getConexion();
        CallableStatement cs = conn.prepareCall("call sp_agregarCarrito(?,?,?)");
        cs.setInt(1, idProducto);
        cs.setInt(2, cantidad);
        cs.setInt(3, idUsuario);
        cs.execute();
        JOptionPane.showMessageDialog(null, "Producto agregado al carrito.");
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error al agregar al carrito: " + e.getMessage());
    }
}
    
    @FXML
private void comprarProducto() {
    Productos productoSeleccionado = tablaProductos.getSelectionModel().getSelectedItem();
    if (productoSeleccionado != null) {
        int idProducto = productoSeleccionado.getIdProducto();
        int cantidad = Integer.parseInt(txtCantidad.getText());
        agregarProductoAlCarrito(idProducto, cantidad);
    } else {
        JOptionPane.showMessageDialog(null, "Selecciona un producto primero.");
    }
}
    
    @FXML
    private void PaginaInicio(){
        principal.PaginaInicio();
    }
    
    @FXML
    private void Carrito(){
        principal.Carrito();
    }

    public void limpiarTextField() {
        txtID.clear();
        txtProducto.clear();
        txtDescripcion.clear();
        txtStock.clear();
        txtPrecio.clear();
    }

    @FXML
    private void buscarProducto() {
        ArrayList<Productos> resultadoBusqueda = new ArrayList<>();
        String nombre = txtBuscar.getText();
        for (Productos producto : listaProductos) {
            if (producto.getProducto().toLowerCase().contains(nombre.toLowerCase())) {
                resultadoBusqueda.add(producto);
            }
        }
        tablaProductos.setItems(FXCollections.observableArrayList(resultadoBusqueda));
        if (!resultadoBusqueda.isEmpty()) {
            tablaProductos.getSelectionModel().selectFirst();
        }
    }

}
