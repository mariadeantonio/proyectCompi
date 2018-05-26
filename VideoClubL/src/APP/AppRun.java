/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APP;

import Controlador.Controlador;
import Modelo.AlquilerDAO;
import Modelo.ClienteDAO;
import Modelo.PeliculaDAO;
import Modelo.ViewDAO;
import Vista.Vista;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dani
 */
public class AppRun {
    
    public static void main(String args[]) {
       
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         *  
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Vista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Vista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Vista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Vista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Vista vista = new Vista();
                    
                    ClienteDAO cliente = new ClienteDAO();
                    PeliculaDAO pelicula = new PeliculaDAO();
                    AlquilerDAO alquiler = new AlquilerDAO();
                    ViewDAO view = new ViewDAO();
                    
                    Controlador c = new Controlador(vista, cliente, pelicula, alquiler, view);
                    c.fechaActual();
                    c.tabla();
                    c.rellenaClienteOpcional();
                    c.rellenaPeliculaOpcional();
                    c.actualizarListaAlquiler();
                    c.desactivarCamposAlquiler();
                    c.creditosActivados();
                    
                    
                } catch (SQLException ex) {
                    Logger.getLogger(Vista.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}
