/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 *
 * @author Dani
 */
public class View {
    
    private String dniCliente;
    private String nombre;
    private String apellidos;
    private String codPelicula;
    private String titulo;
    private String fecha_alquilada;
    private String fecha_devolucion;

    public View(String dniCliente, String nombre, String apellidos, String codPelicula, String titulo, String fecha_alquilada, String fecha_devolucion) {
        this.dniCliente = dniCliente;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.codPelicula = codPelicula;
        this.titulo = titulo;
        this.fecha_alquilada = fecha_alquilada;
        this.fecha_devolucion = fecha_devolucion;
    }
    
    public String getDniCliente() {
        return dniCliente;
    }

    public void setDniCliente(String dniCliente) {
        this.dniCliente = dniCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCodPelicula() {
        return codPelicula;
    }

    public void setCodPelicula(String codPelicula) {
        this.codPelicula = codPelicula;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFecha_alquilada() {
        return fecha_alquilada;
    }

    public void setFecha_alquilada(String fecha_alquilada) {
        this.fecha_alquilada = fecha_alquilada;
    }

    public String getFecha_devolucion() {
        return fecha_devolucion;
    }

    public void setFecha_devolucion(String fecha_devolucion) {
        this.fecha_devolucion = fecha_devolucion;
    }

    @Override
    public String toString() {
        return dniCliente + ", " + nombre + ", " + apellidos + ", " + codPelicula + ", " + titulo + ", " 
                + fecha_alquilada + ", " + fecha_devolucion;
    } 
}
