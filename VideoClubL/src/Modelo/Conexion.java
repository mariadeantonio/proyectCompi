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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    
    public static Connection connection;
    
    public Conexion(){
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            System.out.println("JDBC no encontrado");
            
        }
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:BD/videoclub.bd");
        } catch (SQLException ex) {
            System.out.println("Problema en la conexion a la base de datos.");
            
        }
    }
    
    public static Connection getConnection(){
        if ( connection == null)
            new Conexion();
        return connection;
    }
}
