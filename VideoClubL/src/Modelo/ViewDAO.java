/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dani
 */
public class ViewDAO implements IView{
    private Statement statement;
    private Connection connection;
    private ResultSet resultSet;
    private String sql;
    private List<View> listaView;
    private View v;
    
    public ViewDAO() {
        this.connection = Conexion.getConnection();
    }

    @Override
    public List<View> obtenerView() {
        listaView = new ArrayList<>();
        v = null;
        sql = "select * from vistaAlquiler ";
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                String dniCliente = resultSet.getString("dniCliente");
                String nombre = resultSet.getString("nombre");
                String apellidos = resultSet.getString("apellidos");
                String codPelicula = resultSet.getString("codPelicula");
                String titulo = resultSet.getString("titulo");
                String fecha_alquilada = resultSet.getString("fecha_alquilada");
                String fecha_devolucion = resultSet.getString("fecha_devolucion");

                v = new View(dniCliente, nombre, apellidos, codPelicula, titulo, fecha_alquilada, fecha_devolucion);
                listaView.add(v);
            }
        } catch (SQLException ex) {
            System.out.println("Error en la sentencia SQL: Obtener : VIEW");
        }
        return listaView;
    }
}
