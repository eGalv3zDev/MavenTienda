/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
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
import org.adrianbreyner.model.Carrito;
import org.adrianbreyner.model.TotalGeneral;
import org.adrianbreyner.system.Main;

public class CarritoController implements Initializable {

    private Main principal;

    @FXML
    private Button btnActualizar, btnEliminar, btnBuscar;
    @FXML
    private TextField txtID, txtId, txtBuscar, txtCantidad, txtTotal, txtPrecio;
    @FXML
    private TableView<Carrito> tablaCarrito;
    private Carrito modeloCarritos;

    @FXML
    private TableView<TotalGeneral> tablaTotalGeneral;

    @FXML
    private TableColumn<TotalGeneral, Double> colTotalGeneral;

    private enum EstadoFormulario {
        AGREGAR, ELIMINAR, ACTUALIZAR, NINGUNA
    };
    EstadoFormulario tipoDeAccion = EstadoFormulario.NINGUNA;

    @FXML
    private TableColumn colIdCarrito, colIdProducto, colCantidad, colPrecio, colTotal;

    private ObservableList<Carrito> listaCarrito;

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }

    public Main getPrincipal() {
        return principal;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumna();
        cargarTablaCarritos();
        tablaCarrito.setOnMouseClicked(eventHandler -> cargarProductosEnTextField());
        colIdCarrito.prefWidthProperty().bind(tablaCarrito.widthProperty().multiply(0.08));
        colIdProducto.prefWidthProperty().bind(tablaCarrito.widthProperty().multiply(0.08));
        colCantidad.prefWidthProperty().bind(tablaCarrito.widthProperty().multiply(0.20));
        colPrecio.prefWidthProperty().bind(tablaCarrito.widthProperty().multiply(0.18));
        colTotal.prefWidthProperty().bind(tablaCarrito.widthProperty().multiply(0.12));
        colTotalGeneral.setCellValueFactory(new PropertyValueFactory<>("totalGeneral"));

        cargarTotalGeneral(Main.getIdUsuarioActual());
    }

    public void configurarColumna() {
        colIdCarrito.setCellValueFactory(new PropertyValueFactory<Carrito, Integer>("idCarrito"));
        colIdProducto.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Carrito, Integer>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<Carrito, Double>("precioUnitario"));
        colTotal.setCellValueFactory(new PropertyValueFactory<Carrito, Double>("total"));
    }

    public void cargarTablaCarritos() {
        int idUsuarioActual = Main.getIdUsuarioActual();
        listaCarrito = FXCollections.observableArrayList(listarCarritos(idUsuarioActual));
        tablaCarrito.setItems(listaCarrito);
        tablaCarrito.getSelectionModel().selectFirst();
    }

    public void cargarProductosEnTextField() {
        Carrito CarritoSeleccionado = tablaCarrito.getSelectionModel().getSelectedItem();
        txtId.setText(String.valueOf(CarritoSeleccionado.getIdCarrito()));
        txtID.setText(String.valueOf(CarritoSeleccionado.getIdProducto()));
        txtCantidad.setText(String.valueOf(CarritoSeleccionado.getCantidad()));
        txtPrecio.setText(String.valueOf(CarritoSeleccionado.getPrecioUnitario()));
        txtTotal.setText(String.valueOf(CarritoSeleccionado.getTotal()));
    }

    public ArrayList<Carrito> listarCarritos(int idUsuario) {
        ArrayList<Carrito> carritos = new ArrayList();

        try {
            Connection conexion = (Connection) Conexion.getInstancia().getConexion();
            String sql = "call sp_listarCarritos(?)";
            CallableStatement enunciado = conexion.prepareCall(sql);
            enunciado.setInt(1, idUsuario);
            ResultSet resultado = enunciado.executeQuery();
            while (resultado.next()) {
                carritos.add(new Carrito(resultado.getInt(1),
                        resultado.getInt(2),
                        resultado.getInt(3),
                        resultado.getDouble(4),
                        resultado.getDouble(5)));
            }
        } catch (SQLException ex) {
            System.out.println("Error al cargar MySQL, los Carritos" + ex.getSQLState());
            ex.printStackTrace();
        }
        return carritos;
    }

    public Carrito obtenerModeloCarrito() {
        int idCarrito = 0;
        int idProducto = 0;
        int cantidad = 0;
        double precio = 0.0;
        double total = 0.0;

        try {
            idCarrito = Integer.parseInt(txtId.getText().trim());
        } catch (Exception e) {
            System.out.println("");
        }

        try {
            idProducto = Integer.parseInt(txtID.getText().trim());
        } catch (Exception e) {
            System.out.println("");
        }

        try {
            cantidad = Integer.parseInt(txtCantidad.getText().trim());
        } catch (Exception e) {
            System.out.println("");
        }

        try {
            precio = Double.parseDouble(txtPrecio.getText().trim());
        } catch (Exception e) {
            System.out.println("");
        }

        try {
            total = Double.parseDouble(txtPrecio.getText().trim());
        } catch (Exception e) {
            System.out.println("");
        }

        return new Carrito(idCarrito, idProducto, cantidad, precio, precio);
    }

    public void cargarTotalGeneral(int idUsuario) {
        ObservableList<TotalGeneral> lista = FXCollections.observableArrayList();

        try {
            CallableStatement cs = Conexion.getInstancia().getConexion().prepareCall("call sp_totalGeneralCarrito(?);");
            cs.setInt(1, idUsuario);
            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                double total = rs.getDouble("totalGeneral");
                lista.add(new TotalGeneral(total));
            } else {
                lista.add(new TotalGeneral(0.0));
            }
            tablaTotalGeneral.setItems(lista);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarCarrito() {
        modeloCarritos = obtenerModeloCarrito();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall("call sp_eliminarCarrito(?);");
            enunciado.setInt(1, modeloCarritos.getIdCarrito());
            enunciado.execute();
            int idUsuario = Main.getIdUsuarioActual();
            cargarTablaCarritos();
            cargarTotalGeneral(idUsuario);
        } catch (SQLException e) {
            System.out.println("Error al Eliminar Carrito");
            e.printStackTrace();
        }
    }

    public void limpiarTextField() {
        txtId.clear();
        txtID.clear();
        txtCantidad.clear();
        txtPrecio.clear();
        txtTotal.clear();
    }

    @FXML
    private void PaginaInicio() {
        principal.PaginaInicio();
    }

    @FXML
    private void Tienda() {
        principal.Tienda();
    }

    @FXML
    private void eliminarCarro() {
        eliminarCarrito();
        int idUsuario = Main.getIdUsuarioActual();
        cargarTotalGeneral(idUsuario);
    }

    @FXML
    private void buscarCarrito() {
        ArrayList<Carrito> resultadoBusqueda = new ArrayList<>();
        String idFactura = txtBuscar.getText();
        int idFact = Integer.parseInt(idFactura);
        for (Carrito fa : listaCarrito) {
            if (fa.getIdCarrito() == idFact) {
                resultadoBusqueda.add(fa);
            }
        }
        tablaCarrito.setItems(FXCollections.observableArrayList(resultadoBusqueda));
        if (!resultadoBusqueda.isEmpty()) {
            tablaCarrito.getSelectionModel().selectFirst();
        }
    }

}
