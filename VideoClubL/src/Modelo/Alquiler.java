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
public class Alquiler {
    
    private String fecha_alquilada;
    private String fecha_devolucion;
    private String dniCliente;
    private String codPelicula;

    public Alquiler(String fecha_alquilada, String fecha_devolucion, String dniCliente, String codPelicula) {
        this.fecha_alquilada = fecha_alquilada;
        this.fecha_devolucion = fecha_devolucion;
        this.dniCliente = dniCliente;
        this.codPelicula = codPelicula;
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

    public String getDniCliente() {
        return dniCliente;
    }

    public void setDniCliente(String dniCliente) {
        this.dniCliente = dniCliente;
    }

    public String getCodPelicula() {
        return codPelicula;
    }

    public void setCodPelicula(String codPelicula) {
        this.codPelicula = codPelicula;
    }

    @Override
    public String toString() {
        return fecha_alquilada + ", " + fecha_devolucion + ", " + dniCliente + ", " + codPelicula;
    }
}
