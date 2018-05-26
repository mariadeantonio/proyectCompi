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
public class ClienteDAO implements IClienteDAO {
    
    private Statement statement;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private String sql;
    private int rows;
    private List<Cliente> listaClientes;
    private Cliente c;

    public ClienteDAO() {
        this.connection = Conexion.getConnection();
    }
      
    @Override
    public List<Cliente> obtenerCliente() {
        listaClientes = new ArrayList<>();
        c = null;
        sql = "select * from cliente";
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                String dni = resultSet.getString("dni");
                String nombre = resultSet.getString("nombre");
                String apellidos = resultSet.getString("apellidos");
                String edad = resultSet.getString("edad");
                c = new Cliente(dni, nombre, apellidos, edad);
                listaClientes.add(c);
            }
        } catch (SQLException ex) {
            System.out.println("Error en la sentencia SQL: Obtener : Cliente");
        }
        return listaClientes;
    }
    
    @Override
    public boolean anadirCliente(Cliente c) {
        boolean exito = false;
        sql = "Insert into cliente values (?, ?, ?, ?)";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, c.getDni());
            preparedStatement.setString(2, c.getNombre());
            preparedStatement.setString(3, c.getApellidos());
            preparedStatement.setString(4, c.getEdad());
            rows = preparedStatement.executeUpdate();
            if( rows != 0 ){
               exito = true;
            }
        } catch (SQLException ex) {
            System.out.println("Error en la sentencia SQL: Añadir : Cliente");
        }
        return exito;
    }

    @Override
    public boolean borrarCliente(Cliente c) {
        boolean exito = false;
        sql = "Delete from cliente where dni = ?";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, c.getDni());
            rows = preparedStatement.executeUpdate();
            if ( rows != 0 )
                exito = true;
        } catch (SQLException ex) {
            System.out.println("Error en la sentencia SQL: Borrar : Cliente");
        }
        return exito;
    }
}
