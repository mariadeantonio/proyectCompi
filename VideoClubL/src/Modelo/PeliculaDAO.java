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

public class PeliculaDAO implements IPeliculaDAO {
   
    private Statement statement;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private String sql;
    private int rows;
    private List<Pelicula> listaPelis;
    private Pelicula p;

    public PeliculaDAO() {
        this.connection = Conexion.getConnection();
        
    }
    
    @Override
    public List<Pelicula> obtenerPeli() {
        listaPelis = new ArrayList<>();
        p = null;
        sql = "select * from pelicula";
        
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                String codigo = resultSet.getString("codigo");
                String titulo = resultSet.getString("titulo");
                String director = resultSet.getString("director");
                String anio = resultSet.getString("anio");
                String genero = resultSet.getString("genero");
                p = new Pelicula(codigo, titulo, director, anio, genero);
                listaPelis.add(p);
            }
        } catch (SQLException ex) {
            System.out.println("Error en la sentencia SQL: Obtener : Pelicula");
        }
        return listaPelis;
    }
        
    @Override
    public boolean anadirPeli(Pelicula p) {
        boolean exito = false;
        
        sql = "insert into pelicula values (?, ?, ?, ?, ?)";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, p.getCodigo());
            preparedStatement.setString(2, p.getTitulo());
            preparedStatement.setString(3, p.getDirector());
            preparedStatement.setString(4, p.getAnio());
            preparedStatement.setString(5, p.getGenero());
            rows = preparedStatement.executeUpdate();
            if( rows != 0 ){
               exito = true;
            }
        } catch (SQLException ex) {
            System.out.println("Error en la sentencia SQL: Añadir : Pelicula");
        }
        return exito;
    }

    @Override
    public boolean borrarPeli(Pelicula p) {
        boolean exito = false;
        sql = "Delete from pelicula where codigo = ?";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, p.getCodigo());
            rows = preparedStatement.executeUpdate();
            if ( rows != 0 )
                exito = true;
        } catch (SQLException ex) {
            System.out.println("Error en la sentencia SQL: Borrar : Pelicula");
        }
        
        return exito;
    }
}
