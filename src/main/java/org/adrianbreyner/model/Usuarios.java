package org.adrianbreyner.model;

public class Usuarios {
    private int idUsuario;
    private String usuario;
    private String apellido;
    private String email;
    private String contraseña;
    private String nit;
    private String direccion;

    public Usuarios() {
    }

    public Usuarios(int idUsuario, String usuario, String apellido, String email, String contraseña, String nit, String direccion) {
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.apellido = apellido;
        this.email = email;
        this.contraseña = contraseña;
        this.nit = nit;
        this.direccion = direccion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
}
