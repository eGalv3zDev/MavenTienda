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
import org.adrianbreyner.db.Conexion;
import org.adrianbreyner.model.Productos;
import org.adrianbreyner.system.Main;

public class ProductosController implements Initializable {

    private Main principal;

    @FXML
    private Button btnAgregar, btnActualizar, btnEliminar, btnGuardar, btnCancelar, btnBuscar;
    @FXML
    private TextField txtID, txtBuscar, txtProducto, txtDescripcion, txtStock, txtPrecio;

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
        //Ajusta el ancho de las columnas de la tabla de Productos
        colIDProducto.prefWidthProperty().bind(tablaProductos.widthProperty().multiply(0.11));
        colProducto.prefWidthProperty().bind(tablaProductos.widthProperty().multiply(0.24));
        colDescripcion.prefWidthProperty().bind(tablaProductos.widthProperty().multiply(0.21));
        colStock.prefWidthProperty().bind(tablaProductos.widthProperty().multiply(0.14));
        colPrecio.prefWidthProperty().bind(tablaProductos.widthProperty().multiply(0.30));
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
    
     public void limpiarTextField() {
        txtID.clear();
        txtProducto.clear();
        txtDescripcion.clear();
        txtStock.clear();
        txtPrecio.clear();
    }

    private void cambiarEstado(EstadoFormulario estado) {
        tipoDeAccion = estado;
        boolean activo = (estado == EstadoFormulario.AGREGAR || estado == EstadoFormulario.ACTUALIZAR);
        txtProducto.setDisable(!activo);
        txtDescripcion.setDisable(!activo);
        txtStock.setDisable(!activo);
        txtPrecio.setDisable(!activo);

        tablaProductos.setDisable(activo);
        btnBuscar.setDisable(activo);
        txtBuscar.setDisable(activo);

        btnAgregar.setText(activo ? "Guardar" : "Agregar");
        btnEliminar.setText(activo ? "Cancelar" : "Eliminar");
        btnActualizar.setDisable(activo);
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
