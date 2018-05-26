/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

//Importaciones necesarias
import Modelo.Alquiler;
import Modelo.AlquilerDAO;
import Modelo.Cliente;
import Modelo.ClienteDAO;
import Modelo.Pelicula;
import Modelo.PeliculaDAO;
import Modelo.View;
import Modelo.ViewDAO;
import Vista.Vista;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;

/**
 *
 * @author Dani
 */
public class Controlador implements ActionListener{
    //Llamo a la vista
    private Vista vista;
    
    //Llamo a los DAO
    private ClienteDAO cliente;
    private PeliculaDAO pelicula;
    private AlquilerDAO alquiler;
    private ViewDAO view;
    
    //Listas que contienen la informacion de la BD
        //Se encuentran en los DAO
    private List<Cliente> listaClientes;
    private List<Pelicula> listaPelis;
    private List<Alquiler> listaAlquiler;
    private List<View> listaView;
    
    //Variable que conteniene informacion del usuario //jTextArea
    String camposVacios = "";
    
    //Variables que Comprueban si existen Clientes,Peliculas y Alquileres en la BD
    int controlExistenciaCliente = 0, controlExistenciaPelicula = 0, controlExistenciaAlquilerBorrar = 0;
    
    //Variables que Verifican expresiones regulares
        //DNI: (12345678L)  Nombre: (Francisco)  Apellidos: (Moreno García)  Edad: (21)
        //Codigo: (12345678) Titulo: (Las Vahías de Nichigan) Director: (José Medina Ruíz) Año: (2014)  Género: (Drama)
        //Fecha alquilada: (Fecha Actual)  Fecha devolución:  (00/00/0000)  DniCliente: (Auto)  CodPelicula: (Auto)
    boolean clienteVerificar = false, peliculaVerificar = false, alquilerVerificar = false;
    
    //Constructor del controlador, tiene estos parámetro para su correcta comunicación
    public Controlador(Vista vista, ClienteDAO cliente, PeliculaDAO pelicula, AlquilerDAO alquiler, ViewDAO view) {
        this.vista = vista;
        this.cliente = cliente;
        this.pelicula = pelicula;
        this.alquiler = alquiler;
        this.view = view;
        
        //Declaro las acciones de escucha
        actionListener(this);
    }
    
    //Creamos los eventos a los botones que se aplican a la Vista
    public void actionPerformed(ActionEvent e){
        //Accion del boton anadir //Formulario
        if(e.getActionCommand().equals("Añadir")){
           compruebaClienteBD(controlExistenciaCliente);
           compruebaPeliculaBD(controlExistenciaPelicula);
           compruebaAlquilerBorrarBD(controlExistenciaAlquilerBorrar);
           comprobarCamposObligatoriosAnadir(camposVacios); 
           
           verificarCliente(clienteVerificar);
           verificarPelicula(clienteVerificar);
           verificarAlquiler(alquilerVerificar);
           
           anadirCliente();
           anadirPelicula();
           anadirAlquiler();

        }
        //Accion del boton limpiar //Formulario
        if(e.getActionCommand().equals("Limpiar")){
            limpiarTexto();
        }
        //Accion del boton borrar //Formulario
        if(e.getActionCommand().equals("Borrar")){
           borrarCliente();
           borrarPelicula();
           borrarAlquiler();
        }
        //Accion del boton borrar //Alquiler
        if(e.getActionCommand().equals("Borrar ")){
            borrarCampoTabla();
        }  
        //Accion del boton volcar //Listas
        if(e.getActionCommand().equals("Volcar")){
            volcarAlquilerOpcional();
        }
        //Accion del boton activarAlquiler //Formulario
        if(e.getActionCommand().equals("Activar")){
            compruebaClienteBD(controlExistenciaCliente);
            compruebaPeliculaBD(controlExistenciaPelicula);
            compruebaAlquilerBorrarBD(controlExistenciaAlquilerBorrar);
            
            activarAlquiler();
        }
        //Accion del boton actualizarOpcional  //Listas
        if(e.getActionCommand().equals("Actualizar")){
            actualizarListaAlquiler();
        }
        //Accion al pulsar Activar creditos //Menu ABOUT
        if(e.getActionCommand().equals("ON Creditos")){
            creditosActivados();
        }
        //Accion al pulsar Desactivar creditos //Menu ABOUT
        if(e.getActionCommand().equals("OFF Creditos")){
            creditosDesactivados();
        } 
        //Accion del boton BorrarCliente //Listas
        if(e.getActionCommand().equals("Borrar Cliente")){
            borrarClienteLista();
        } 
        //Accion del boton BorrarPelicula //Listas
        if(e.getActionCommand().equals("Borrar Pelicula")){
            borrarPeliculaLista();
        } 
    }
    
    //Método que escucha las acciones a los eventos de los botones //Vista
    public void actionListener(ActionListener escuchar){
        //Escucha del boton anadir
        vista.getjButtonANADIR().addActionListener(escuchar);
        //Escucha del boton limpiar
        vista.getjButtonLIMPIAR().addActionListener(escuchar);
        //Escucha del boton borrar
        vista.getjButtonBORRAR().addActionListener(escuchar);
        //Escucha del boton volcar
        vista.getjButtonVolcarAlquileres().addActionListener(escuchar);
        //Escucha del boton activarAlquiler
        vista.getjButtonACTIVARALQUILER().addActionListener(escuchar);
        //Escucha del boton actualizar
        vista.getjButtonActualizarL().addActionListener(escuchar);
        //Escucha del listener de los creditos
        vista.getjMenuItemActivarCreditos().addActionListener(escuchar);
        vista.getjMenuItem1DesactivarCreditos().addActionListener(escuchar);
        //Escucha del boton borrar de la Tabla
        vista.getjButtonBorrarT().addActionListener(escuchar);
        //Ecucha de los botones borrar de las listas
        vista.getjButtonBorrarClienteLista().addActionListener(escuchar);
        vista.getjButtonBorrarPeliculaLista().addActionListener(escuchar);
    }
    
    //Método que verifica las expresiones regulares de CLIENTE
        //Recojo los falses y trues en variables que comprueba si todo esta bien,
        //Si es asi creara el cliente
    public boolean verificarCliente(boolean clienteVerificar){
    boolean dniVerificar, nombreVerificar, apellidosVerificar = false, edadVerificar;
    String dni = vista.getjTextFieldDNI().getText();
    
        if(!dni.isEmpty()){
        String dniRegexp = "\\d{8}[A-Z]";
        dniVerificar = Pattern.matches(dniRegexp, dni);

        String nombreRegexp = "[a-zA-ZñÑáéíóúÁÉÍÓÚ]{3,15}";
        String nombre = vista.getjTextFieldNOMBRE().getText();
        nombreVerificar = Pattern.matches(nombreRegexp, nombre);

        String apellidos = vista.getjTextFieldAPELLIDOS().getText();
        String apellidosRegexp = "[a-zA-ZñÑáéíóúÁÉÍÓÚ]{3,30}";
        String [] cadenas = apellidos.split("[ ]");
        
            for(int i = 0; i<cadenas.length; i++){
            apellidosVerificar = Pattern.matches(apellidosRegexp, cadenas[i]);
                if(Pattern.matches(apellidosRegexp, cadenas[i]) == false){
                    apellidosVerificar = false;
                    break;
                }   
            }

        String edadRegexp =  "\\d{2}";
        String edad = vista.getjTextFieldEDAD().getText();
        edadVerificar = Pattern.matches(edadRegexp, edad);

            if((dniVerificar == true) && (nombreVerificar == true) && (apellidosVerificar == true) && (edadVerificar == true)){
                clienteVerificar = true;
            }   else {
                    clienteVerificar = false;
                }
        }
    return clienteVerificar;
    }
    
    public boolean verificarPelicula(boolean peliculaVerificar){
    boolean codigoVerificar, tituloVerificar = false, directorVerificar = false, anioVerificar, generoVerificar;
    String codigo = vista.getjTextFieldCODIGO().getText();

        if(!codigo.isEmpty()){
        String codigoRegexp = "[1-9]{8}";
        codigoVerificar = Pattern.matches(codigoRegexp, codigo);
        Pattern.matches(codigoRegexp, codigo);

        String titulo = vista.getjTextFieldTITULO().getText();
        String tituloRegexp = "[a-zA-ZñÑáéíóúÁÉÍÓÚ]{3,30}";
        String [] cadenas = titulo.split("[ ]");
        
            for(int i = 0; i<cadenas.length; i++){
            tituloVerificar = Pattern.matches(tituloRegexp, cadenas[i]);
                if(Pattern.matches(tituloRegexp, cadenas[i]) == false){
                    tituloVerificar = false;
                    break;
                }   
            }
            
        String director = vista.getjTextFieldDIRECTOR().getText();
        String directorRegexp = "[a-zA-ZñÑáéíóúÁÉÍÓÚ]{3,30}";
        String [] cadenas1 = director.split("[ ]");
        
            for(int i = 0; i<cadenas1.length; i++){
            directorVerificar = Pattern.matches(directorRegexp, cadenas1[i]);
                if(Pattern.matches(directorRegexp, cadenas1[i]) == false){
                    directorVerificar = false;
                    break;
                }   
            }

        String anioRegexp =  "\\d{4}";
        String anio = vista.getjTextFielANIO().getText();
        anioVerificar = Pattern.matches(anioRegexp, anio);

        String generoRegexp = "[a-zA-ZñÑáéíóúÁÉÍÓÚ]{3,15}";
        String genero = vista.getjTextFieldGENERO().getText();
        generoVerificar = Pattern.matches(generoRegexp, genero);

            if((codigoVerificar == true) && (tituloVerificar == true) && (directorVerificar == true) && (anioVerificar == true) 
                && (generoVerificar == true)){
                peliculaVerificar = true;
            }   else{
                    peliculaVerificar = false;
                }
        }
        return peliculaVerificar;
    }
    
    public boolean verificarAlquiler(boolean alquilerVerificar){
    boolean fecha_devolucionVerificar;
    String fecha_devolucion = vista.getjTextFieldFECHADEVOLUCION().getText();
    
        if(!fecha_devolucion.isEmpty()){
        String fecha_devolucionRegexp = "\\d{2}/\\d{2}/\\d{4}";
        fecha_devolucionVerificar = Pattern.matches(fecha_devolucionRegexp, fecha_devolucion);

            if((fecha_devolucionVerificar == true)){
                alquilerVerificar = true;
            }   else {
                    alquilerVerificar = false;
                }
        }
        return alquilerVerificar;
    }

    //Método que añade el Cliente a la base de datos
    public void anadirCliente(){ 
    String dni = vista.getjTextFieldDNI().getText();
    String nombre = vista.getjTextFieldNOMBRE().getText();
    String apellidos = vista.getjTextFieldAPELLIDOS().getText();
    String edad = vista.getjTextFieldEDAD().getText();
    
    Cliente c = new Cliente(dni, nombre, apellidos, edad); 
    
        if(verificarCliente(clienteVerificar) == true){
            if(!dni.isEmpty()){         
                listaClientes = new ClienteDAO().obtenerCliente();
                cliente.anadirCliente(c);  
                vista.getL1().removeAllElements();
                rellenaClienteOpcional();  
            }
        }
    }  
     
    public void borrarCliente(){
    listaClientes = new ClienteDAO().obtenerCliente();
    String dni = vista.getjTextFieldDNI().getText();
    
        for (Cliente cl : listaClientes) {            
            if(dni.equals(cl.getDni())){
               int respuesta = JOptionPane.showConfirmDialog(null, "¿ESTÁ SEGURO DE BORRAR EL CLIENTE?","Borrar", YES_NO_OPTION, QUESTION_MESSAGE);
                if(respuesta == YES_OPTION){ 
                  listaClientes.remove(cl);
                  cliente.borrarCliente(cl);
                  vista.getjTextArea1CAJA().setText("Cliente borrado correctamente\n");
                  vista.getL1().removeAllElements();
                  rellenaClienteOpcional();  
                break;
                }
            }          
        }
    }  
               
    public void anadirPelicula(){
    listaPelis = new PeliculaDAO().obtenerPeli();
    String codigo = vista.getjTextFieldCODIGO().getText();
    String titulo = vista.getjTextFieldTITULO().getText();
    String director = vista.getjTextFieldDIRECTOR().getText();
    String anio = vista.getjTextFielANIO().getText();
    String genero = vista.getjTextFieldGENERO().getText();
        if(verificarPelicula(peliculaVerificar) == true){
            if(!codigo.isEmpty()){         
                Pelicula p = new Pelicula(codigo, titulo, director, anio, genero);
                pelicula.anadirPeli(p);
                listaPelis.removeAll(listaPelis);
                vista.getL2().removeAllElements();
                rellenaPeliculaOpcional();
            } 
        }
    }
    
    public void borrarPelicula(){
    listaPelis = new PeliculaDAO().obtenerPeli();
    String codigo = vista.getjTextFieldCODIGO().getText();
           
        for (Pelicula cl : listaPelis) {            
            if(codigo.equals(cl.getCodigo())){
               int respuesta = JOptionPane.showConfirmDialog(null, "¿ESTÁ SEGURO DE BORRAR LA PELICULA?","Borrar", YES_NO_OPTION, QUESTION_MESSAGE);
                if(respuesta == YES_OPTION){ 
                  listaPelis.remove(cl);
                  pelicula.borrarPeli(cl);
                  vista.getjTextArea1CAJA().setText("Pelicula borrada correctamente\n");
                  vista.getL2().removeAllElements();
                  rellenaPeliculaOpcional();
                break;
                }
            }          
        }
    }  
    
    public void anadirAlquiler(){
    listaAlquiler = new AlquilerDAO().obtenerAlquiler();
   
    String fecha_alquilada = vista.getjTextFieldFECHAALQUILADA().getText();
    String fecha_devolucion = vista.getjTextFieldFECHADEVOLUCION().getText();
    String dniCliente = vista.getjTextFieldDNICLIENTE().getText();
    String codPelicula = vista.getjTextFieldCODPELICULA().getText(); 

    Alquiler a = new Alquiler(fecha_alquilada, fecha_devolucion, dniCliente, codPelicula);
  
        compruebaClienteBD(controlExistenciaCliente);
        compruebaPeliculaBD(controlExistenciaPelicula);
        compruebaAlquilerBorrarBD(controlExistenciaAlquilerBorrar);
        
        if(verificarAlquiler(alquilerVerificar) == true){
            if(!dniCliente.isEmpty() && !codPelicula.isEmpty()){
                if((compruebaClienteBD(controlExistenciaCliente) == 1 && compruebaPeliculaBD(controlExistenciaPelicula) == 1)
                        && compruebaAlquilerBorrarBD(controlExistenciaAlquilerBorrar) == 0){
                    alquiler.anadirAlquiler(a);
                    listaAlquiler.removeAll(listaAlquiler);
                    listaView.removeAll(listaView);
                    vista.getjTextFieldFECHADEVOLUCION().setEnabled(false);
                    // Borra la tabla que habia por defecto
                    for (int i = 0; i < vista.getjTable1().getRowCount(); i++) {
                      vista.getTabla().removeRow(i);
                      i-=1;
                    }
                    //Carga la lista de la VISTA con los nuevos valores //Actualizados  
                    listaView = new ViewDAO().obtenerView();
                    View v;
                    for (int i = 0; i < listaView.size(); i++) { 
                        Object fila[] = new Object[7];
                        v = listaView.get(i);

                        fila[0] = v.getDniCliente();
                        fila[1] = v.getNombre();
                        fila[2] = v.getApellidos();
                        fila[3] = v.getCodPelicula();
                        fila[4] = v.getTitulo();
                        fila[5] = v.getFecha_alquilada();
                        fila[6] = v.getFecha_devolucion();

                        vista.getTabla().addRow(fila);
                    }            
                }
            }
        }
    }
    
    public void borrarAlquiler(){
    //Creo una nueva lista con los Alquileres ya actualizados
    listaAlquiler = new AlquilerDAO().obtenerAlquiler();
    String dniCliente = vista.getjTextFieldDNICLIENTE().getText();
    String codPelicula = vista.getjTextFieldCODPELICULA().getText();
    
        for (Alquiler cl : listaAlquiler) {            
            if((dniCliente.equals(cl.getDniCliente())) && (codPelicula.equals(cl.getCodPelicula()))){
                if(compruebaAlquilerBorrarBD(controlExistenciaAlquilerBorrar) == 1){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿ESTÁ SEGURO DE BORRAR EL ALQUILER?","Borrar", YES_NO_OPTION, QUESTION_MESSAGE);
                    if(respuesta == YES_OPTION){ 
                        alquiler.borrarAlquiler(cl);
                        listaAlquiler.removeAll(listaAlquiler);
                        vista.getjTextArea1CAJA().setText("Alquiler Borrado Correctamente");
                        //Cuando borro el Alquiler, borro la tabla y creo otra nueva para que se vea //Actualizada
                        for (int i = 0; i < vista.getjTable1().getRowCount(); i++) {
                            vista.getTabla().removeRow(i);
                             i-=1;
                        }
                        listaView = new ViewDAO().obtenerView();
                        View v;
                        for (int i = 0; i < listaView.size(); i++) { 
                            Object fila[] = new Object[7];
                            v = listaView.get(i);

                            fila[0] = v.getDniCliente();
                            fila[1] = v.getNombre();
                            fila[2] = v.getApellidos();
                            fila[3] = v.getCodPelicula();
                            fila[4] = v.getTitulo();
                            fila[5] = v.getFecha_alquilada();
                            fila[6] = v.getFecha_devolucion();

                            vista.getTabla().addRow(fila);
                        }            
                    break;         
                    }
                }
            } 
        }
    }
    
    //Método que saca la fecha del sistema
    public void fechaActual(){
      String fechaActual = "";
      String cero = "0";
      Calendar fecha = new GregorianCalendar();
      int anio = fecha.get(Calendar.YEAR);
      int mes = fecha.get(Calendar.MONTH);
      int dia = fecha.get(Calendar.DAY_OF_MONTH);

        if (dia < 10 || mes < 10){

            if (dia < 10 && mes > 10){
                fechaActual = cero + dia + "/" +(mes+1) + "/" + anio;   
            }

            if (mes < 10 && dia > 10){
                fechaActual = dia + "/" + cero +(mes+1) + "/" + anio;   
            }

            if (dia < 10 && mes < 10){
                fechaActual = cero + dia + "/" + cero +(mes+1) + "/" + anio;   
            }

        } else{
            fechaActual = dia + "/" +(mes+1) + "/" + anio;   
          }
     
      vista.getjTextFieldFECHAALQUILADA().setText(fechaActual);
      vista.getjTextFieldFECHAALQUILADA().setEnabled(false);
    }

    //Método que comprueba campos y muestra información en el jTextArea
    public String comprobarCamposObligatoriosAnadir(String camposVacios){
    String c = "", ca = "", p = "", pa = "", v = "", dc = "", dcc = "";
       
    String dni = vista.getjTextFieldDNI().getText();  
    String codigo = vista.getjTextFieldCODIGO().getText();
    String dniCliente = vista.getjTextFieldDNICLIENTE().getText();
    String codPelicula = vista.getjTextFieldCODPELICULA().getText();
          
        if(dni.isEmpty()){
                c = "Cliente ";
                v = "Campos obligatorios vacíos: ";
        }else if(verificarCliente(clienteVerificar) == true){ 
            ca = "Cliente añadido correctamente\n";
        }else{
            ca = "Campos Inválidos o Vacíos en CLIENTE\n";
        }
        if(compruebaClienteBD(controlExistenciaCliente) == 1){
            ca = "Cliente ya esta añadido!!\n";
        }

        if(codigo.isEmpty()){
                p = "Pelicula ";
                v = "Campos obligatorios vacíos: ";
        }else if (verificarPelicula(peliculaVerificar) == true){
            pa = "Pelicula añadida correctamente\n";
        }else{
            pa = "Campos Inválidos o Vacíos en PELICULA\n";
        }
        
        if(compruebaPeliculaBD(controlExistenciaPelicula) == 1){
            pa= "Pelicula ya esta añadida!!\n";
        }
        
        if (dniCliente.isEmpty() && codPelicula.isEmpty()){
                dc = "Alquiler ";
        }else if (verificarAlquiler(alquilerVerificar) == true){
            System.out.println(compruebaAlquilerBorrarBD(controlExistenciaAlquilerBorrar));
           if ((compruebaClienteBD(controlExistenciaCliente) == 1 && (compruebaPeliculaBD(controlExistenciaPelicula) == 1))
                   && (compruebaAlquilerBorrarBD(controlExistenciaAlquilerBorrar) == 0)) {
                dcc = "Alquiler añadido correctamente\n";
            }
            if (compruebaAlquilerBorrarBD(controlExistenciaAlquilerBorrar) == 1) {
                dcc = "Ya esta añadido este alquiler\n";
            }  
        }else{
            dcc = "Campos Inválidos o Vacíos en ALQUILER\n";
        }

        if ((!dni.isEmpty() && !codigo.isEmpty()) && (dniCliente.isEmpty() && codPelicula.isEmpty())){
                camposVacios = ca + pa + "" + v + c + p;
        }else {
                camposVacios = ca + pa + "" + v + c + p ; 
        }
        
        if((!dni.isEmpty() && !codigo.isEmpty()) && (!dniCliente.isEmpty() && !codPelicula.isEmpty())) {
                camposVacios =  ca + ""+ pa + ""+ dcc + "" + dc;
        }
        
        vista.getjTextArea1CAJA().setText(camposVacios);   
        return camposVacios;
    }
    
    //Metododo que comprueba si existe en alquiler en la base de datos
        //Si es así devuelve 1 si es falso devuelve 0
    public int compruebaAlquilerBorrarBD(int controlExistenciaAlquilerBorrar){
    listaAlquiler = new AlquilerDAO().obtenerAlquiler();
    String dniCliente = vista.getjTextFieldDNICLIENTE().getText();
    String codPelicula = vista.getjTextFieldCODPELICULA().getText();
    
     for (Alquiler cl : listaAlquiler) {   
        if((dniCliente.equals(cl.getDniCliente())) && (codPelicula.equals(cl.getCodPelicula()))){
            controlExistenciaAlquilerBorrar = 1;  
            break;
        }
        controlExistenciaAlquilerBorrar = 0;
     } 
     return controlExistenciaAlquilerBorrar;
    }

    //Metodo que comprueba si el cliente introducido en "Cliente: DNI" esta registrado en la base de datos
    public int compruebaClienteBD(int controlExistenciaCliente){
    listaClientes = new ClienteDAO().obtenerCliente();
    String dni = vista.getjTextFieldDNI().getText();
     for (Cliente cl : listaClientes) {   
            if((dni.equals(cl.getDni())) ){
                controlExistenciaCliente = 1;
                break;
            }
            controlExistenciaCliente = 0;
        }
     return controlExistenciaCliente;   
    }
    
    //Metodo que comprueba si la pelicula introducida en "Pelicula: CODIGO" esta registrado en la base de datos
    public int compruebaPeliculaBD(int controlExistenciaPelicula){
    listaPelis = new PeliculaDAO().obtenerPeli();
    String codigo = vista.getjTextFieldCODIGO().getText();
     for (Pelicula cl : listaPelis) {   
        if((codigo.equals(cl.getCodigo()))){
            controlExistenciaPelicula = 1;
            break;
        }
        controlExistenciaPelicula = 0;
     }
     return controlExistenciaPelicula;  
    }
          
    //Método que coge los campos Dni y Codigo 
        //Sirve para activar los campos de Fecha_Devolucion y así crear un Alquiler
    public void activarAlquiler(){
    //Variables que guardan las cajas de texto del FORMULARIO "cliente" y "pelicula"
    String dni = vista.getjTextFieldDNI().getText();  
    String codigo = vista.getjTextFieldCODIGO().getText();
        
        //Si el (DNI y CODIGO están vacios) Y ("cliente" DNI y "pelicula" CODIGO NO existen en la base de datos)
        //DESACTIVA EL BOTON ACTIVAR "alquiler"
        if((dni.isEmpty() && codigo.isEmpty()) && (compruebaClienteBD(controlExistenciaCliente) == 0 &&
                compruebaPeliculaBD(controlExistenciaPelicula) == 0)){
           vista.getjTextFieldDNICLIENTE().setEnabled(false);
           vista.getjTextFieldCODPELICULA().setEnabled(false);
           vista.getjTextFieldFECHADEVOLUCION().setEnabled(false);
        }
        
        //Si el (DNI y CODIGO no están vacios) Y ("cliente" DNI y "pelicula" CODIGO existen en la base de datos)
            //ACTIVA EL BOTON ACTIVAR "alquiler"
        if ((!dni.isEmpty() && !codigo.isEmpty()) && (compruebaClienteBD(controlExistenciaCliente) == 1 && 
                compruebaPeliculaBD(controlExistenciaPelicula) == 1)){
           vista.getjTextFieldFECHADEVOLUCION().setEnabled(true);
           vista.getjTextFieldDNICLIENTE().setText(dni);
           vista.getjTextFieldCODPELICULA().setText(codigo);
        }
    }
    
    //Método que borra las cajas de texto
    public void limpiarTexto(){
        vista.getjTextFieldDNI().setText("");
        vista.getjTextFieldNOMBRE().setText("");
        vista.getjTextFieldAPELLIDOS().setText("");
        vista.getjTextFieldEDAD().setText("");
        vista.getjTextFieldCODIGO().setText("");
        vista.getjTextFieldTITULO().setText("");
        vista.getjTextFieldDIRECTOR().setText("");
        vista.getjTextFielANIO().setText("");
        vista.getjTextFieldGENERO().setText("");
        vista.getjTextFieldFECHADEVOLUCION().setText("");
        vista.getjTextFieldDNICLIENTE().setText("");
        vista.getjTextFieldCODPELICULA().setText("");
        vista.getjTextArea1CAJA().setText("");
    }
    
    //Crea una tabla //VIEW !!! 
    public void tabla (){
       listaView = new ViewDAO().obtenerView();
       listaAlquiler = new AlquilerDAO().obtenerAlquiler();
        View v;

        vista.getTabla().addColumn("DNI");
        vista.getTabla().addColumn("NOMBRE");
        vista.getTabla().addColumn("APELLIDOS");
        vista.getTabla().addColumn("COD_PELI");
        vista.getTabla().addColumn("TITULO");
        vista.getTabla().addColumn("FECHA_ALQUILADA");
        vista.getTabla().addColumn("FECHA_DEVUELTA");
        
        vista.getjTable1().setModel(vista.getTabla());
        if (listaView.size() > 0){
          for (int i = 0; i < listaView.size(); i++) { 
                Object fila[] = new Object[7];
                v = listaView.get(i);
                
                fila[0] = v.getDniCliente();
                fila[1] = v.getNombre();
                fila[2] = v.getApellidos();
                fila[3] = v.getCodPelicula();
                fila[4] = v.getTitulo();
                fila[5] = v.getFecha_alquilada();
                fila[6] = v.getFecha_devolucion();
                
                vista.getTabla().addRow(fila);
            }
            vista.getjTable1().setModel(vista.getTabla());
            vista.getjTable1().setRowHeight(30);
        }  
    }
    
    //Método que rellena la listaCliente //Listas
    public void rellenaClienteOpcional(){
       List<Cliente> c = cliente.obtenerCliente();
       for (Cliente cliente1 : c) {
       vista.getL1().addElement(cliente1);
       }
       vista.getjListClientes().setModel(vista.getL1());
    }
    
    //Método que rellena la listaPelicula //Listas
    public void rellenaPeliculaOpcional(){
       List <Pelicula> p = pelicula.obtenerPeli();
       for (Pelicula pelicula1 : p) {
         vista.getL2().addElement(pelicula1);            
       }
         vista.getjListPeliculas().setModel(vista.getL2());   
    }
    
    //Método que actualiza la listaAlquiler //Listas
    public void actualizarListaAlquiler(){
    boolean activado = vista.getjButtonVolcarAlquileres().isEnabled();
        if(activado == true){
           vista.getjButtonActualizarL().setEnabled(false);
        }else{
            vista.getjButtonActualizarL().setEnabled(true);
            vista.getL3().removeAllElements();
            volcarAlquilerOpcional();
        }
    }
    
    //Método que rellena la listaAlquiler //Listas
    public void volcarAlquilerOpcional(){
       vista.getjButtonActualizarL().setEnabled(true);
       List <Alquiler> a = alquiler.obtenerAlquiler();
       for (Alquiler alquiler1 : a) {
         vista.getL3().addElement(alquiler1);            
       }
         vista.getjListAlquileres().setModel(vista.getL3());
         vista.getjButtonVolcarAlquileres().setEnabled(false);
    }
    
    //Método que desactiva botones del formulario Alquiler
        //También desactiva otros botones al arrancar la Aplicación
    public void desactivarCamposAlquiler(){
    String dni = vista.getjTextFieldDNI().getText();  
    String codigo = vista.getjTextFieldCODIGO().getText();
    
        if(dni.isEmpty() && codigo.isEmpty()){
          vista.getjTextFieldDNICLIENTE().setEnabled(false);
          vista.getjTextFieldCODPELICULA().setEnabled(false);
          vista.getjTextFieldFECHADEVOLUCION().setEnabled(false);
        }
        
        vista.getjTextFieldDniClienteT().setEnabled(false);
        vista.getjTextFieldNombreT().setEnabled(false);
        vista.getjTextFieldApellidosT().setEnabled(false);
        vista.getjTextFieldCodPeliculaT().setEnabled(false);
        vista.getjTextFieldTituloT().setEnabled(false);
        vista.getjTextFieldFecha_AlquiladaT().setEnabled(false);
        vista.getjTextFieldFecha_DevueltaT().setEnabled(false);
        vista.getjTextFieldNOMBRELISTA().setVisible(false);
        vista.getjTextFieldTITULOLISTA().setVisible(false);
    }
    
    //Método que desactiva el cuadro de Texto inferior derecha 
    public void creditosDesactivados(){
        vista.getjTextFieldCreditos1().setVisible(false);
        vista.getjTextFieldCreditos2().setVisible(false);
        vista.getjTextFieldCreditos3().setVisible(false);
    }
    
    //Método que activa el cuadro de Texto inferior derecha 
        //Por defecto se activar al ejecutar al App
    public void creditosActivados(){
        vista.getjTextFieldCreditos1().setVisible(true);
        vista.getjTextFieldCreditos2().setVisible(true);
        vista.getjTextFieldCreditos3().setVisible(true);
    }

    //Método similar al de borrarAquiler
        //Borra un alquiler cuando lo seleccionamos en la tabla con el raton click
        //Al pulsar el boton borrar de la tabla, borra el Alquiler de la bd y la tabla
    public void borrarCampoTabla(){
    listaAlquiler = new AlquilerDAO().obtenerAlquiler();
    int filaSeleccionada = vista.getjTable1().getSelectedRow();
    String dniClienteT = vista.getjTextFieldDniClienteT().getText();
    String codPeliculaT = vista.getjTextFieldCodPeliculaT().getText();

        for (Alquiler cl : listaAlquiler) { 
          if((dniClienteT.equals(cl.getDniCliente())) && (codPeliculaT.equals(cl.getCodPelicula()))){
            int respuesta = JOptionPane.showConfirmDialog(null, "¿ESTÁ SEGURO DE BORRAR EL ALQUILER?","Borrar", YES_NO_OPTION, QUESTION_MESSAGE);
            if(respuesta == YES_OPTION){ 
                  alquiler.borrarAlquiler(cl);
                  System.out.println(cl);
                  System.out.println(alquiler.borrarAlquiler(cl));
                  vista.getTabla().removeRow(filaSeleccionada);
                  System.out.println(filaSeleccionada);
                break;
            }
          }          
        }
    }
    
    //Metodo que al pusal el boton borrarListaCliente //Lista 
        //Borra el cliente de la lista y de la Base de Datos
    public void borrarClienteLista(){
    listaClientes = new ClienteDAO().obtenerCliente();
    int fila = vista.getjListClientes().getSelectedIndex();
    String nombreLista = vista.getjTextFieldNOMBRELISTA().getText().toString();

        for (Cliente cl : listaClientes) {  
            if(nombreLista.equals(cl.toString())){
               int respuesta = JOptionPane.showConfirmDialog(null, "¿ESTÁ SEGURO DE BORRAR EL CLIENTE?","Borrar", YES_NO_OPTION, QUESTION_MESSAGE);
                if(respuesta == YES_OPTION){ 
                  cliente.borrarCliente(cl);
                  vista.getL1().removeElementAt(fila);
                  break;
                }
            }          
        }
    }
    
    //Metodo que al pusal el boton borrarListaPelicula //Lista 
        //Borra la Pelicula de la lista y de la Base de Datos
    public void borrarPeliculaLista(){   
    listaPelis = new PeliculaDAO().obtenerPeli();
    int fila = vista.getjListPeliculas().getSelectedIndex();
    String tituloLista = vista.getjTextFieldTITULOLISTA().getText().toString();

        for (Pelicula cl : listaPelis) {  
            if(tituloLista.equals(cl.toString())){
               int respuesta = JOptionPane.showConfirmDialog(null, "¿ESTÁ SEGURO DE BORRAR LA PELICULA?","Borrar", YES_NO_OPTION, QUESTION_MESSAGE);
                if(respuesta == YES_OPTION){ 
                  pelicula.borrarPeli(cl);
                  vista.getL2().removeElementAt(fila);
                  break;
                }
            }          
        } 
    }
}
