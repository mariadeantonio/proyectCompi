/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Dani
 */
public class AlquilerDAO implements IAlquiler {
    
    private Statement statement;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private String sql;
    private int rows;
    private List<Alquiler> listaAlquiler;
    private Alquiler a;

    public AlquilerDAO(){
        this.connection = Conexion.getConnection();
    }
   
    @Override
    public List<Alquiler> obtenerAlquiler() {
       listaAlquiler = new ArrayList<>();
       a = null;
        sql = "select * from alquila ";
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                String fecha_alquilada = resultSet.getString("fecha_alquilada");
                String fecha_devolucion = resultSet.getString("fecha_devolucion");
                String dniCliente = resultSet.getString("dniCliente");
                String codPelicula = resultSet.getString("codPelicula");
                a = new Alquiler(fecha_alquilada, fecha_devolucion, dniCliente, codPelicula);
                listaAlquiler.add(a);
            }
        } catch (SQLException ex) {
            System.out.println("Error en la sentencia SQL: Obtener : Alquiler");
        }
        return listaAlquiler;
    }

    @Override
    public boolean anadirAlquiler(Alquiler a) {
        boolean exito = false;
        sql = "insert into alquila values (?, ?, ?, ?)";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, a.getFecha_alquilada());
            preparedStatement.setString(2, a.getFecha_devolucion());
            preparedStatement.setString(3, a.getDniCliente());
            preparedStatement.setString(4, a.getCodPelicula());            
            rows = preparedStatement.executeUpdate();
            if( rows != 0 ){
               exito = true;
            }
        } catch (SQLException ex) {
            System.out.println("Error en la sentencia SQL: Añadir : Alquiler");
        }
        return exito;
    }

    @Override
    public boolean borrarAlquiler(Alquiler a) {
        boolean exito = false;
        sql = "Delete from alquila where dniCliente = ? and codPelicula = ?";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, a.getDniCliente());
            preparedStatement.setString(2, a.getCodPelicula());
            rows = preparedStatement.executeUpdate();
            if ( rows != 0 )
                exito = true;
        } catch (SQLException ex) {
            System.out.println("Error en la sentencia SQL: Borrar : Alquiler");
        }
        
        return exito;
    }
}
