package org.adrianbreyner.model;

public class Productos {
    private int idProducto;
    private String producto;
    private String descripcion;
    private int stock;
    private double precio;
 
    public Productos() {
    }
 
    public Productos(int idProducto, String producto, String descripcion, int stock, double precio) {
        this.idProducto = idProducto;
        this.producto = producto;
        this.descripcion = descripcion;
        this.stock = stock;
        this.precio = precio;
    }
 
    public int getIdProducto() {
        return idProducto;
    }
 
    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }
 
    public String getProducto() {
        return producto;
    }
 
    public void setProducto(String producto) {
        this.producto = producto;
    }
 
    public String getDescripcion() {
        return descripcion;
    }
 
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
 
    public int getStock() {
        return stock;
    }
 
    public void setStock(int stock) {
        this.stock = stock;
    }
 
    public double getPrecio() {
        return precio;
    }
 
    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
